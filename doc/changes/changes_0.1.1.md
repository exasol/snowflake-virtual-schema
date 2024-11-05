# Virtual Schema for Snowflake 0.1.1, released 2024-??-??

Code name: Fixed vulnerability CVE-2024-43382 in net.snowflake:snowflake-jdbc:jar:3.16.1:compile

## Summary

This release fixes the following vulnerability:

### CVE-2024-43382 (CWE-326) in dependency `net.snowflake:snowflake-jdbc:jar:3.16.1:compile`
Snowflake JDBC driver versions >= 3.2.6 and <= 3.19.1 have an Incorrect Security Setting that can result in data being uploaded to an encrypted stage without the additional layer of protection provided by client side encryption.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-43382?component-type=maven&component-name=net.snowflake%2Fsnowflake-jdbc&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-43382
* https://github.com/snowflakedb/snowflake-jdbc/security/advisories/GHSA-f686-hw9c-xw9c

## Security

* #7: Fixed vulnerability CVE-2024-43382 in dependency `net.snowflake:snowflake-jdbc:jar:3.16.1:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `net.snowflake:snowflake-jdbc:3.16.1` to `3.20.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.6.4` to `1.7.0`
* Updated `com.exasol:test-db-builder-java:3.5.3` to `3.6.0`
* Updated `com.exasol:udf-debugging-java:0.6.11` to `0.6.13`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter:5.10.1` to `5.11.3`
* Updated `org.mockito:mockito-junit-jupiter:5.10.0` to `5.14.2`
* Updated `org.testcontainers:junit-jupiter:1.19.4` to `1.20.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.3` to `4.4.0`
