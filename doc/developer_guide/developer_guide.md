# Developers Guide

## Running the integration tests

Located in `SnowflakeSqlDialectIT.java`

### Locally

You need to add a `test.properties` file to the project folder that has the following structure:
`
snowflake.username = <username>
snowflake.accountname = <snowflake accountname>
snowflake.password = <password>
`

### In the GitHub CI

The credentials are stored in the following GitHub repository secrets:
- `USERNAME`
- `ACCOUNTNAME`
- `PASSWORD`
and get read out by the relevant CI workflows.


