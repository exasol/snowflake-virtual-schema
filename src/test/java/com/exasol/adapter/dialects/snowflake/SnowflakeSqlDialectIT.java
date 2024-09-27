package com.exasol.adapter.dialects.snowflake;

import static com.exasol.matcher.ResultSetMatcher.matchesResultSet;
import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import com.exasol.closeafterall.CloseAfterAll;
import com.exasol.closeafterall.CloseAfterAllExtension;
import com.exasol.dbbuilder.dialects.Schema;
import com.exasol.dbbuilder.dialects.exasol.VirtualSchema;
import com.exasol.matcher.TypeMatchMode;

@Tag("integration")
@ExtendWith({CloseAfterAllExtension.class})
class SnowflakeSqlDialectIT {
    @CloseAfterAll
    private static final SnowflakeVirtualSchemaIntegrationTestSetup SETUP = new SnowflakeVirtualSchemaIntegrationTestSetup();
    private static final String SCHEMA_SNOWFLAKE = "SCHEMA_SNOWFLAKE";
    private static final String SCHEMA_SNOWFLAKE_UPPERCASE_TABLE = "SCHEMA_SNOWFLAKE_UPPER";
    private static final String TABLE_SNOWFLAKE_SIMPLE = "table_snowflake_simple";
    private static final String TABLE_SNOWFLAKE_ALL_DATA_TYPES = "table_snowflake_all_data_types";
    private static Schema exasolSchema;
    private static VirtualSchema virtualSchemaSnowflake;
    private static VirtualSchema virtualSchemaSnowflakeUppercaseTable;
    private static final String TABLE_JOIN_1 = "TABLE_JOIN_1";
    private static final String TABLE_JOIN_2 = "TABLE_JOIN_2";
    private static VirtualSchema virtualSchemaSnowflakePreserveOriginalCase;
    private static String QUALIFIED_TABLE_JOIN_NAME_1;
    private static String QUALIFIED_TABLE_JOIN_NAME_2;
    private static Statement statementExasol;

    @BeforeAll
    static void beforeAll() throws SQLException {
        final Statement statementSnowflake = SETUP.getSnowflakeStatement();

        statementSnowflake.execute("CREATE DATABASE " + SETUP.getDatabaseName());
        statementSnowflake.execute("CREATE SCHEMA " + SCHEMA_SNOWFLAKE);
        statementSnowflake.execute("CREATE SCHEMA " + SCHEMA_SNOWFLAKE_UPPERCASE_TABLE);

        createSnowflakeTestTableSimple(statementSnowflake);
        createSnowflakeTestTableAllDataTypes(statementSnowflake);
        createTestTablesForJoinTests(SCHEMA_SNOWFLAKE);
        statementExasol = SETUP.getExasolStatement();
        virtualSchemaSnowflake = SETUP.createVirtualSchema(SCHEMA_SNOWFLAKE, Map.of());

        QUALIFIED_TABLE_JOIN_NAME_1 = virtualSchemaSnowflake.getName() + "." + TABLE_JOIN_1;
        QUALIFIED_TABLE_JOIN_NAME_2 = virtualSchemaSnowflake.getName() + "." + TABLE_JOIN_2;
        exasolSchema = SETUP.getExasolFactory().createSchema("EXASOL_TEST_SCHEMA");
    }

    @AfterAll
    static void afterAll() throws SQLException {
        final Statement statementSnowflake = SETUP.getSnowflakeStatement();
            statementSnowflake.execute("DROP DATABASE " + SETUP.getDatabaseName() + " CASCADE;");
    }

    private static void createSnowflakeTestTableSimple(final Statement statementSnowflake) throws SQLException {
        final String qualifiedTableName = SCHEMA_SNOWFLAKE + "." + TABLE_SNOWFLAKE_SIMPLE;
        statementSnowflake.execute("CREATE TABLE " + qualifiedTableName + " (x NUMBER(36,0))");
        statementSnowflake.execute("INSERT INTO " + qualifiedTableName + " VALUES (1)");
    }

