# Virtual Schema for Snowflake 1.0.2, released 2026-10-23

Code name: Secure Connections

## Summary

This release fixes security vulnerabilities in the Snowflake JDBC driver, ASM, and JSch dependencies.

## Security

* #31: Fixed CVE-2026-85525 (improper OCSP validation) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
* #32: Fixed CVE-2026-85528 (credential redirection) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
* #33: Fixed CVE-2026-86597 (sensitive information in logs) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
* #34: Fixed CVE-2026-86600 (workload identity token disclosure) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
* #39: Fixed CVE-2026-110257 (uncontrolled memory allocation) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #40: Fixed CVE-2026-110258 (uncontrolled memory allocation) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #41: Fixed CVE-2026-110259 (uncontrolled recursion) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #42: Fixed CVE-2026-110260 (algorithmic complexity) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #43: Fixed CVE-2026-110261 (algorithmic complexity) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #44: Fixed CVE-2026-110263 (uncontrolled recursion) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #37: Fixed CVE-2026-110262 (uncontrolled memory allocation) in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
* #38: Fixed CVE-2026-110270 (uncontrolled memory allocation) in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
* #30: Fixed CVE-2026-86231 (improper certificate revocation check) in dependency `com.github.mwiede:jsch:jar:2.28.5:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.4` to `14.1.0`

### Runtime Dependency Updates

* Updated `net.snowflake:snowflake-jdbc:4.3.2` to `4.4.0`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:8.0.1` to `8.0.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.3` to `1.7.4`
* Updated `com.exasol:test-db-builder-java:4.0.2` to `4.0.3`
* Updated `com.exasol:udf-debugging-java:0.6.20` to `0.6.22`
* Updated `com.exasol:virtual-schema-common-jdbc:14.0.4` to `14.1.0`
* Removed `com.exasol:virtual-schema-shared-integration-tests:3.0.3`
* Updated `org.mockito:mockito-junit-jupiter:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.20`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.8.0`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.6.3` to `3.6.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.23.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
