package common

import org.apache.spark.sql.{DataFrame,SparkSession}
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
                //     .builder()
                //     .appName(name="HelloSpark")
                //     .master("spark://spark:7077")
                //     //.config("spark://spark-master:7077","local")
                //     .config("spark.executor.memory", "512m")
                //     .config("spark.cores.max", "1")
                //     //.config("spark.sql.warehouse.dir","/user/hive/warehouse")
                //     .config("spark.dynamicAllocation.enabled","false")
                //     //.config("spark.shuffle.service.enabled","false")
                //     .config("spark.submit.deployMode","cluster")
                //     //.config("spark.driver.host", "theia")
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

    def createHiveTable (spark : SparkSession) : Unit = {

        logger.info("createHiveTable started ...")
        
        spark.sql("create database if not exists newdb")
        spark.sql("create table if not exists newdb.newtable(course_id string, course_name string, author_name string, no_of_reviews string)")
        
        spark.sql("insert into newdb.newtable values (1,'Java','FutureX',45)")
        spark.sql("insert into newdb.newtable VALUES (2,'Java','FutureXSkill',56)")
        spark.sql("insert into newdb.newtable VALUES (3,'Big Data','Future',100)")
        spark.sql("insert into newdb.newtable VALUES (4,'Linux','Future',100)")
        spark.sql("insert into newdb.newtable VALUES (5,'Microservices','Future',100)")
        spark.sql("insert into newdb.newtable VALUES (6,'CMS','',100)")

        spark.sql("alter table newdb.newtable set tblproperties('serialization.null.format'='')")

        logger.info("createHiveTable ended ...")
    }

    def readHiveTable(spark : SparkSession) : Option[DataFrame] = {
        try {
            logger.info("readHiveTable started ...")

            val courseDF = spark.sql("select * from newdb.newtable")

            logger.info("readHiveTable ended ...")

            Some(courseDF)
        } catch {
            case e: Exception =>
                logger.error("Error Reading newdb.newtable "+e.printStackTrace())
                None
        }
    }
 }