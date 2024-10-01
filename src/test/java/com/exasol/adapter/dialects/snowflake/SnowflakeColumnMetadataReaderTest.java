package com.exasol.adapter.dialects.snowflake;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;
import com.exasol.adapter.jdbc.JDBCTypeDescription;
import com.exasol.adapter.metadata.DataType;

class SnowflakeColumnMetadataReaderTest {
    private SnowflakeColumnMetadataReader columnMetadataReader;
    private Map<String, String> rawProperties;

    @BeforeEach
    void beforeEach() {
        this.columnMetadataReader = createDefaultSnowflakeColumnMetadataReader();
        this.rawProperties = new HashMap<>();
    }

    private SnowflakeColumnMetadataReader createDefaultSnowflakeColumnMetadataReader() {
        return new SnowflakeColumnMetadataReader(null, AdapterProperties.emptyProperties(),
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

    @ValueSource(ints = {Types.SQLXML, Types.DISTINCT})
    @ParameterizedTest
    void testMapJdbcTypeFallbackToMaxVarChar(final int type) {
        assertThat(mapJdbcType(type), equalTo(DataType.createMaximumSizeVarChar(DataType.ExaCharset.UTF8)));
    }

    @Test
    void testMapJdbcTypeFallbackToParent() {
        assertThat(mapJdbcType(Types.BOOLEAN), equalTo(DataType.createBool()));
    }


}
