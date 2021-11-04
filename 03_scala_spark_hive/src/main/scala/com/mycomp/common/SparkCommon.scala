package common

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

object SparkCommon {

    private val logger = LoggerFactory.getLogger(getClass.getName)

    def createSparkSession(): Option[SparkSession] = {

        try {

            logger.info("createSparkSession started ...")
            
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

            logger.info("createSparkSession ended ...")

            Some(spark)

        } catch {

            case e:Exception =>
                logger.error("An error has occured in the createSparkSession method" + e.printStackTrace())
                System.exit(1)
                None

        }



    }
}
