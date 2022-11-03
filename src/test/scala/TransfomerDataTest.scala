import com.snowflake.snowpark._
import com.snowflake.snowpark.functions._
import com.snowflake.snowpark.types._
import transformer._
import org.scalatest._
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class TransfomerDataTest extends FunSuite with BeforeAndAfter {
    var session: Session = null
    var logger: Logger = null

    test("TestApplyGroupBy") {
        val dfSchema = StructType(
            Seq(
                StructField("COL1", IntegerType),
                StructField("COL2", StringType)
            )
        )
        val dfSeq = session.createDataFrame(Seq((1, "USA"), (2, "GERMANY"), (3, "USA"), (2, "JAPAN"))).toDF("COL1", "COL2")
        val dfResult=TransformData.applyGroupBy(dfSeq, "COL1", "COL2","count" )
        assert(dfResult.count() === 3)
    }

    before {

        logger = LoggerFactory.getLogger(getClass.getSimpleName)
        logger.info(s"Connecting to account: ${sys.env("SNOWSQL_ACCOUNT")} role: ${sys.env("SNOWSQL_ROLE")} db: ${sys.env("SNOWSQL_DATABASE")}")
        val configs = Map (
            "URL" -> s"https://${sys.env("SNOWSQL_ACCOUNT")}.snowflakecomputing.com:443",
            "USER" -> sys.env("SNOWSQL_USER"),
            "PASSWORD" -> sys.env("SNOWSQL_PWD"),
            "ROLE" -> sys.env("SNOWSQL_ROLE"),
            "WAREHOUSE" -> sys.env("SNOWSQL_WAREHOUSE"),
            "DB" -> "\"" + sys.env("SNOWSQL_DATABASE") + "\"",
            "SCHEMA" -> sys.env.getOrElse("SNOWSQL_SCHEMA", "")
        )
        session = Session.builder.configs(configs).create
            
    }

    after {
        
    }
}
