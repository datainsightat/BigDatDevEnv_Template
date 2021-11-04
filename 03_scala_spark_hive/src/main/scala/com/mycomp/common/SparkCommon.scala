package common

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

object SparkCommon {

    private val logger = LoggerFactory.getLogger(getClass.getName)

    def createSparkSession(): SparkSession = {

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

        spark
        //println("Created Spark Session ...")

        //val sampleSeq = Seq((1,"spark"),(2,"big data"))

        //val df = spark.createDataFrame(sampleSeq).toDF(colNames = "course id", "course name")
        //df.show()
        //df.write.format(source="csv").mode(SaveMode.Overwrite).save(path="samplesq")
    }
}