    // https://docs.snowflake.com/en/sql-reference/intro-summary-data-types
    private static void createSnowflakeTestTableAllDataTypes(final Statement statementSnowflake) throws SQLException {
        final String qualifiedTableName = SCHEMA_SNOWFLAKE + "." + TABLE_SNOWFLAKE_ALL_DATA_TYPES;
        final String createAllDatatypesTableStatement = "CREATE TABLE " + qualifiedTableName //
                + " (" //
                + "myBigint BIGINT,	" //
                + "myBoolean BOOLEAN, " //
                + "myCharacter CHARACTER(1000), " // ALIAS FOR NVARCHAR IN SNOWFLAKE, SAME THING, NO PADDING
                + "myCharacterVar CHARACTER, " //
                + "myDate DATE, " //
                + "myDouble DOUBLE PRECISION, " //
                + "myInteger NUMBER(36,0), " // INT(EGER) has (38,0) precision and scale in snowflake, Exasol has (36,0) as a max. The integer datatype causes problems with the EXALOADER.
                + "myNumeric NUMERIC(36, 10), " // same as NUMBER IN snowflake
                + "myReal REAL, " //
                + "mySmallint SMALLINT, " //
                + "myText TEXT, " //
                + "myTime TIME, " //
                // TIME WITH TIME ZONE DOES NOT EXIST IN SNOWFLAKE
                + "myTimestamp TIMESTAMP, " //
                + "myTimestampWithTimeZone TIMESTAMP_TZ " // TIMESTAMP WITH TIME ZONE
                + ")";
        statementSnowflake.execute(createAllDatatypesTableStatement);
        final String fillAllDatabaseTypesStatement = ("INSERT INTO " + qualifiedTableName + " VALUES (" //
                + "10000000000, " // myBigint
                + "false, " // myBoolean
                + "'hajksdf', " // myCharacter
                + "'h', " // myCharacterVar                // + "'192.168.100.128/25'::cidr, " // myCidr
                + "'2010-01-01', " // myDate
                + "192189234.1723854, " // myDouble
                + "7189234, " // myInteger
                + "24.23, " // myNumeric
                + "10.12, " // myReal
                + "100, " // mySmallint
                + "'This cat is super cute', " // myText
                + "'11:11:11', " // myTime
                + "'2010-01-01 11:11:11', " // myTimestamp
                + "'2010-01-01 11:11:11 +01:00' " // myTimestampwithtimezone
                + ")");
        statementSnowflake.execute(fillAllDatabaseTypesStatement);
    }

