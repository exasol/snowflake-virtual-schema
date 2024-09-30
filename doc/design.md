# Design for the Snowflake Virtual Schema adapter

## General design notes
The snowflake virtual schema's capabilities, mapping and query pushdown are mainly based on the existing PostgreSql virtual schema. 
This is because the PostgreSql database approaches Snowflake the closest, functionality wise.

## Notes on NUMERIC
The numeric datatype has a higher supported precision in Snowflake (38 vs 36 in Snowflake): 
If the precision is higher than 36 the column gets mapped to varchar and will display 'Precision not supported' as value.