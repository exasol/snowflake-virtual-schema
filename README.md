# Snowflake Virtual Schema

[![Build Status](https://github.com/exasol/snowflake-virtual-schema/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/snowflake-virtual-schema/actions/workflows/ci-build.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Asnowflake-virtual-schema&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Asnowflake-virtual-schema)

# Overview

The **Snowflake Virtual Schema** provides an abstraction layer that makes an external [Snowflake](https://www.Snowflake.org/) database accessible from an Exasol database through regular SQL commands. The contents of the external Snowflake database are mapped to virtual tables which look like and can be queried as any regular Exasol table.

If you want to set up a Virtual Schema for a different database system, please head over to the [Virtual Schemas Repository][virtual-schemas].

## Features

* Access a Snowflake database using a Virtual Schema.

## Table of Contents

### Information for Users

* [Virtual Schema User Guide](https://docs.exasol.com/database_concepts/virtual_schemas.htm)
* [Snowflake Dialect User Guide](doc/user_guide/snowflake_user_guide.md)
* [Changelog](doc/changes/changelog.md)
* [Dependencies](dependencies.md)

Find all the documentation in the [Virtual Schemas project][vs-doc].

## Information for Developers

* [Virtual Schema API Documentation](https://github.com/exasol/virtual-schema-common-java/blob/main/doc/development/api/virtual_schema_api.md)
* [Remote logging](https://docs.exasol.com/db/latest/database_concepts/virtual_schema/logging.htm)

## Additional Resources

* [Dependencies](dependencies.md)
* [Changelog](doc/changes/changelog.md)