    private static void createTestTablesForJoinTests(final String schemaName) throws SQLException {
        final Statement statement = SETUP.getSnowflakeStatement();
        statement.execute("CREATE TABLE " + schemaName + "." + TABLE_JOIN_1 + "(x NUMBER(36,0), y VARCHAR(100))");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_1 + " VALUES (1,'aaa')");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_1 + " VALUES (2,'bbb')");
        statement.execute("CREATE TABLE " + schemaName + "." + TABLE_JOIN_2 + "(x NUMBER(36,0), y VARCHAR(100))");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_2 + " VALUES (2,'bbb')");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_2 + " VALUES (3,'ccc')");
    }

    @Test
    void testSelectSingleColumn() throws SQLException {
        final ResultSet actualResultSet = statementExasol
                .executeQuery("SELECT * FROM " + virtualSchemaSnowflake.getName() + "." + TABLE_SNOWFLAKE_SIMPLE);
        assertThat(actualResultSet, table().row(1).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    @Test
    void testInnerJoin() throws SQLException {
        final String query = "SELECT * FROM " + QUALIFIED_TABLE_JOIN_NAME_1 + " a INNER JOIN  "
                + QUALIFIED_TABLE_JOIN_NAME_2 + " b ON a.x=b.x";
        final ResultSet expected = getExpectedResultSet(
                List.of("x  DECIMAL(36,0)", "y VARCHAR(100)", "a DECIMAL(36,0)", "b VARCHAR(100)"), //
                List.of("2,'bbb', 2,'bbb'"));
        final ResultSet actualResultSet = getActualResultSet(query);
        assertThat(actualResultSet, matchesResultSet(expected));
    }

    @Test
    void testInnerJoinWithProjection() throws SQLException {
        final String query = "SELECT b.y || " + QUALIFIED_TABLE_JOIN_NAME_1 + ".y FROM " + QUALIFIED_TABLE_JOIN_NAME_1
                + " INNER JOIN  " + QUALIFIED_TABLE_JOIN_NAME_2 + " b ON " + QUALIFIED_TABLE_JOIN_NAME_1 + ".x=b.x";
        final ResultSet expected = getExpectedResultSet(List.of("y VARCHAR(100)"), //
                List.of("'bbbbbb'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testLeftJoin() throws SQLException {
        final String query = "SELECT * FROM " + QUALIFIED_TABLE_JOIN_NAME_1 + " a LEFT OUTER JOIN  "
                + QUALIFIED_TABLE_JOIN_NAME_2 + " b ON a.x=b.x ORDER BY a.x";
        final ResultSet expected = getExpectedResultSet(
                List.of("x DECIMAL(36,0)", "y VARCHAR(100)", "a DECIMAL(36,0)", "b VARCHAR(100)"), //
                List.of("1, 'aaa', null, null", //
                        "2, 'bbb', 2, 'bbb'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testRightJoin() throws SQLException {
        final String query = "SELECT * FROM " + QUALIFIED_TABLE_JOIN_NAME_1 + " a RIGHT OUTER JOIN  "
                + QUALIFIED_TABLE_JOIN_NAME_2 + " b ON a.x=b.x ORDER BY a.x";
        final ResultSet expected = getExpectedResultSet(
                List.of("x DECIMAL(36,0)", "y VARCHAR(100)", "a DECIMAL(36,0)", "b VARCHAR(100)"), //
                List.of("2, 'bbb', 2, 'bbb'", //
                        "null, null, 3, 'ccc'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testFullOuterJoin() throws SQLException {
        final String query = "SELECT * FROM " + QUALIFIED_TABLE_JOIN_NAME_1 + " a FULL OUTER JOIN  "
                + QUALIFIED_TABLE_JOIN_NAME_2 + " b ON a.x=b.x ORDER BY a.x";
        final ResultSet expected = getExpectedResultSet(
                List.of("x DECIMAL(36,0)", "y VARCHAR(100)", "a DECIMAL(36,0)", "b VARCHAR(100)"), //
                List.of("1, 'aaa', null, null", //
                        "2, 'bbb', 2, 'bbb'", //
                        "null, null, 3, 'ccc'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testRightJoinWithComplexCondition() throws SQLException {
        final String query = "SELECT * FROM " + QUALIFIED_TABLE_JOIN_NAME_1 + " a RIGHT OUTER JOIN  "
                + QUALIFIED_TABLE_JOIN_NAME_2 + " b ON a.x||a.y=b.x||b.y ORDER BY a.x";
        final ResultSet expected = getExpectedResultSet(
                List.of("x DECIMAL(36,0)", "y VARCHAR(100)", "a DECIMAL(36,0)", "b VARCHAR(100)"), //
                List.of("2, 'bbb', 2, 'bbb'", //
                        "null, null, 3, 'ccc'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testFullOuterJoinWithComplexCondition() throws SQLException {
        final String query = "SELECT * FROM " + QUALIFIED_TABLE_JOIN_NAME_1 + " a FULL OUTER JOIN  "
                + QUALIFIED_TABLE_JOIN_NAME_2 + " b ON a.x-b.x=0 ORDER BY a.x";
        final ResultSet expected = getExpectedResultSet(
                List.of("x DECIMAL(36,0)", "y VARCHAR(100)", "a DECIMAL(36,0)", "b VARCHAR(100)"), //
                List.of("1, 'aaa', null, null", //
                        "2, 'bbb', 2, 'bbb'", //
                        "null, null, 3, 'ccc'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testYearScalarFunctionFromTimeStamp() throws SQLException {
        final String query = "SELECT year(\"MYTIMESTAMP\") FROM " + virtualSchemaSnowflake.getName() + "."
                + TABLE_SNOWFLAKE_ALL_DATA_TYPES;
        final ResultSet actualResultSet = getActualResultSet(query);
        final Short yearShort = 2010;
        assertThat(actualResultSet, table().row(yearShort).matches());
    }

    @Test
    void testYearScalarFunctionFromDate() throws SQLException {
        final String query = "SELECT year(\"MYDATE\") FROM " + virtualSchemaSnowflake.getName() + "."
                + TABLE_SNOWFLAKE_ALL_DATA_TYPES;
        final ResultSet actualResultSet = getActualResultSet(query);
        final Short yearShort = 2010;
        assertThat(actualResultSet, table().row(yearShort).matches());
    }

    // Check 'current_schema' functionality, re-enable tests after resolution
    // currently a bug in the compiler, compiler always expects 'VARCHAR(1) ASCII' see
    // https://github.com/exasol/snowflake-virtual-schema/issues/79
    // https://exasol.atlassian.net/browse/SPOT-19716
    @Disabled("Currently a bug in the compiler, compiler always expects 'VARCHAR(1) ASCII'")
    @Test
    void testCurrentSchemaScalarFunction() throws SQLException {
        final String query = " SELECT current_schema FROM " + virtualSchemaSnowflake.getName() + "."
                + TABLE_SNOWFLAKE_ALL_DATA_TYPES;
        final ResultSet actualResultSet = getActualResultSet(query);
        assertThat(actualResultSet, table().row(TABLE_SNOWFLAKE_ALL_DATA_TYPES).matches());
    }

    @Test
    void testFloatDivFunction() throws SQLException {
        final String query = " SELECT MYINTEGER / MYINTEGER FROM " + virtualSchemaSnowflake.getName() + "."
                + TABLE_SNOWFLAKE_ALL_DATA_TYPES;
        final ResultSet actualResultSet = getActualResultSet(query);
        assertThat(actualResultSet, table("DOUBLE PRECISION").row(1.0).matches());
    }

    @Test
    void testCountAll() throws SQLException {
        final String qualifiedExpectedTableName = virtualSchemaSnowflake.getName() + "." + TABLE_SNOWFLAKE_SIMPLE;
        final String query = "SELECT COUNT(*) FROM " + qualifiedExpectedTableName;
        final ResultSet actualResultSet = getActualResultSet(query);
        assertThat(actualResultSet, table("BIGINT").row(1L).matches());
    }

    @Test
    void testDatatypeBigint() throws SQLException {
        assertSingleValue("myBigint", "VARCHAR(2000000) UTF8", "Number precision not supported");
    }

    @Test
    void testDatatypeBoolean() throws SQLException {
        assertSingleValue("myBoolean", "BOOLEAN", false);
    }

    @Test
    void testDatatypeCharacter() throws SQLException {
        final String expected = "hajksdf";
        assertSingleValue("myCharacter", "VARCHAR(1000) UTF8", expected);
    }

    @Test
    void testDatatypeCharacterVar() throws SQLException {
        assertSingleValue("myCharactervar", "VARCHAR(1000) UTF8", "h");
    }

    @Test
    void testDatatypeDate() throws SQLException, ParseException {
        final Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2010-01-01");
        assertSingleValue("myDate", "DATE", expectedDate);
    }

    @Test
    void testDatatypeDouble() throws SQLException {
        assertSingleValue("myDouble", "DOUBLE", "192189234.1723854");
    }

    @Test
    void testDatatypeInteger() throws SQLException {
        assertSingleValue("myInteger", "DECIMAL(10,0)", "7189234");
    }

    @Test
    void testDatatypeNumeric() throws SQLException {
        assertSingleValue("myNumeric", "VARCHAR(2000000) UTF8", 24.2300000000);
    }

    @Test
    void testDatatypeReal() throws SQLException {
        assertSingleValue("myReal", "DOUBLE", 10.12);
    }

    @Test
    void testDatatypeSmallInt() throws SQLException {
        assertSingleValue("mySmallint", "VARCHAR(2000000) UTF8", "Number precision not supported");
    }

    @Test
    void testDatatypeText() throws SQLException {
        assertSingleValue("myText", "VARCHAR(2000000) UTF8", "This cat is super cute");
    }

    @Test
    void testDatatypeTime() throws SQLException {
        assertSingleValue("myTime", "VARCHAR(2000000) UTF8", "1970-01-01 11:11:11.0");
    }

    @Test
    void testDatatypeTimestamp() throws SQLException, ParseException {
        final Timestamp expectedDate = new Timestamp(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2010-01-01 11:11:11").getTime());
        assertSingleValue("myTimestamp", "TIMESTAMP", expectedDate);
    }

    @Test
    void testDatatypeTimestampWithTimezone() throws SQLException, ParseException {
        final Timestamp expectedDate = new Timestamp(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2010-01-01 11:11:11").getTime());
        assertSingleValue("myTimestampwithtimezone", "TIMESTAMP", expectedDate);
    }

    private void assertSingleValue(final String columnName, final String expectedColumnType, final Object expectedValue)
            throws SQLException {
        final String getActualValueQuery = "SELECT " + columnName + " FROM " + virtualSchemaSnowflake.getName() + "."
                + TABLE_SNOWFLAKE_ALL_DATA_TYPES;
        final ResultSet actualResultSet = statementExasol.executeQuery(getActualValueQuery);
        MatcherAssert.assertThat(actualResultSet, table().row(expectedValue).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    // TODO refactor to use table().row().matches()
    private ResultSet getExpectedResultSet(final List<String> expectedColumns, final List<String> expectedRows)
            throws SQLException {
        final String expectedValues = expectedRows.stream().map(row -> "(" + row + ")")
                .collect(Collectors.joining(","));
        final String qualifiedExpectedTableName = exasolSchema.getName() + ".EXPECTED";
        final String createTableStatement = "CREATE OR REPLACE TABLE " + qualifiedExpectedTableName + "("
                + String.join(", ", expectedColumns) + ");";
        statementExasol.execute(createTableStatement);
        final String insertIntoTableStatement = "INSERT INTO " + qualifiedExpectedTableName + " VALUES "
                + expectedValues + ";";
        statementExasol.execute(insertIntoTableStatement);
        final String selectStatement = "SELECT * FROM " + qualifiedExpectedTableName + ";";
        return statementExasol.executeQuery(selectStatement);
    }

    private ResultSet getActualResultSet(final String query) throws SQLException {
        return statementExasol.executeQuery(query);
    }

}
