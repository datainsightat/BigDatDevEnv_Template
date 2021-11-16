
// POM > https://mvnrepository.com/artifact/org.apache.spark/spark-core
// POM > https://mvnrepository.com/artifact/org.apache.spark/spark-sql
// POM > https://mvnrepository.com/artifact/org.apache.spark/spark-hive (<scope>compile</scope>)
// POM > https://mvnrepository.com/artifact/org.postgresql/postgresql
// POM > https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl
// POM > https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api

package com.mycomp

import common.{InputConfig,SparkCommon,PostgresCommon,SparkTransformer,JsonParser}
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

            //Check, if arguments where passed
            val arg_length = args.length

            if (arg_length == 0) {
                logger.warn("No Argument passed")
                System.exit(1)
            }

            //Get arguments
            val inputConfig : InputConfig = InputConfig(env = args(0), targetDB = args(1))

            //System.out.println(inputConfig)

            //Create    spark session
            val spark = SparkCommon.createSparkSession(inputConfig).get

            //Create hive table
            //SparkCommon.createHiveTable(spark)

            //Read hive table
            val courseDF = SparkCommon.readHiveTable(spark).get
            courseDF.show()

            //Replace Null Value
            val transformedDF1 = SparkTransformer.replaceNullValues(courseDF)
            transformedDF1.show()

            if (inputConfig.targetDB == "pg") {
                logger.warn("Writing to PG table")

                //Writing to Postgres Table
                val pgCourseTable = JsonParser.fetchPGTargetTable()
                logger.warn(s"**** pgCourseTable **** is $pgCourseTable")
                PostgresCommon.writeDFToPostgresTable(transformedDF1,pgCourseTable)
            } else if (inputConfig.targetDB == "hive") {
                logger.info("Writing to Hive Table ...")

                //Writing to Hive Table
                SparkCommon.writeToHiveTable(spark,transformedDF1,"customer_transformed")
                logger.info("Finished writing to Hive Table ...")
            }            

            // val pgCourseDataframe = PostgresCommon.fetchDataFrameFromPgTable(spark, pgCourseTable).get

            // logger.warn("main method ended ...")
            
            // pgCourseDataframe.show()

        } catch {

            case e:Exception =>
                logger.error("An error has occured in the main method" + e.printStackTrace())

        }

    }

}