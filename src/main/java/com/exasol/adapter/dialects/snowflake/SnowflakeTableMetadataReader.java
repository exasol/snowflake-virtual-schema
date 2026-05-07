package com.exasol.adapter.dialects.snowflake;

import java.sql.Connection;
import java.util.logging.Logger;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.IdentifierConverter;
import com.exasol.adapter.jdbc.BaseTableMetadataReader;
import com.exasol.adapter.jdbc.ColumnMetadataReader;

/**
 * This class handles the specifics of mapping Snowflake table metadata to Exasol.
 */
public class SnowflakeTableMetadataReader extends BaseTableMetadataReader {
    static final Logger LOGGER = Logger.getLogger(SnowflakeTableMetadataReader.class.getName());

    /**
     * Create a new {@link SnowflakeTableMetadataReader} instance.
     *
     * @param connection           JDBC connection to the remote data source
     * @param columnMetadataReader reader to be used to map the metadata of the tables columns
     * @param properties           user-defined adapter properties
     * @param exaMetadata          Exasol metadata
     * @param identifierConverter  converter between source and Exasol identifiers
     */
    public SnowflakeTableMetadataReader(final Connection connection, final ColumnMetadataReader columnMetadataReader,
            final AdapterProperties properties, final ExaMetadata exaMetadata, final IdentifierConverter identifierConverter) {
        super(connection, columnMetadataReader, properties, exaMetadata, identifierConverter);
    }
}
