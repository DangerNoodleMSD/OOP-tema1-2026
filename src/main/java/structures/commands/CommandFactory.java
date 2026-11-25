package structures.commands;

import fileio.CommandInput;

public final class CommandFactory {
    /**
     * Private constructor, since it's a utility class
     */
    private CommandFactory() {

    }

    /**
     * Creates the concrete Command corresponding to the given CommandInput.
     */
    public static Command create(final CommandInput commandInput) {
        return switch (commandInput.getCommand()) {
            case "startImport" -> new StartImportCommand(commandInput);
            case "stopImport" -> new StopImportCommand(commandInput);
            case "import" -> new ImportCommand(commandInput);
            case "patch" -> new PatchCommand(commandInput);
            case "get" -> new GetCommand(commandInput);
            default -> throw new IllegalArgumentException(
                    "Unknown command: " + commandInput.getCommand());
        };
    }
}
