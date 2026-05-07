package com.exasol.adapter.dialects.snowflake;

import static com.exasol.adapter.AdapterProperties.IGNORE_ERRORS_PROPERTY;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;

class SnowflakeTableMetadataReaderTest {
    private Map<String, String> rawProperties;
    private SnowflakeTableMetadataReader reader;

    @BeforeEach
    void beforeEach() {
        this.rawProperties = new HashMap<>();
        final AdapterProperties properties = new AdapterProperties(this.rawProperties);
        this.reader = new SnowflakeTableMetadataReader(null, null, properties, null,
                BaseIdentifierConverter.createDefault());
    }

    private void ignoreErrors(final String ignoreErrors) {
        this.rawProperties.put(IGNORE_ERRORS_PROPERTY, ignoreErrors);
    }
}
