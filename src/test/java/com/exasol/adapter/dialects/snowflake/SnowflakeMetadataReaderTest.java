package com.exasol.adapter.dialects.snowflake;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterProperties;

@ExtendWith(MockitoExtension.class)
class SnowflakeMetadataReaderTest {
    @Mock
    ExaMetadata exaMetadataMock;
    private SnowflakeMetadataReader reader;

    @BeforeEach
    void beforeEach() {
        when(exaMetadataMock.getDatabaseVersion()).thenReturn("1.2.3");
        this.reader = new SnowflakeMetadataReader(null, AdapterProperties.emptyProperties(), exaMetadataMock);
    }

    @Test
    void testGetTableMetadataReader() {
        assertThat(this.reader.getTableMetadataReader(), instanceOf(SnowflakeTableMetadataReader.class));
    }

    @Test
    void testGetColumnMetadataReader() {
        assertThat(this.reader.getColumnMetadataReader(), instanceOf(SnowflakeColumnMetadataReader.class));
    }
}
