package structures.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fileio.CommandInput;
import structures.types.DailyRepo;
import structures.types.enums.DataType;

public final class Repo {
    private LocalDateTime currentTime;
    private boolean importing;
    private static Repo repo = null;

    private Repo() {
    }

    private Map<DataType, TreeMap<LocalDate, DailyRepo>> data;

    /**
     * Returns a new repository if there is none initialized, otherwise returns
     * existing one
     */
    public static Repo getRepo() {
        if (repo == null) {
            repo = new Repo();
            repo.init();
        }

        return repo;
    }

    /**
     * Initializes empty storage maps for all data types and disables importing.
     */
    public void init() {
        data = new HashMap<>();
        for (DataType type : DataType.values()) {
            data.put(type, new TreeMap<>());
        }
        importing = false;
    }

    /**
     * Sets the current time to the time in the parameter timestamp
     *
     * @param timestamp
     *                  String with the format YYYY-MM-DD'T'hh:mm
     */
    private void setTime(final String timestamp) {
        this.currentTime = LocalDateTime.parse(timestamp);
    }

    /**
     * Method that runs when the import command is sent to the system
     *
     * @param cmd
     *            Command given
     */
    public void startImportCmd(final CommandInput cmd) {
        importing = true;
        this.setTime(cmd.getTimestamp());
    }

    /**
     * Method that runs when the import command is sent to the system
     *
     * @param cmd
     *            Command given
     */
    public void stopImportCmd(final CommandInput cmd) {
        importing = false;
        this.setTime(cmd.getTimestamp());
    }

    /**
     * Method that runs when the import command is sent to the system
     *
     * @param cmd
     *            Command given
     */
    public void importCmd(final CommandInput cmd) {
        if (importing) {

        }
        this.setTime(cmd.getTimestamp());
    }

    /**
     * Method that runs when the import command is sent to the system
     *
     * @param cmd
     *            Command given
     */
    public void patchCmd(final CommandInput cmd) {

        this.setTime(cmd.getTimestamp());
    }

    /**
     * Method that runs when the import command is sent to the system
     *
     * @param cmd
     *            Command given
     */
    public void getCmd(final CommandInput cmd) {

        this.setTime(cmd.getTimestamp());
    }
}
