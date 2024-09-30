package com.exasol.adapter.dialects.snowflake;

import java.sql.*;
import java.util.logging.Logger;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.IdentifierConverter;
import com.exasol.adapter.jdbc.BaseColumnMetadataReader;
import com.exasol.adapter.jdbc.JDBCTypeDescription;
import com.exasol.adapter.metadata.DataType;

/**
 * This class implements Snowflake-specific reading of column metadata.
 */
public class SnowflakeColumnMetadataReader extends BaseColumnMetadataReader {
    private static final Logger LOGGER = Logger.getLogger(SnowflakeColumnMetadataReader.class.getName());
    private static final String SNOWFLAKE_VARBIT_TYPE_NAME = "varbit";

    /**
     * Create a new instance of the {@link SnowflakeColumnMetadataReader}.
     *
     * @param connection          JDBC connection to the remote data source
     * @param properties          user-defined adapter properties
     * @param identifierConverter converter between source and Exasol identifiers
     */
    public SnowflakeColumnMetadataReader(final Connection connection, final AdapterProperties properties,
                                         final IdentifierConverter identifierConverter) {
        super(connection, properties, identifierConverter);
    }

    /**
     * Get the catalog name that is applied as filter criteria when looking up remote metadata.
     *
     * @return catalog name or <code>null</code> if metadata lookups are not limited by catalog
     */
    @Override
    public DataType mapJdbcType(final JDBCTypeDescription jdbcTypeDescription) {
        switch (jdbcTypeDescription.getJdbcType()) {
            case Types.OTHER:
                return mapJdbcTypeOther(jdbcTypeDescription);
            case Types.SQLXML:
            case Types.DISTINCT:
            case Types.BINARY:
                return DataType.createMaximumSizeVarChar(DataType.ExaCharset.UTF8);
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return DataType.createTimestamp(false);
            default:
                return super.mapJdbcType(jdbcTypeDescription);
        }
    }

    private DataType mapJdbcTypeOther(final JDBCTypeDescription jdbcTypeDescription) {
        if (isVarBitColumn(jdbcTypeDescription)) {
            final int n = jdbcTypeDescription.getPrecisionOrSize();
            LOGGER.finest(() -> "Mapping Snowflake datatype \"OTHER:varbit\" to VARCHAR(" + n + ")");
            return DataType.createVarChar(n, DataType.ExaCharset.UTF8);
        } else {
            LOGGER.finest(() -> "Mapping Snowflake datatype \"" + jdbcTypeDescription.getTypeName()
                    + "\" to maximum VARCHAR()");
            return DataType.createMaximumSizeVarChar(DataType.ExaCharset.UTF8);
        }
    }

    private boolean isVarBitColumn(final JDBCTypeDescription jdbcTypeDescription) {
        return jdbcTypeDescription.getTypeName().equals(SNOWFLAKE_VARBIT_TYPE_NAME);
    }

    @Override
    public String readColumnName(final ResultSet columns) throws SQLException {
        return super.readColumnName(columns);
    }

    @Override
    public String getSchemaNameFilter() {
        return this.properties.getSchemaName().toUpperCase();
    }
}