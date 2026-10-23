# Developer Guide

## Running the integration tests

Located in `SnowflakeSqlDialectIT.java`

### Locally

You need to add a `test.properties` file to the project folder that has the following structure:
```properties
snowflake.username = <username>
snowflake.password = <token>
snowflake.accountname = <snowflake account identifier>
snowflake.database = <pre-created test database>
```

### In the GitHub CI

The credentials are stored in the following GitHub repository secrets:
- `USERNAME`
- `ACCOUNTNAME`
- `PASSWORD`
- `SNOWFLAKE_TEST_DATABASE`
and get read out by the relevant CI workflows.
