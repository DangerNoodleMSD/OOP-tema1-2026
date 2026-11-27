# Tema 1 - Energex-Lite

## How I organized the project

I tried to split everything into packages under `structures/` so I don't end
up with one huge file in `main`:

- `main.Main`: just resets `Repo`, reads the commands using `InputLoader`,
  and runs them one by one with `CommandFactory`.
- `structures.repository.Repo`: Singleton that acts like the in-memory
  database. It keeps a `Map<DataType, TreeMap<LocalDate, DailyRepo>>` and
  also holds `SimulationClock`. Here I put `save`, `clearSlot`, `findLatest`,
  `findInRange`, and `findPreviousGridLoadQuantity`.
- `structures.types`:
  - `DailyRepo`: represents one day for a data type, keeps the template
    metadata and a `TreeMap<Integer, DataPoint>` with the 96 slots.
  - `SimulationClock`: handles the simulated time (1 tick = 1 minute) and
    checks if importing is active and on a 15-minute mark.
  - `TimeIntervalUtil`: helper for fixing interval strings (`00:00-00:15` to
    `00:00 - 00:15`) and turning slot indexes into timestamps.
- `structures.types.datapoints`: abstract `DataPoint` class and the 6
  subclasses (`DamPricePoint`, `DamPricePrediction`, `GridLoadPoint`,
  `GridLoadPredictionPoint`, `ImbalancePricePoint`, `ImbalanceVolPoint`).
- `structures.types.enums`: `DataType` (keeps each template file path, if it
  needs premium, and builds the right `DataPoint` via `createPoint`) and
  `ClientTier` (`FREE` / `PREMIUM`).
- `structures.templateio`: `TemplateLoader`, `DailyTemplate`, and
  `TemplatePoint` for reading `template/*.json` with Jackson.
- `structures.commands`: `Command` interface, `CommandFactory`,
  `ResponseBuilder`, `StartImportCommand`, `StopImportCommand`, `GetCommand`,
  and `AbstractIngestionCommand` (extended by `ImportCommand` and
  `PatchCommand`).

## OOP ideas I used

1. **Inheritance & Polymorphism on `DataPoint`**: all 6 types extend
   `DataPoint`. `toJson` is written once in the base class and calls
   `addSpecificFields`, and each subclass does its own `validate`. Also
   `DataType` overrides `createPoint` for each enum value so I didn't need a
   switch for creating datapoints.
2. **Command pattern + shared base class**: `CommandFactory` turns each
   `CommandInput` into a `Command` object and `Main` just calls `execute`.
   Because `ImportCommand` and `PatchCommand` do almost the same loop, I put
   the shared part in `AbstractIngestionCommand` and used an empty
   `onValidationFailure` hook that only `ImportCommand` overrides (to clear
   the slot when an active import fails, while `patch` keeps old valid data).
3. **No `instanceof`**: for `GRID_LOAD`'s 10000 MW change check, `Repo` has
   to grab `BigDecimal quantity` from a `DataPoint` stored in `DailyRepo`.
   Instead of casting or using `instanceof`, `DataPoint` has an empty
   `acceptGridLoadQuantity(Consumer<BigDecimal>)` method that only
   `GridLoadPoint` overrides.
4. **Templates & clock**: the first time a `(DataType, LocalDate)` gets
   saved, `Repo` loads its json from `template/` through `TemplateLoader`
   (using one `ObjectMapper`) so the metadata and 96 null slots are set, and
   `periodStart`/`periodEnd` are computed from the date.

## Stuff I got stuck on / changed along the way

- At first I started putting all the commands inside `Repo` and wanted to
  make `DailyRepo` abstract with 6 child classes. After realizing one
  `import` or `get` can touch multiple types or days, I moved commands into
  `structures.commands` and made `DailyRepo` hold any `DataPoint`.
- I also tried making my own `Date` class at the start, then switched to
  `LocalDate` and `LocalDateTime` because `TreeMap` sorting and midnight
  rollover for slot 96 (`23:45 - 00:00`) were way easier with them.
- Having 96 `null` values in `TreeMap` broke normal `lastEntry()` lookups,
  so `DailyRepo` scans backwards until the first non-null point
  (`findLastNonNullBefore` also makes sure `patch` doesn't compare a slot
  against itself).
- In `GetCommand`, `[from, to)` had to be figured out before checking if a
  `FREE` client has access, otherwise `target: "LATEST"` gives the wrong
  `from`/`to` on `ERROR`.

## AI usage

I used an AI assistant to help me understand the statement (since some parts
were pretty confusing), explain Java stuff I wasn't sure about and learn new
stuff (`TreeMap`, `Optional`, `Consumer`, Checkstyle errors), check my code
for bugs, and help me reorganise code from time to time to be more managable.
