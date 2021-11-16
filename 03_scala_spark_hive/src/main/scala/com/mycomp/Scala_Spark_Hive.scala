
// POM > https://mvnrepository.com/artifact/org.apache.spark/spark-core
// POM > https://mvnrepository.com/artifact/org.apache.spark/spark-sql
// POM > https://mvnrepository.com/artifact/org.apache.spark/spark-hive (<scope>compile</scope>)
// POM > https://mvnrepository.com/artifact/org.postgresql/postgresql
// POM > https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl
// POM > https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api

package com.mycomp

import common.{SparkCommon,PostgresCommon,SparkTransformer,JsonParser}
import org.apache.spark.sql.{DataFrame,SaveMode,SparkSession}
import org.slf4j.LoggerFactory

/**
 * @author ${user.name}
 */
object Scala_Spark_Hive {

    private val logger = LoggerFactory.getLogger(getClass.getName)
  
    def main(args: Array[String]): Unit = {

        try{

            logger.warn("main method started ...")

            //Create spark session
            val spark : SparkSession = SparkCommon.createSparkSession(true).get

            //Create hive table
            //SparkCommon.createHiveTable(spark)

            //Read hive table
            val courseDF = SparkCommon.readHiveTable(spark).get
            courseDF.show()

            //Replace Null Value
            val transformedDF1 = SparkTransformer.replaceNullValues(courseDF)
            transformedDF1.show()

            //val pgCourseTable = "newschema.newtable"
            val pgCourseTable = JsonParser.fetchPGTargetTable()

            logger.warn("**** pgCourseTable *** is ", pgCourseTable)

            // val pgCourseDataframe = PostgresCommon.fetchDataFrameFromPgTable(spark, pgCourseTable).get

            // logger.warn("main method ended ...")
            
            // pgCourseDataframe.show()

        } catch {

            case e:Exception =>
                logger.error("An error has occured in the main method" + e.printStackTrace())

        }

    }

}