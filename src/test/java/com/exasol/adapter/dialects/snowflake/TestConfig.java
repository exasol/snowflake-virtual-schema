package com.exasol.adapter.dialects.snowflake;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.Logger;

public class TestConfig {
    private static final Logger LOGGER = Logger.getLogger(TestConfig.class.getName());
    private static final Path CONFIG_FILE = Paths.get("test.properties").toAbsolutePath();
    private final Properties properties;

    private TestConfig(final Properties properties) {
        this.properties = properties;
    }

    public static TestConfig read() {
        final Path file = CONFIG_FILE;
        if (!Files.exists(file)) {
            throw new IllegalStateException("Config file " + file + " does not exist.");
        }
        return new TestConfig(loadProperties(file));
    }

    private static Properties loadProperties(final Path configFile) {
        LOGGER.info(() -> "Reading config file " + configFile);
        try (InputStream stream = Files.newInputStream(configFile)) {
            final Properties props = new Properties();
            props.load(stream);
            return props;
        } catch (final IOException exception) {
            throw new UncheckedIOException("Error reading config file " + configFile, exception);
        }
    }


    private String getMandatoryValue(final String param) {
        if (!properties.containsKey(param)) {
            throw new IllegalStateException(
                    "Config file " + CONFIG_FILE + " does not contain parameter '" + param + "'");
        }
        return this.properties.getProperty(param);
    }

    public String getSnowflakeUsername() {
        return getMandatoryValue("snowflake.username");
    }

    public String getSnowflakeAccountname() {
        return getMandatoryValue("snowflake.accountname");
    }

    public String getSnowflakePassword() {
        return getMandatoryValue("snowflake.password");
    }

}