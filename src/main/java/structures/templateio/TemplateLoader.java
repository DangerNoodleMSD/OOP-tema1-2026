package structures.templateio;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import structures.types.enums.DataType;

public final class TemplateLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TemplateLoader() {
    }

    /**
     * Loads the daily document template for the given data type.
     *
     * @param type market data type
     * @return deserialized template
     */
    public static DailyTemplate load(final DataType type) {
        try {
            return MAPPER.readValue(new File(type.getTemplatePath()), DailyTemplate.class);
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to load template: "
                    + type.getTemplatePath(), e);
        }
    }
}
