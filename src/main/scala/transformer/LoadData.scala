package transformer

import com.snowflake.snowpark._
import com.snowflake.snowpark.functions._
import com.snowflake.snowpark.types._

object LoadData {
    def main(args: Array[String]) : Unit = {
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

        val createStage = session.sql("CREATE or replace STAGE customer_data URL='s3://snowflake-workshop-lab/vhol_customer_order/customer'").collect()
        val dfCustSchema = StructType(
            Seq(
                StructField("ID", StringType),
                StructField("NAME", StringType),
                StructField("ADDRESS", StringType),
                StructField("PHONE", StringType),
                StructField("MKTSEGMENT", StringType),
                StructField("COUNTRY", StringType)
            )
        )

        // Create a reader
        var dfReader = session.read.schema(dfCustSchema)

        // Get the data into the data frame
        val dfCustRd = dfReader.csv("@customer_data/customer.csv")

        val ret = dfCustRd.write.mode("overwrite").saveAsTable("CUSTOMER")
    }
}