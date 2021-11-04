package common

import java.util.Properties
import org.apache.spark.sql.{DataFrame,SparkSession}

object PostgresCommon {

    def getPostgresCommonProps(): Properties = {

        println("getPostgresCommonProps started ...")

        // Connect to PostgreSQL
        // Checkout 'README_SETUP_POSTGRES.md' to setup the database properly
        println("Create Dataframe from Postgres DB ...")

        val pgConnectionProperties = new Properties()
        pgConnectionProperties.put("user","root")
        pgConnectionProperties.put("password","root")

        println("getPostgresCommonProps ended ...")

        pgConnectionProperties

    }

    def getPostgresServerDatabase() : String = {
        val pgURL = "jdbc:postgresql://pg_container:5432/newdb"
        pgURL
    }

    def fetchDataFrameFromPgTable(spark : SparkSession, pgTable : String) : DataFrame = {

        println("fetchDataFrameFromPgTable started ...")

        // This connection requires an active postgresql container named 'pg_container'
        val pgCourseDataframe = spark.read.jdbc(url=getPostgresServerDatabase(),pgTable,getPostgresCommonProps())

        println("fetchDataFrameFromPgTable ended ...")

        pgCourseDataframe
    }
}
