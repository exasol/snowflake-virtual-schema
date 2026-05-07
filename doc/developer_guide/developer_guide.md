# Developer Guide

## Running the integration tests

Located in `SnowflakeSqlDialectIT.java`

### Locally

You need to add a `test.properties` file to the project folder that has the following structure:
```properties
snowflake.username = <username>
snowflake.password = <token>
snowflake.accountname = <snowflake account identifier>
```

To create a token, go to "Programmatic access tokens". Click "Generate Token" and select "All of my roles".

**Important:** After creating the token, click on its three-dot-menu, select "Bypass Network Policy Requirement" and click "Grant Access". Access will fail with `SnowflakeSQLException: Fail : Network policy is required` if this is not done.

### In the GitHub CI

The credentials are stored in the following GitHub repository secrets:
- `USERNAME`
- `ACCOUNTNAME`
- `PASSWORD`
and get read out by the relevant CI workflows.
