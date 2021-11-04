package com.mycomp

import org.apache.spark.sql.{SaveMode,SparkSession}

/**
 * @author ${user.name}
 */
object Scala_Spark_Hive {
  
  def main(args: Array[String]): Unit = {

        // POM > https://mvnrepository.com/artifact/org.apache.spark/spark-core
        // POM > https://mvnrepository.com/artifact/org.apache.spark/spark-sql
        // POM > https://mvnrepository.com/artifact/org.apache.spark/spark-hive (<scope>compile</scope>)

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
        df.write.format(source="csv").mode(SaveMode.Overwrite).save(path="samplesq")
  }

}
