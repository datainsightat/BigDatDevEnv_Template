package com.mycomp

import common.{SparkCommon,PostgresCommon}

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
        // POM > https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl

        val spark : SparkSession = SparkCommon.createSparkSession()

        val pgTable = "newschema.course_catalog"

        val pgCourseDataframe = PostgresCommon.fetchDataFrameFromPgTable(spark, pgTable)
        
        pgCourseDataframe.show()

  }

}
