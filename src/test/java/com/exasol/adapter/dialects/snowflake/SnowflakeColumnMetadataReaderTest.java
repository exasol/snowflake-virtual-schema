package com.exasol.adapter.dialects.snowflake;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.sql.Types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;
import com.exasol.adapter.jdbc.JDBCTypeDescription;
import com.exasol.adapter.metadata.DataType;

@ExtendWith(MockitoExtension.class)
class SnowflakeColumnMetadataReaderTest {
    @Mock
    ExaMetadata exaMetaDataMock;
    private SnowflakeColumnMetadataReader columnMetadataReader;

    @BeforeEach
    void beforeEach() {
        this.columnMetadataReader = createDefaultSnowflakeColumnMetadataReader();
    }

    private SnowflakeColumnMetadataReader createDefaultSnowflakeColumnMetadataReader() {
        when(exaMetaDataMock.getDatabaseVersion()).thenReturn("1.2.3");
        return new SnowflakeColumnMetadataReader(null, AdapterProperties.emptyProperties(), exaMetaDataMock,
                BaseIdentifierConverter.createDefault());
    }

    @Test
    void testMapJdbcTypeOther() {
        assertThat(mapJdbcType(Types.OTHER), equalTo(DataType.createMaximumSizeVarChar(DataType.ExaCharset.UTF8)));
    }

    protected DataType mapJdbcType(final int type) {
        final JDBCTypeDescription jdbcTypeDescription = new JDBCTypeDescription(type, 0, 0, 0, "");
        return this.columnMetadataReader.mapJdbcType(jdbcTypeDescription);
    }

    @ValueSource(ints = { Types.SQLXML, Types.DISTINCT })
    @ParameterizedTest
    void testMapJdbcTypeFallbackToMaxVarChar(final int type) {
        assertThat(mapJdbcType(type), equalTo(DataType.createMaximumSizeVarChar(DataType.ExaCharset.UTF8)));
    }

    @Test
    void testMapJdbcTypeFallbackToParent() {
        assertThat(mapJdbcType(Types.BOOLEAN), equalTo(DataType.createBool()));
    }
}
