package com.exasol.adapter.dialects.snowflake;

import static com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language.JAVA;

import java.io.*;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeoutException;

import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.containers.ExasolService;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.drivers.JdbcDriver;
import com.exasol.errorreporting.ExaError;
import com.exasol.udfdebugging.UdfTestSetup;
import com.github.dockerjava.api.model.ContainerNetwork;

/**
 * This class contains the common integration test setup for all Snowflake virtual schemas.
 */
public class SnowflakeVirtualSchemaIntegrationTestSetup implements Closeable {
    private static final String VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION = "virtual-schema-dist-12.0.0-snowflake-0.1.0.jar";
    private static final Path PATH_TO_VIRTUAL_SCHEMAS_JAR = Path.of("target", VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION);
    private static final String SCHEMA_EXASOL = "SCHEMA_EXASOL";
    private static final String ADAPTER_SCRIPT_EXASOL = "ADAPTER_SCRIPT_EXASOL";
    private static final String EXASOL_DOCKER_IMAGE_REFERENCE = "8.24.0";
    private static final String SNOWFLAKE_CONTAINER_NAME = "localstack/snowflake:14.2";

    private static final String JDBC_DRIVER_NAME = "snowflake-jdbc.jar";
    private static final Path JDBC_DRIVER_PATH = Path.of("target/snowflake-driver/" + JDBC_DRIVER_NAME);

    private static final int SNOWFLAKE_PORT = 5432;
    private final Statement snowflakeStatement;
    private final ExasolContainer<? extends ExasolContainer<?>> exasolContainer = new ExasolContainer<>(
            EXASOL_DOCKER_IMAGE_REFERENCE).withRequiredServices(ExasolService.BUCKETFS, ExasolService.UDF)
            .withReuse(true);
    private final Connection exasolConnection;
    private final Statement exasolStatement;
    private final AdapterScript adapterScript;
    private final ConnectionDefinition connectionDefinition;
    private final ExasolObjectFactory exasolFactory;
    private final Connection snowflakeConnection;
    private int virtualSchemaCounter = 0;
    private String userName;
    private String password;
    private String accountName;
    private String databaseName;

