import com.snowflake.snowpark._
import com.snowflake.snowpark.functions._
object Main extends App {
  // If you just want to use Scala (without Snowpark) you can start right with the following line of code
  println("Hello, World!")

  // If you want to use Snowpark, you can use this code to build a connection and run a basic query
  // This session is configured to use the current active connection (which you can modify using the SQL Tools extension)
  val configs = Map (
    "URL" -> s"https://${sys.env("SNOWSQL_ACCOUNT")}.snowflakecomputing.com:443",
    "USER" -> sys.env("SNOWSQL_USER"),
    "PASSWORD" -> sys.env("SNOWSQL_PASSWORD"),
    "ROLE" -> sys.env("SNOWSQL_ROLE"),
    "WAREHOUSE" -> sys.env("SNOWSQL_WAREHOUSE"),
    "DB" -> sys.env("SNOWSQL_DATABASE"),
    "SCHEMA" -> sys.env.getOrElse("SNOWSQL_SCHEMA", "")
  )
  val session = Session.builder.configs(configs).create
  session.sql("SELECT 'Hello World!'").show()
}