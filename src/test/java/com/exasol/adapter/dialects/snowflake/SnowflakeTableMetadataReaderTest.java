package com.exasol.adapter.dialects.snowflake;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;

class SnowflakeTableMetadataReaderTest {

    @Test
    void testConstructor() {
        final AdapterProperties properties = new AdapterProperties(emptyMap());
        final SnowflakeTableMetadataReader reader = new SnowflakeTableMetadataReader(null, null, properties, null,
                BaseIdentifierConverter.createDefault());
        assertThat(reader, not(nullValue()));
    }
}
