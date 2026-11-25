package structures.types.enums;

public enum DataType {
    DAM_PRICE("template/dam_prices.json"),
    DAM_PRICE_PREDICTION("template/dam_prices_prediction.json"),
    GRID_LOAD("template/grid_load.json"),
    GRID_LOAD_PREDICTION("template/grid_load_prediction.json"),
    IMBALANCE_PRICE("template/imbalance_prices.json"),
    IMBALANCE_VOLUME("template/imbalance_volumes.json");

    private final String templatePath;

    DataType(final String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }
}
