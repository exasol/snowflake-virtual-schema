# Virtual Schema for Snowflake 0.1.3, released 2025-??-??

Code name: Fixed vulnerability CVE-2025-27496 in net.snowflake:snowflake-jdbc:jar:3.22.0:runtime

## Summary

This release fixes the following vulnerability:

### CVE-2025-27496 (CWE-532) in dependency `net.snowflake:snowflake-jdbc:jar:3.22.0:runtime`
Snowflake, a platform for using artificial intelligence in the context of cloud computing, has a vulnerability in the Snowflake JDBC driver ("Driver") in versions 3.0.13 through 3.23.0 of the driver. When the logging level was set to DEBUG, the Driver would log locally the client-side encryption master key of the target stage during the execution of GET/PUT commands. This key by itself does not grant access to any sensitive data without additional access authorizations,  and is not logged server-side by Snowflake. Snowflake fixed the issue in version 3.23.1.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://ossindex.sonatype.org/vulnerability/CVE-2025-27496 for details
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-27496?component-type=maven&component-name=net.snowflake%2Fsnowflake-jdbc&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-27496
* https://github.com/snowflakedb/snowflake-jdbc/security/advisories/GHSA-q298-375f-5q63

## Security

* #16: Fixed vulnerability CVE-2025-27496 in dependency `net.snowflake:snowflake-jdbc:jar:3.22.0:runtime`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:12.0.0` to `12.0.1`

### Runtime Dependency Updates

* Updated `net.snowflake:snowflake-jdbc:3.22.0` to `3.23.1`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.3` to `7.1.4`
* Updated `com.exasol:udf-debugging-java:0.6.14` to `0.6.15`
* Updated `com.exasol:virtual-schema-common-jdbc:12.0.0` to `12.0.1`
* Updated `org.junit.jupiter:junit-jupiter:5.11.4` to `5.12.1`
* Updated `org.mockito:mockito-junit-jupiter:5.15.2` to `5.16.1`
* Updated `org.testcontainers:junit-jupiter:1.20.4` to `1.20.6`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.5.0` to `5.0.0`
