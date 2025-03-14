# Virtual Schema for Snowflake 0.1.3, released 2025-03-14

Code name: Fixed CVE-2025-27496 by updating Snowflake Driver

## Summary

In versions before 3.23.1 the Snowflake driver would log the master encryption key when the log level was set to `DEBUG`. We updated the dependency on the snowflake driver to 3.23.1 as a fix.

## Features

* Fixed CVE-2025-27496 by updating snowflake driver to 3.23.1 (PR #15)

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:12.0.0` to `12.0.1`

### Runtime Dependency Updates

* Updated `net.snowflake:snowflake-jdbc:3.22.0` to `3.23.1`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.3` to `7.1.4`
* Updated `com.exasol:udf-debugging-java:0.6.14` to `0.6.15`
* Updated `com.exasol:virtual-schema-common-jdbc:12.0.0` to `12.0.1`
* Added `org.junit-pioneer:junit-pioneer:2.3.0`
* Added `org.junit.jupiter:junit-jupiter-api:5.12.0`
* Removed `org.junit.jupiter:junit-jupiter:5.11.4`
* Updated `org.mockito:mockito-junit-jupiter:5.15.2` to `5.16.0`
* Added `org.slf4j:slf4j-jdk14:2.0.17`
* Updated `org.testcontainers:junit-jupiter:1.20.4` to `1.20.6`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.2` to `0.4.3`
* Updated `com.exasol:project-keeper-maven-plugin:4.5.0` to `5.0.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`
