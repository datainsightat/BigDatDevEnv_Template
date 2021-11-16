package common

import java.util.Properties
import org.apache.spark.sql.{DataFrame,SparkSession}
import org.slf4j.LoggerFactory

object PostgresCommon {

    private val logger = LoggerFactory.getLogger(getClass.getName)

    def getPostgresCommonProps(): Properties = {

        logger.info("getPostgresCommonProps started ...")

        // Connect to PostgreSQL
        // Checkout 'README_SETUP_POSTGRES.md' to setup the database properly
        logger.info("Create Dataframe from Postgres DB ...")

        val pgConnectionProperties = new Properties()
        pgConnectionProperties.put("user","root")
        pgConnectionProperties.put("password","root")

        logger.info("getPostgresCommonProps ended ...")

        pgConnectionProperties

    }

    def getPostgresServerDatabase() : String = {
        val pgURL = "jdbc:postgresql://pg_container:5432/newdb"
        pgURL
    }

    def fetchDataFrameFromPgTable(spark : SparkSession, pgTable : String) : Option[DataFrame] = {

        try {

            logger.info("fetchDataFrameFromPgTable started ...")

            // This connection requires an active postgresql container named 'pg_container'
            val pgCourseDataframe = spark.read.jdbc(url=getPostgresServerDatabase(),pgTable,getPostgresCommonProps())

            logger.info("fetchDataFrameFromPgTable ended ...")

            Some(pgCourseDataframe)

        } catch {

            case e:Exception =>
                logger.error("An error has occured in the fetchDataFrameFromPgTable method" + e.printStackTrace())
                System.exit(1)
                None
        }

    }
}