    public String randomDbAddendum() {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        final int targetStringLength = 4;
        final Random random = new Random();

        final String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

        return generatedString;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    SnowflakeVirtualSchemaIntegrationTestSetup() {
        try {
            this.databaseName = "TESTDB" + randomDbAddendum().toUpperCase();
            this.exasolContainer.start();
            final Bucket bucket = this.exasolContainer.getDefaultBucket();
            uploadDriverToBucket(this.exasolContainer);
            uploadVsJarToBucket(bucket);
            this.exasolConnection = this.exasolContainer.createConnection("");
            this.exasolStatement = this.exasolConnection.createStatement();
            getTestCredentials();
            this.snowflakeConnection = getSnowflakeConnection(userName, password, accountName);
            this.snowflakeStatement = snowflakeConnection.createStatement();
            final String hostIpAddress = getTestHostIpFromInsideExasol();
            assert (hostIpAddress != null);
            final UdfTestSetup udfTestSetup = new UdfTestSetup(hostIpAddress, this.exasolContainer.getDefaultBucket(),
                    this.exasolConnection);
            this.exasolFactory = new ExasolObjectFactory(this.exasolContainer.createConnection(""),
                    ExasolObjectConfiguration.builder().withJvmOptions(udfTestSetup.getJvmOptions()).build());
            final ExasolSchema exasolSchema = this.exasolFactory.createSchema(SCHEMA_EXASOL);
            this.adapterScript = createAdapterScript(exasolSchema);
            final String connectionString = getSnowflakeConnectionString(accountName);
            connectionDefinition = getSnowflakeConnectionDefinition(connectionString, userName, password);
        } catch (final SQLException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to created snowflake test setup.", exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionDefinition getSnowflakeConnectionDefinition(final String connectionString, final String username,
                                                                  final String password) {
        final ConnectionDefinition connectionDefinition;
        connectionDefinition = this.exasolFactory.createConnectionDefinition("SNOWFLAKE_CONNECTION", connectionString,
                username, password);
        return connectionDefinition;
    }

    private String getSnowflakeConnectionString(final String accountname) {
        final String connectionString = "jdbc:snowflake://" + accountname + ".snowflakecomputing.com";
        return connectionString;
    }

    private Connection getSnowflakeConnection(final String username, final String password, final String accountname)
            throws SQLException {
        try {
            Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
        } catch (final ClassNotFoundException ex) {
            System.err.println("Driver not found");
        }

        // build connection properties
        final Properties properties = new Properties();
        properties.put("user", username); // replace "" with your username
        properties.put("password", password); // replace "" with your password
        properties.put("account", accountname); // replace "" with your account name
        properties.put("db", this.databaseName); // replace "" with target database name
        properties.put("schema", "TESTSCHEMA"); // replace "" with target schema name
        // properties.put("tracing", "on");

        // create a new connection
        String connectStr = System.getenv("SF_JDBC_CONNECT_STRING");
        // use the default connection string if it is not set in environment
        if (connectStr == null) {
            connectStr = "jdbc:snowflake://" + accountname + ".snowflakecomputing.com"; // replace accountName with your account name
        }
        return DriverManager.getConnection(connectStr, properties);
    }

    private void getTestCredentials() {
        TestConfig testConfig = TestConfig.read();
        this.userName = testConfig.getSnowflakeUsername();
        this.password = testConfig.getSnowflakePassword();
        this.accountName = testConfig.getSnowflakeAccountname();
    }

    private static void uploadDriverToBucket(final ExasolContainer<? extends ExasolContainer<?>> container)
            throws InterruptedException, TimeoutException, BucketAccessException {
        try {
            container.getDriverManager().install( //
                    JdbcDriver.builder("SNOWFLAKE_JDBC_DRIVER") //
                            .enableSecurityManager(false) //
                            .mainClass("net.snowflake.client.jdbc.SnowflakeDriver") //
                            .prefix("jdbc:snowflake:") //
                            .sourceFile(JDBC_DRIVER_PATH) //
                            .build());

        } catch (final Exception exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("F-VSSF-8")
                            .message("An error occurred while uploading the jdbc driver to the bucket.")
                            .mitigation("Make sure the {{JDBC_DRIVER_PATH}} file exists.")
                            .parameter("JDBC_DRIVER_PATH", JDBC_DRIVER_PATH)
                            .mitigation("You can generate it by executing the integration test with maven.").toString(),
                    exception);
        }
    }

    private static void uploadVsJarToBucket(final Bucket bucket) {
        try {
            bucket.uploadFile(PATH_TO_VIRTUAL_SCHEMAS_JAR, VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION);
        } catch (FileNotFoundException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to upload jar to bucket " + bucket, exception);
        }
    }

    private AdapterScript createAdapterScript(final ExasolSchema schema) {
        final String content = "%scriptclass com.exasol.adapter.RequestDispatcher;\n" //
                + "%jar /buckets/bfsdefault/default/" + VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION + ";\n";
        return schema.createAdapterScript(ADAPTER_SCRIPT_EXASOL, JAVA, content);
    }

    public Statement getSnowflakeStatement() {
        return this.snowflakeStatement;
    }

    public Statement getExasolStatement() {
        return this.exasolStatement;
    }

    public ExasolContainer<? extends ExasolContainer<?>> getExasolContainer() {
        return this.exasolContainer;
    }

    public VirtualSchema createVirtualSchema(final String forSnowflakeSchema,
                                             final Map<String, String> additionalProperties) {
        final Map<String, String> properties = new HashMap<>(Map.of("CATALOG_NAME", databaseName, //
                "SCHEMA_NAME", forSnowflakeSchema)); //
        properties.putAll(additionalProperties);
        return this.exasolFactory
                .createVirtualSchemaBuilder("SNOWFLAKE_VIRTUAL_SCHEMA_" + (this.virtualSchemaCounter++))
                .adapterScript(this.adapterScript).connectionDefinition(this.connectionDefinition)
                .properties(properties).build();
    }

    public ExasolObjectFactory getExasolFactory() {
        return this.exasolFactory;
    }

    @Override
    public void close() {
        try {
            this.exasolStatement.close();
            this.exasolConnection.close();
            this.snowflakeStatement.close();
            this.snowflakeConnection.close();
            this.exasolContainer.stop();
        } catch (final SQLException exception) {
            throw new IllegalStateException("Failed to stop test setup.", exception);
        }
    }

    private String getTestHostIpFromInsideExasol() {
        final Map<String, ContainerNetwork> networks = this.exasolContainer.getContainerInfo().getNetworkSettings()
                .getNetworks();
        if (networks.size() == 0) {
            return null;
        }
        return networks.values().iterator().next().getGateway();
    }
}
