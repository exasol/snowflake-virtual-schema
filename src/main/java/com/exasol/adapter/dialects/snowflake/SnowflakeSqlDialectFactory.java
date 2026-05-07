package com.exasol.adapter.dialects.snowflake;

import com.exasol.adapter.dialects.*;
import com.exasol.logging.VersionCollector;

/**
 * Factory for the Snowflake SQL dialect.
 */
public class SnowflakeSqlDialectFactory implements SqlDialectFactory {

    @Override
    public String getSqlDialectName() {
        return SnowflakeSqlDialect.NAME;
    }

    @Override
    public SqlDialect createSqlDialect(final JDBCAdapterContext context) {
        return new SnowflakeSqlDialect(context);
    }

    @Override
    public String getSqlDialectVersion() {
        final VersionCollector versionCollector = new VersionCollector(
                "META-INF/maven/com.exasol/snowflake-virtual-schema/pom.properties");
        return versionCollector.getVersionNumber();
    }

    @Override
    public String getAdapterProjectShortTag() {
        return "VSSF";
    }
}
