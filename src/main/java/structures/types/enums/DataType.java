package structures.types.enums;

import fileio.MarketDataInput;
import structures.types.datapoints.DamPricePoint;
import structures.types.datapoints.DamPricePrediction;
import structures.types.datapoints.DataPoint;
import structures.types.datapoints.GridLoadPoint;
import structures.types.datapoints.GridLoadPredictionPoint;
import structures.types.datapoints.ImbalancePricePoint;
import structures.types.datapoints.ImbalanceVolPoint;

public enum DataType {
    DAM_PRICE("template/dam_prices.json", false) {
        @Override
        public DataPoint createPoint(final MarketDataInput input) {
            return new DamPricePoint(DAM_PRICE, input, isPremiumRequired());
        }
    },
    DAM_PRICE_PREDICTION("template/dam_prices_prediction.json", true) {
        @Override
        public DataPoint createPoint(final MarketDataInput input) {
            return new DamPricePrediction(DAM_PRICE, input, isPremiumRequired());
        }
    },
    GRID_LOAD("template/grid_load.json", false) {
        @Override
        public DataPoint createPoint(final MarketDataInput input) {
            return new GridLoadPoint(DAM_PRICE, input, isPremiumRequired());
        }
    },
    GRID_LOAD_PREDICTION("template/grid_load_prediction.json", false) {
        @Override
        public DataPoint createPoint(final MarketDataInput input) {
            return new GridLoadPredictionPoint(DAM_PRICE, input, isPremiumRequired());
        }
    },
    IMBALANCE_PRICE("template/imbalance_prices.json", false) {
        @Override
        public DataPoint createPoint(final MarketDataInput input) {
            return new ImbalancePricePoint(DAM_PRICE, input, isPremiumRequired());
        }
    },
    IMBALANCE_VOLUME("template/imbalance_volumes.json", false) {
        @Override
        public DataPoint createPoint(final MarketDataInput input) {
            return new ImbalanceVolPoint(DAM_PRICE, input, isPremiumRequired());
        }
    };

    private final String templatePath;
    private final boolean premiumRequired;

    DataType(final String templatePath, final boolean premiumRequired) {
        this.templatePath = templatePath;
        this.premiumRequired = premiumRequired;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    /**
     * Automatically creates the right type of DataPoint when calling
     * dataType.createPoint(...)
     */
    public abstract DataPoint createPoint(final MarketDataInput input);

    public boolean isPremiumRequired() {
        return premiumRequired;
    }
}
