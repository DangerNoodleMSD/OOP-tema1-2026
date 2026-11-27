package structures.types.enums;

public enum ClientTier {
    FREE(false),
    PREMIUM(true);

    private final boolean fullAccess;

    ClientTier(final boolean fullAccess) {
        this.fullAccess = fullAccess;
    }

    /**
     * Checks whether this client tier can query the given data type.
     */
    public boolean canAccess(final DataType dataType) {
        return fullAccess || !dataType.isPremiumRequired();
    }
}
