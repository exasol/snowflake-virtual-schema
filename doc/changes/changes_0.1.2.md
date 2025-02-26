# Virtual Schema for Snowflake 0.1.2, released 2025-02-10

Code name: Fixed vulnerabilities in net.snowflake:snowflake-jdbc:jar:3.20.0:compile

## Summary

This release fixes vulnerabilities [CVE-2025-24789](https://github.com/advisories/GHSA-7hpq-3g6w-pvhf)
and [CVE-2025-24790](https://github.com/advisories/GHSA-33g6-495w-v8j2) in dependency `net.snowflake:snowflake-jdbc:jar:3.20.0:runtime`

## Security

* #10: Fixed vulnerability CVE-2025-24789 in dependency net.snowflake:snowflake-jdbc:jar:3.20.0:runtime
* #11: Fixed vulnerability CVE-2025-24790 in dependency net.snowflake:snowflake-jdbc:jar:3.20.0:runtime

## Dependency Updates

### Runtime Dependency Updates

* Updated `net.snowflake:snowflake-jdbc:3.20.0` to `3.22.0`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.1` to `7.1.3`
* Updated `com.exasol:udf-debugging-java:0.6.13` to `0.6.14`
* Updated `org.junit.jupiter:junit-jupiter:5.11.3` to `5.11.4`
* Updated `org.mockito:mockito-junit-jupiter:5.14.2` to `5.15.2`
* Updated `org.testcontainers:junit-jupiter:1.20.3` to `1.20.4`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.4.0` to `4.5.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.8.0` to `3.8.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.1` to `3.5.2`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`
