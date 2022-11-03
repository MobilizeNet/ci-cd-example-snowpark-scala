package transformer

import com.snowflake.snowpark._
import com.snowflake.snowpark.functions._

object TransformData {

def loadResults(session: Session, df: DataFrame, tableName: String)
:Unit = {
    df.write.mode(SaveMode.Overwrite).saveAsTable(tableName)
}

def prefixColumns(df: DataFrame, prefix: String): DataFrame = {
    return df.select(df.schema.map(c=> df(c.name).as(prefix+c.name)))
}

def applyGroupBy(df: DataFrame, gbcolumname: String, idcolumnname: String, columnalias: String): DataFrame = {
    return df.groupBy(col(gbcolumname)).agg(count(col(idcolumnname)).alias(columnalias))
}
def main(args: Array[String]): Unit = {
    val database = '"' + sys.env("SNOWSQL_DATABASE") + '"'
    val configMap = Map (
        "URL" -> s"https://${sys.env("SNOWSQL_ACCOUNT")}.snowflakecomputing.com:443",
        "USER" -> sys.env("SNOWSQL_USER"),
        "PASSWORD" -> sys.env("SNOWSQL_PWD"),
        "ROLE" -> sys.env("SNOWSQL_ROLE"),
        "WAREHOUSE" -> sys.env("SNOWSQL_WAREHOUSE"),
        "DB" -> database,
        "SCHEMA" -> sys.env.getOrElse("SNOWSQL_SCHEMA", "")
    )
    val session = Session.builder.configs(configMap).create
    val customers = session.table("CUSTOMER")
    //rename columns
    val renamed=prefixColumns(customers,"raw_")
    // Apply Group By
    val transformed=applyGroupBy(renamed,"COUNTRY","ID","NUMBER_OF_CUSTOMERS")
    loadResults(session, transformed, "CUSTOMERS_BY_COUNTRY")
}
}