package com.mycomp

import java.util.Properties
import org.apache.spark.sql.{SaveMode,SparkSession}

/**
 * @author ${user.name}
 */
object Scala_Spark_Hive {
  
  def main(args: Array[String]): Unit = {

        // POM > https://mvnrepository.com/artifact/org.apache.spark/spark-core
        // POM > https://mvnrepository.com/artifact/org.apache.spark/spark-sql
        // POM > https://mvnrepository.com/artifact/org.apache.spark/spark-hive (<scope>compile</scope>)
        // POM > https://mvnrepository.com/artifact/org.postgresql/postgresql


        // Set Hadoop Home Directory
        System.setProperty("hadoop.home.dir","")
        //.config("spark.sql.warehouse.dir",warehouseLocation).enableHiveSupport()

        // Create Spark Session
        val spark = SparkSession
            .builder
            .appName(name="HelloSpark")
            .config("spark.master","local")
            .enableHiveSupport()
            .getOrCreate()

        println("Created Spark Session ...")

        val sampleSeq = Seq((1,"spark"),(2,"big data"))

        val df = spark.createDataFrame(sampleSeq).toDF(colNames = "course id", "course name")
        df.show()
        //df.write.format(source="csv").mode(SaveMode.Overwrite).save(path="samplesq")

        // Connect to PostgreSQL
        // Checkout 'README_SETUP_POSTGRES.md' to setup the database properly
        println("Create Dataframe from Postgres DB ...")

        val pgConnectionProperties = new Properties()
        pgConnectionProperties.put("user","root")
        pgConnectionProperties.put("password","root")

        val pgTable = "newschema.course_catalog"

        val pgCrouseDataframe = spark.read.jdbc(url="jdbc:postgresql://pg_container:5432/newdb",pgTable,pgConnectionProperties)

        println("Fetched table ...")

        pgCrouseDataframe.show()

  }

}
