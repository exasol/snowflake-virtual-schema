# Virtual Schema for Snowflake 1.0.2, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-86231, CVE-2026-85525, CVE-2026-85528, CVE-2026-86597, CVE-2026-86600

## Summary

This release fixes the following 5 vulnerabilities:

### CVE-2026-86231 (CWE-298) in dependency `com.github.mwiede:jsch:jar:2.28.5:test`
A security flaw has been discovered in mwiede jsch up to 2.28.5. Affected is the function getRevokedKeys of the file src/main/java/com/jcraft/jsch/KnownHosts.java. Performing a manipulation of the argument known_hosts results in improper check for certificate revocation. The attack is possible to be carried out remotely. The attack is considered to have high complexity. The exploitability is told to be difficult. The exploit has been released to the public and may be used for attacks. Upgrading to version 2.28.6 is able to address this issue. The patch is named 194a2f76a5c0f1c3f778565be3fd66bcafc42d23. You should upgrade the affected component.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-86231?component-type=maven&component-name=com.github.mwiede%2Fjsch&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-86231
* https://github.com/mwiede/jsch/issues/1091
* https://github.com/mwiede/jsch/pull/1098
* https://github.com/mwiede/jsch/releases/tag/jsch-2.28.6

### CVE-2026-85525 (CWE-295) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
Improper OCSP response validation in the Snowflake Python, Go, JDBC, and Node.js drivers allowed a revoked TLS certificate to be accepted as valid, because OCSP responses were not reliably bound to the certificate being validated and definitive verification failures were treated as transient. A man-in-the-middle attacker holding a revoked certificate and its private key for a Snowflake or stage hostname could cause the driver to establish a TLS session to the attacker-controlled endpoint anyway, allowing the attacker to read and modify data transmitted within that connection. Successful exploitation requires that on-path position and the corresponding private key, and impact is limited to data carried within the intercepted connection. The fix is available in Snowflake Connector for Python v4.7.3, Snowflake Go Driver v2.2.0, Snowflake JDBC Driver v4.3.4 (including the snowflake-jdbc-fips and snowflake-jdbc-thin), and Snowflake Node.js Driver v3.3.0. Users must manually upgrade.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-85525?component-type=maven&component-name=net.snowflake%2Fsnowflake-jdbc&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-85525
* https://docs.snowflake.com/en/release-notes/clients-drivers/golang-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/jdbc-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/nodejs-2026
* https://github.com/snowflakedb/snowflake-connector-python/releases/tag/v4.7.3

### CVE-2026-85528 (CWE-20) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
Improper input validation of the auto-configuration account identifier in Snowflake JDBC Driver versions 4.2.0 through 4.3.3 allowed a credential-bearing login request to be redirected to an attacker-selected HTTPS endpoint. An attacker able to control the account value could cause the driver to transmit a reusable login credential to a host of their choosing and replay it to obtain the privileges granted to that credential. Successful exploitation requires an application using jdbc:snowflake:auto with a connections.toml section that omits an explicit host and a lower-trust principal able to set the account value; ordinary JDBC URLs are unaffected. The fix is available in Snowflake JDBC Driver version 4.3.4, including the snowflake-jdbc-fips and snowflake-jdbc-thin. Users must manually upgrade.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-85528?component-type=maven&component-name=net.snowflake%2Fsnowflake-jdbc&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-85528
* https://docs.snowflake.com/en/release-notes/clients-drivers/jdbc-2026#version-434-sep-03-2026

### CVE-2026-86597 (CWE-532) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
Insertion of sensitive information into log files in the Snowflake Python, Go, JDBC, Node.js, PHP PDO, and ODBC drivers allowed authentication tokens, query-result encryption keys, pre-signed cloud-storage URLs, and SAML assertions to be written to diagnostic logs in circumstances where the available log redaction did not cover all affected log paths and data types. An attacker with read access to the log destination, whether the local filesystem, a log aggregation service, or a CI/CD artifact store, could obtain credentials and decryption keys that, if still valid at the time of access, could be used to authenticate to the corresponding Snowflake account or cloud-storage object. Successful exploitation requires read access to the log destination, and impact is bounded by credential lifetime and object scope. The fix is available in Snowflake Connector for Python v4.7.3, Snowflake Go Driver v2.2.0, Snowflake JDBC Driver v4.3.4 (including the snowflake-jdbc-fips and snowflake-jdbc-thin), Snowflake Node.js Driver v3.3.0, Snowflake PHP PDO Driver v4.2.0, and Snowflake ODBC Driver v3.20.0. Users must manually upgrade and should securely delete previously generated diagnostic logs containing sensitive information where retention is not required.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-86597?component-type=maven&component-name=net.snowflake%2Fsnowflake-jdbc&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-86597
* https://docs.snowflake.com/en/release-notes/clients-drivers/golang-2026#version-220-sep-03-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/jdbc-2026#version-434-sep-03-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/nodejs-2026#version-330-september-3-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/odbc-2026#version-3200-sep-3-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/php-pdo-2026#version-420-sep-3-2026

### CVE-2026-86600 (CWE-441) in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
In affected Snowflake drivers, WORKLOAD_IDENTITY authentication requests a cloud workload-identity token and attaches it to the login request without verifying that the configured host is a Snowflake endpoint. An attacker who can modify the connection configuration can cause the driver to mint a fresh attestation and send it to a host they control. The captured token can be replayed to Snowflake for its remaining lifetime in accounts where that workload identity is already registered. On Azure, the token audience is also taken from connection configuration. Combined with an attacker-controlled host, the driver can request a Managed Identity access token scoped to a non-Snowflake Azure resource and deliver it to the attacker. That path is the only case in which impact extends beyond Snowflake; it is bounded by the token lifetime and the managed identityâs permissions. Successful exploitation requires WORKLOAD_IDENTITY authentication on a workload that already has an ambient cloud identity. Patched driver versions restrict this authenticator to recognized Snowflake hosts. Users must manually upgrade.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-86600?component-type=maven&component-name=net.snowflake%2Fsnowflake-jdbc&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-86600
* https://docs.snowflake.com/en/release-notes/clients-drivers/dotnet-2026#version-610-september-3-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/golang-2026#version-220-sep-03-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/jdbc-2026#version-434-sep-03-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/nodejs-2026#version-330-september-3-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/odbc-2026#version-3200-sep-3-2026
* https://docs.snowflake.com/en/release-notes/clients-drivers/php-pdo-2026#version-420-sep-3-2026
* https://github.com/snowflakedb/libsnowflakeclient/releases/tag/v2.10.0
* https://github.com/snowflakedb/snowflake-connector-python/releases/tag/v4.7.3

## Security

* #30: Fixed vulnerability CVE-2026-86231 in dependency `com.github.mwiede:jsch:jar:2.28.5:test`
* #31: Fixed vulnerability CVE-2026-85525 in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
* #32: Fixed vulnerability CVE-2026-85528 in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
* #33: Fixed vulnerability CVE-2026-86597 in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`
* #34: Fixed vulnerability CVE-2026-86600 in dependency `net.snowflake:snowflake-jdbc:jar:4.3.2:runtime`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.4` to `14.0.5`

### Runtime Dependency Updates

* Updated `net.snowflake:snowflake-jdbc:4.3.2` to `4.3.4`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:8.0.1` to `8.0.2`
* Updated `com.exasol:virtual-schema-common-jdbc:14.0.4` to `14.0.5`
* Updated `org.junit.jupiter:junit-jupiter-api:5.14.4` to `6.1.3`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.7.5`
