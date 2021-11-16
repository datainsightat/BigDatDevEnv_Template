package common

import java.util.Properties
import org.apache.spark.sql.{DataFrame,SaveMode,SparkSession}
import org.slf4j.LoggerFactory

object PostgresCommon {

    private val logger = LoggerFactory.getLogger(getClass.getName)

    def getPostgresCommonProps(): Properties = {

        logger.warn("getPostgresCommonProps started ...")

        // Connect to PostgreSQL
        // Checkout 'README_SETUP_POSTGRES.md' to setup the database properly
        logger.warn("Create Dataframe from Postgres DB ...")

        val pgConnectionProperties = new Properties()
        pgConnectionProperties.put("user","root")
        pgConnectionProperties.put("password","root")

        logger.warn("getPostgresCommonProps ended ...")

        pgConnectionProperties

    }

    def getPostgresServerDatabase() : String = {
        val pgURL = "jdbc:postgresql://pg_container:5432/newdb"
        pgURL
    }

    def fetchDataFrameFromPgTable(spark : SparkSession, pgTable : String) : Option[DataFrame] = {

        try {

            logger.warn("fetchDataFrameFromPgTable started ...")

            val pgProp = getPostgresCommonProps
            val pgURLdetails  = getPostgresServerDatabase()

            logger.info("Creating Dataframe from Postgres")

            // This connection requires an active postgresql container named 'pg_container'
            val pgCourseDataframe = spark.read.jdbc(pgURLdetails,pgTable,pgProp)

            logger.warn("fetchDataFrameFromPgTable ended ...")

            Some(pgCourseDataframe)

        } catch {

            case e:Exception =>
                logger.error("An error has occured in the fetchDataFrameFromPgTable method" + e.printStackTrace())
                System.exit(1)
                None
        }

    }

    def writeDFToPostgresTable(dataFrame: DataFrame, pgTable : String): Unit = {
        try {
            logger.warn("writeDFToPostgresTable method started ....")

            dataFrame.write
                .mode(SaveMode.Append)
                .format("jdbc")
                .option("url",getPostgresServerDatabase())
                .option("dbtable",pgTable)
                .option("user","root")
                .option("password","root")
                .save()

            logger.warn("writeDFToPostgresTable method ended ....")
        } catch {
            case e: Exception =>
                logger.error("An error occured in writeDFToPostgreTabls " + e.printStackTrace())
        }
    }
}