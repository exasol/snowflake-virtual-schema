package com.exasol.adapter.dialects.snowflake;

import java.sql.Connection;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;
import com.exasol.adapter.dialects.IdentifierConverter;
import com.exasol.adapter.jdbc.*;

/**
 * This class implements a reader for Snowflake-specific metadata.
 */
public class SnowflakeMetadataReader extends AbstractRemoteMetadataReader {
    /**
     * Create a new instance of the {@link SnowflakeMetadataReader}.
     *
     * @param connection connection to the Snowflake database
     * @param properties user-defined adapter properties
     */
    public SnowflakeMetadataReader(final Connection connection, final AdapterProperties properties) {
        super(connection, properties);
    }

    @Override
    public BaseTableMetadataReader createTableMetadataReader() {
        return new SnowflakeTableMetadataReader(this.connection, getColumnMetadataReader(), this.properties,
                getIdentifierConverter());
    }

    @Override
    protected IdentifierConverter createIdentifierConverter() {
        return BaseIdentifierConverter.createDefault();
    }

    @Override
    public ColumnMetadataReader createColumnMetadataReader() {
        return new SnowflakeColumnMetadataReader(this.connection, this.properties, getIdentifierConverter());
    }

    @Override
    public String getSchemaNameFilter() {
        return this.properties.getSchemaName().replace("_", "\\_").toUpperCase();
    }
}