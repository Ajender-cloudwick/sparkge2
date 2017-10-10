import java.util.Properties

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{SaveMode, SparkSession}


/**
  * Created by AjenderNeelam on 9/28/17.
  */
object dbaccess {

//print("hello from dbaccess")
  val spark = SparkSession
    .builder()
    .master("spark://Ajenders-MacBook-Pro.local:7077")
    .appName("Spark SQL basic example")
    .getOrCreate()
  // $example on:jdbc_dataset$
  // Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
  // Loading data from a JDBC source
  //Way1
  var conf = ConfigFactory.load("/Users/AjenderNeelam/IdeaProjects/sparkge2/src/main/scala/resources/application.properties");
  val jdbcDF = spark.read
    .format("jdbc")
    .option("url", conf.getString("db.url"))
    .option("dbtable", "test2")
    .option("user", "ajender")
    .option("password", "ajender")
    .option("driver","org.postgresql.Driver")

    .load()
  jdbcDF.show()
  jdbcDF.select("rank").show()

  //Way2 use connection properties to read from database
  val connectionProperties = new Properties()
  connectionProperties.put("user", "ajender")
  connectionProperties.put("password", "ajender")
  connectionProperties.put("driver","org.postgresql.Driver")
  val jdbcDF2 = spark.read
    .jdbc("jdbc:postgresql://localhost/test", "test1", connectionProperties)
  jdbcDF2.show()


  // Saving data to a JDBC source creates a table
  jdbcDF.write
    .format("jdbc")
    .option("url", "jdbc:postgresql://localhost/test")
    .option("dbtable", "test5")
    .option("user", "ajender")
    .option("password", "ajender")
    .save()

  jdbcDF2.write.mode(SaveMode.Append)
    .jdbc("jdbc:postgresql:test", "test6", connectionProperties)

  // Specifying create table column data types on write
  jdbcDF.write
    .option("createTableColumnTypes", "name CHAR(64), id VARCHAR(1024),rank int ,rolno int")
    .jdbc("jdbc:postgresql://localhost/test", "test7", connectionProperties)
  // $example off:jdbc_dataset$

}
