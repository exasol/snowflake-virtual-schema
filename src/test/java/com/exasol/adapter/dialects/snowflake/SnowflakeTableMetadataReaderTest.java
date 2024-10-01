package com.exasol.adapter.dialects.snowflake;

import static com.exasol.adapter.AdapterProperties.IGNORE_ERRORS_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;
import com.exasol.adapter.jdbc.RemoteMetadataReaderException;

class SnowflakeTableMetadataReaderTest {
    private Map<String, String> rawProperties;
    private SnowflakeTableMetadataReader reader;

    @BeforeEach
    void beforeEach() {
        this.rawProperties = new HashMap<>();
        final AdapterProperties properties = new AdapterProperties(this.rawProperties);
        this.reader = new SnowflakeTableMetadataReader(null, null, properties,
                BaseIdentifierConverter.createDefault());
    }

    private void ignoreErrors(final String ignoreErrors) {
        this.rawProperties.put(IGNORE_ERRORS_PROPERTY, ignoreErrors);
    }
}