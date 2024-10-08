package com.exasol.adapter.dialects.snowflake;

import java.util.*;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.adapternotes.ColumnAdapterNotes;
import com.exasol.adapter.adapternotes.ColumnAdapterNotesJsonConverter;
import com.exasol.adapter.dialects.SqlDialect;
import com.exasol.adapter.dialects.rewriting.SqlGenerationContext;
import com.exasol.adapter.dialects.rewriting.SqlGenerationVisitor;
import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.sql.*;

/**
 * This class generates SQL queries for the {@link SnowflakeSqlDialect}.
 */
public class SnowflakeSqlGenerationVisitor extends SqlGenerationVisitor {
    private static final List<String> TYPE_NAMES_NOT_SUPPORTED = List.of("bytea");

    /**
     * Create a new instance of the {@link com.exasol.adapter.dialects.snowflake.SnowflakeSqlGenerationVisitor}.
     *
     * @param dialect {@link SnowflakeSqlDialect} SQL dialect
     * @param context SQL generation context
     */
    public SnowflakeSqlGenerationVisitor(final SqlDialect dialect, final SqlGenerationContext context) {
        super(dialect, context);
    }

    @Override
    protected String representAnyColumnInSelectList() {
        return SqlConstants.ONE;
    }

    @Override
    public String visit(final SqlColumn column) throws AdapterException {
        final String projectionString = super.visit(column);
        return getColumnProjectionString(column, projectionString);
    }

    private String getColumnProjectionString(final SqlColumn column, final String projectionString)
            throws AdapterException {

        if (super.isDirectlyInSelectList(column)) { //
            final ColumnAdapterNotesJsonConverter converter = ColumnAdapterNotesJsonConverter.getInstance();
            ColumnMetadata metaData = column.getMetadata();
            DataType mappedType = metaData.getType();
            ColumnAdapterNotes columnAdapterNotes = converter.convertFromJsonToColumnAdapterNotes(metaData.getAdapterNotes(), column.getName());
            String sourceTypeName = columnAdapterNotes.getTypeName();

            return buildColumnProjectionString(sourceTypeName, mappedType, projectionString);
        } else {
            return projectionString;
        }
    }

    @Override
    public String visit(final SqlFunctionScalar function) throws AdapterException {
        final List<SqlNode> arguments = function.getArguments();
        final List<String> argumentsSql = new ArrayList<>(arguments.size());
        for (final SqlNode node : arguments) {
            argumentsSql.add(node.accept(this));
        }
        final ScalarFunction scalarFunction = function.getFunction();
        switch (scalarFunction) {
            case ADD_DAYS:
                return getAddDateTime(argumentsSql, "days");
            case ADD_HOURS:
                return getAddDateTime(argumentsSql, "hours");
            case ADD_MINUTES:
                return getAddDateTime(argumentsSql, "mins");
            case ADD_SECONDS:
                return getAddDateTime(argumentsSql, "secs");
            case ADD_WEEKS:
                return getAddDateTime(argumentsSql, "weeks");
            case ADD_YEARS:
                return getAddDateTime(argumentsSql, "years");
            case ADD_MONTHS:
                return getAddDateTime(argumentsSql, "months");
            case SECOND:
            case MINUTE:
            case DAY:
            case WEEK:
            case MONTH:
            case YEAR:
                return getDateTime(argumentsSql, scalarFunction);
            case POSIX_TIME:
                return getPosixTime(argumentsSql);
            case FLOAT_DIV:
                return getCastToDoublePrecisionAndDivide(argumentsSql);
            default:
                return super.visit(function);
        }
    }

    private String getCastToDoublePrecisionAndDivide(final List<String> sqlArguments) {
        return "( CAST (" + sqlArguments.get(0) + " AS DOUBLE PRECISION) / CAST (" + sqlArguments.get(1)
                + " AS DOUBLE PRECISION))";
    }

    private String getAddDateTime(final List<String> argumentsSql, final String unit) {
        final StringBuilder builder = new StringBuilder();
        builder.append(argumentsSql.get(0));
        builder.append(" + ");
        builder.append(buildInterval(argumentsSql, unit));
        return builder.toString();
    }

    private String buildInterval(final List<String> argumentsSql, final String unit) {
        return "make_interval(" + unit + " => " + argumentsSql.get(1) + ")";
    }

    private String getDateTime(final List<String> argumentsSql, final ScalarFunction scalarFunction) {
        final StringBuilder builder = new StringBuilder();
        builder.append("CAST(DATE_PART(");
        appendDatePart(scalarFunction, builder);
        builder.append(",");
        builder.append(argumentsSql.get(0));
        builder.append(") AS DECIMAL(");
        appendDecimalSize(scalarFunction, builder);
        builder.append(",0))");
        return builder.toString();
    }

    private static void appendDatePart(ScalarFunction scalarFunction, StringBuilder builder) {
        switch (scalarFunction) {
            case SECOND:
                builder.append("'SECOND'");
                break;
            case MINUTE:
                builder.append("'MINUTE'");
                break;
            case DAY:
                builder.append("'DAY'");
                break;
            case WEEK:
                builder.append("'WEEK'");
                break;
            case MONTH:
                builder.append("'MONTH'");
                break;
            case YEAR:
                builder.append("'YEAR'");
                break;
            default:
                break;
        }
    }

    private static void appendDecimalSize(ScalarFunction scalarFunction, StringBuilder builder) {
        switch (scalarFunction) {
            case SECOND:
            case MINUTE:
            case DAY:
            case WEEK:
            case MONTH:
                builder.append("2");
                break;
            case YEAR:
                builder.append("4");
                break;
            default:
                break;
        }
    }

    private String getPosixTime(final List<String> argumentsSql) {
        return "EXTRACT(EPOCH FROM " + argumentsSql.get(0) + ")";
    }

    private String buildColumnProjectionString(final String typeName, DataType mappedType, final String projectionString) {
        if (typeName.startsWith("NUMBER") && mappedType.getExaDataType() == DataType.ExaDataType.VARCHAR) {
            return "'Number precision not supported'";
        } else if (typeName.startsWith("TIMESTAMPTZ")) {
            return "TO_TIMESTAMP_NTZ(" + projectionString + ")";
        } else if (checkIfNeedToCastToVarchar(typeName)) {
            return "CAST(" + projectionString + "  as VARCHAR )";
        } else if (TYPE_NAMES_NOT_SUPPORTED.contains(typeName)) {
            return "cast('" + typeName + " NOT SUPPORTED' as varchar) as not_supported";
        } else {
            return projectionString;
        }
    }

    private boolean checkIfNeedToCastToVarchar(final String typeName) {
        final List<String> typesToVarcharCast = Arrays.asList("point", "line", "varbit", "lseg", "box", "path",
                "polygon", "circle", "cidr", "citext", "inet", "macaddr", "interval", "json", "jsonb", "uuid",
                "tsquery", "tsvector", "xml");
        return typesToVarcharCast.contains(typeName);
    }

    @Override
    public String visit(final SqlFunctionAggregateGroupConcat function) throws AdapterException {
        final StringBuilder builder = new StringBuilder();
        builder.append("STRING_AGG");
        builder.append("(");
        final String expression = function.getArgument().accept(this);
        builder.append(expression);
        builder.append(", ");
        final String separator = function.hasSeparator() ? function.getSeparator().accept(this) : "','";
        builder.append(separator);
        builder.append(") ");
        return builder.toString();
    }
}