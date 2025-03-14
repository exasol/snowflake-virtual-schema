# Snowflake SQL Dialect User Guide

[Snowflake](https://www.snowflake.com/) operates a platform that provides data storage via cloud computing and allows for data analysis.

## Uploading the JDBC Driver to Exasol BucketFS

1. Download the [SnowflakeJDBC driver](https://docs.snowflake.com/en/developer-guide/jdbc/jdbc-download).

2. Upload the driver to BucketFS, see [BucketFS documentation](https://docs.exasol.com/db/latest/administration/on-premise/bucketfs/accessfiles.htm).

   Hint: Put the driver into folder `default/drivers/jdbc/` to register it for [ExaLoader](#registering-the-jdbc-driver-for-exaloader), too.

## Registering the JDBC driver for ExaLoader

In order to enable the ExaLoader to fetch data from the external database you must register the driver for ExaLoader as described in the [Installation procedure for JDBC drivers](https://github.com/exasol/docker-db/#installing-custom-jdbc-drivers).
1. ExaLoader expects the driver in BucketFS folder `default/drivers/jdbc`.<br />
   If you uploaded the driver for UDF to a different folder, then you need to [upload](#uploading-the-jdbc-driver-to-exasol-bucketfs) the driver again.
2. Additionally, you need to create file `settings.cfg` and [upload](#uploading-the-jdbc-driver-to-exasol-bucketfs) it to the same folder in BucketFS. Contents below:

```
DRIVERNAME=SNOWFLAKE_JDBC_DRIVER
JAR=<jar file containing the jdbc driver>
DRIVERMAIN=net.snowflake.client.jdbc.SnowflakeDriver
PREFIX=jdbc:snowflake:
FETCHSIZE=100000
INSERTSIZE=-1
NOSECURITY=YES

```
Make sure there's an empty line at the end of the `settings.cfg` file, as shown above, or it will not be properly read out, the EXALoader will display an error message.

| Variable                                | Description                      |
|-----------------------------------------|----------------------------------|
| `<jar file containing the jdbc driver>` | E.g. `snowflake-jdbc-3.16.1.jar` |

## Installing the Adapter Script

[Upload](https://docs.exasol.com/db/latest/administration/on-premise/bucketfs/accessfiles.htm) the latest available release of [Snowflake Virtual Schema JDBC Adapter](https://github.com/exasol/snowflake-virtual-schema/releases) to Bucket FS.

Then create a schema to hold the adapter script.

```sql
CREATE SCHEMA ADAPTER;
```

The SQL statement below creates the adapter script, defines the Java class that serves as entry point and tells the UDF framework where to find the libraries (JAR files) for Virtual Schema and database driver.

```sql
--/
CREATE OR REPLACE JAVA ADAPTER SCRIPT ADAPTER.SNOWFLAKE_JDBC_ADAPTER AS
  %scriptclass com.exasol.adapter.RequestDispatcher;
  %jar /buckets/<BFS service>/<bucket>/virtual-schema-dist-12.0.0-snowflake-0.1.3.jar;
  %jar /buckets/<BFS service>/<bucket>/drivers/jdbc/snowflake-jdbc-<snowflake-driver-version>.jar;
/
```

## Defining a Named Connection

Define the connection to the Snowflake database as shown below.

```sql
CREATE OR REPLACE CONNECTION SNOWFLAKE_CONNECTION
TO 'jdbc:snowflake://<account name>.snowflakecomputing.com'
USER '<user>'
IDENTIFIED BY '<password>';
```

| Variable        | Description                                                             |
|-----------------|-------------------------------------------------------------------------|
| `<account name` | Account name or 'account identifier' of the Snowflake platform account. |

The account name or account identifier is usually in the form of `xxx-xxx`: 
It is part of the specific login url you get upon registering (e.g.:`igzdtnt-du40000` in `https:// igzdtnt-du40000 .snowflakecomputing.com/`). 
It can also be found under the admin panel in Snowflake. 
For more info see:https://docs.snowflake.com/en/user-guide/admin-account-identifier

## Creating a Virtual Schema

Use the following SQL statement in the Exasol database to create a Snowflake Virtual Schema 
(Make sure to use UPPERCASE for the catalog and schema name):

```sql
CREATE VIRTUAL SCHEMA <virtual schema name>
  USING ADAPTER.SNOWFLAKE_JDBC_ADAPTER
  WITH
  CATALOG_NAME = '<catalog name>'
  SCHEMA_NAME = '<schema name>'
  CONNECTION_NAME = 'SNOWFLAKE_CONNECTION';
```

| Variable                | Description                                                                                          |
|-------------------------|------------------------------------------------------------------------------------------------------|
| `<virtual schema name>` | Name of the virtual schema you want to use.                                                          |
| `<catalog name>`        | Name of the catalog, usually equivalent to the name of the Snowflake database. Please use UPPERCASE. |
| `<schema name>`         | Name of the database schema you want to use in the Snowflake database. Please use UPPERCASE.         |


For additional parameters (optional), see also [Adapter Properties for JDBC-Based Virtual Schemas](https://github.com/exasol/virtual-schema-common-jdbc#adapter-properties-for-jdbc-based-virtual-schemas).

## Data Types Conversion

| Snowflake Data Type                                                                            | Supported | Converted Exasol Data Type | Known limitations                   |
|------------------------------------------------------------------------------------------------|-----------|----------------------------|-------------------------------------|
| NUMBER,DECIMAL , DEC , NUMERIC                                                                 | ✓         | DECIMAL(36,0)              | Precision > 36 is not supported.    |
| INT , INTEGER , BIGINT , SMALLINT , TINYINT , BYTEINT                                          | ✓         | VARCHAR(2000000)           | Alias for NUMBER(38,0) in Snowflake |
| BOOLEAN                                                                                        | ✓         | BOOLEAN                    |                                     |
| VARCHAR,CHAR, CHARACTER, NCHAR, STRING, TEXT, NVARCHAR, NVARCHAR2, CHAR VARYING, NCHAR VARYING | ✓         | VARCHAR                    |                                     |
| DATE                                                                                           | ✓         | DATE                       |                                     |
| FLOAT,DOUBLE PRECISION,DOUBLE , REAL                                                           | ✓         | DOUBLE                     |                                     |
| TIME                                                                                           | ✓         | TIME                       |                                     |
| TIMESTAMP                                                                                      | ✓         | TIMESTAMP                  |                                     |
| TIMESTAMP WITH TIME ZONE                                                                       | ✓         | TIMESTAMP (UTC)            |                                     |
