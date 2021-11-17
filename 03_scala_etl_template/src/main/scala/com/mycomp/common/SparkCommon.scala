package common

import org.apache.spark.sql.{DataFrame,SparkSession}
import org.slf4j.LoggerFactory

object SparkCommon {

    private val logger = LoggerFactory.getLogger(getClass.getName)

    def createSparkSession(inputConfig : InputConfig): Option[SparkSession] = {

        try {

            logger.warn("createSparkSession started ...")

            //logger.warn("Spark environment is ",inputConfig.env)
            
            if (inputConfig.env == "dev")  {
                
                // Set Hadoop Home Directory
                System.setProperty("hadoop.home.dir","")
                //.config("spark.sql.warehouse.dir",warehouseLocation).enableHiveSupport()

                logger.warn("Local Session ...")

                var spark = SparkSession
                    .builder
                    .appName(name="HelloSpark")
                    .config("spark.master","local")
                    .enableHiveSupport()
                    .getOrCreate()

                Some(spark)

            } else {

                logger.warn("Cluster Session ...")

                //System.setProperty("hadoop.home.dir","hdfs://hive:54310")
                //.config("spark.sql.warehouse.dir",warehouseLocation).enableHiveSupport()

                var spark = SparkSession
                    .builder()
                    .appName(name="HelloSpark")
                    .master("spark://spark:7077")
                    //.config("spark://spark-master:7077","local")
                    .config("spark.executor.memory", "512m")
                    .config("spark.cores.max", "1")
                    //.config("hive.metastore.warehouse.uris","hdfs://hive:54310")
                    .config("spark.sql.warehouse.dir","hdfs://hive:54310/user/hive/warehouse")
                    //.config("spark.dynamicAllocation.enabled","false")
                    //.config("spark.shuffle.service.enabled","false")
                    //.config("spark.submit.deployMode","cluster")
                    //.config("spark.driver.host", "theia")
                    .enableHiveSupport()
                    .getOrCreate()

                Some(spark)
            }

        } catch {

            case e:Exception =>
                logger.error("An error has occured in the createSparkSession method" + e.printStackTrace())
                System.exit(1)
                None

        }

    }

    def createHiveTable (spark : SparkSession) : Unit = {

        logger.warn("createHiveTable started ...")
        
        spark.sql("create database if not exists newdb")
        spark.sql("create table if not exists newdb.newtable(course_id string, course_name string, author_name string, no_of_reviews string)")
        
        spark.sql("insert into newdb.newtable values (1,'Java','FutureX',45)")
        spark.sql("insert into newdb.newtable VALUES (2,'Java','FutureXSkill',56)")
        spark.sql("insert into newdb.newtable VALUES (3,'Big Data','',100)")
        spark.sql("insert into newdb.newtable VALUES (4,'Linux','Future','')")
        spark.sql("insert into newdb.newtable VALUES (5,'Microservices','Future',100)")
        spark.sql("insert into newdb.newtable VALUES (6,'CMS','',100)")

        spark.sql("alter table newdb.newtable set tblproperties('serialization.null.format'='')")

        logger.warn("createHiveTable ended ...")
    }

    def readHiveTable(spark : SparkSession) : Option[DataFrame] = {
        try {
            logger.warn("readHiveTable started ...")

            val courseDF = spark.sql("select * from newdb.newtable")

            logger.warn("readHiveTable ended ...")

            Some(courseDF)
        } catch {
            case e: Exception =>
                logger.error("Error Reading newdb.newtable "+e.printStackTrace())
                None
        }
    }

    def writeToHiveTable(spark : SparkSession, df : DataFrame, hiveTable : String): Unit = {
        try {
            logger.warn("writeToHiveTable started ...")
            val tmpView = hiveTable+"tempView"
            df.createOrReplaceTempView(tmpView)
            val sqlQuery = "create table if not exists " + hiveTable + " as select * from " + tmpView
            spark.sql(sqlQuery)
            logger.warn("Finished writing to Hive Table")
        } catch {
            case e: Exception =>
                logger.error("Error writing to Hive Table " + e.printStackTrace())
        }
    }
 }