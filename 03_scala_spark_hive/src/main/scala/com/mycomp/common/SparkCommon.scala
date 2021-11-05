package common

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

object SparkCommon {

    private val logger = LoggerFactory.getLogger(getClass.getName)

    def createSparkSession(sparkLocal : Boolean): Option[SparkSession] = {

        try {

            logger.info("createSparkSession started ...")
            
            // Set Hadoop Home Directory
            System.setProperty("hadoop.home.dir","")
            //.config("spark.sql.warehouse.dir",warehouseLocation).enableHiveSupport()

            // Create Spark Session
            // if (sparkLocal) {
                logger.info("Local Session ...")
                val spark = SparkSession
                    .builder
                    .appName(name="HelloSpark")
                    .config("spark.master","local")
                    .enableHiveSupport()
                    .getOrCreate()

            // } else {
                // logger.info("Cluster Session ...")
                // val spark = SparkSession
                //     .builder
                //     .appName(name="HelloSpark")
                //     .master("spark://spark-master:7077")
                //     .config("spark.executor.memory", "512m")
                //     // .config("spark.cores.max", "4")
                //     // .config("spark.submit.deployMode","cluster")
                //     // .config("spark.driver.host", "theia")
                //     //.enableHiveSupport()
                //     .getOrCreate()
            // }

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
