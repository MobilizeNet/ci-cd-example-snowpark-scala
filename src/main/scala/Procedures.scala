package Procedures
import com.snowflake.snowpark._
import com.snowflake.snowpark.functions._
import com.snowflake.snowpark.types.StructType
import com.snowflake.snowpark.types.StructField
import com.snowflake.snowpark.types.IntegerType
import com.snowflake.snowpark.types.StringType
object Example {
    /* You can create a Snowflake stored procedure from the 'createDummyTable' function. You only need to follow these steps:
        1. Import the build (this can be done from the Metals extension tab on the left activity bar). Wait for this import to finish.
        2. Right-click on the build.sbt file in the root of this project.
        3. Choose the 'Black Diamond: Export Scala UDF' option.
        4. Fill the template file 'UDF-Export-Snowflake-Scala.sql' (on the 'Target' folder) with the requested information
            (you can ignore or delete the section 3.2, since that part is used to export a User Defined Function and section 3.1
            is used to export a Procedure, which is what we are doing in this case).
        5. Execute the SQL commands in the template file.
        6. Your stored procedure should be ready. You can call this stored procedure as you would normally do in Snowflake.
        7. Take into consideration that there are some limitations when creating Snowpark procedures or UDFs in Scala.
            You can learn more about this here: https://docs.snowflake.com/en/developer-guide/snowpark/scala/creating-udfs.html
    */
    def createDummyTable(sess: Session): String = {
        val idColumn: StructField = StructField("id", IntegerType)
        val valueColumn: StructField = StructField("value", StringType)
        val structure = StructType(Array(idColumn, valueColumn))
        sess.createDataFrame(Seq(Row(1, "one"), Row(2, "two")), structure).write.saveAsTable("exampleDummyTable")
        "Procedure Executed Successfully"
    }
}
