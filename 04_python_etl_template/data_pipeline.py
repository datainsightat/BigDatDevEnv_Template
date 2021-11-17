import pyspark
from pyspark.sql import SparkSession
from pyspark.sql.types import IntegerType

from pipeline import ingest,transform,persist
import logging
import logging.config

import sys

import configparser

class Pipeline:

    #logging.basicConfig(level="INFO") #DEBUG, INFO, WARN, ERROR
    logging.config.fileConfig("pipeline/resources/configs/logging.conf")

    def run_pipeline(self):

        try:

            logging.info("run_pipeline started ...")

            pipline_config = self.read_config()

            ingest_process = ingest.Ingest(self.spark)
            #df = ingest_process.read_from_csv()
            #df = ingest_process.read_from_pg(pipline_config)
            #df = ingest_process.read_from_pg_jdbc(pipline_config)
            df = ingest_process.read_from_hdfs(pipline_config)
            df.show()

            transform_process = transform.Transform(self.spark)
            transformed_df = transform_process.transform_data(df)
            transformed_df.show()
            
            persist_process = persist.Persist(self.spark)
            #persist_process.write_to_hdfs_local(transformed_df)
            #persist_process.write_to_pg(pipline_fonfig)
            persist_process.write_to_pg_jdbc(transformed_df,pipline_config)

            logging.info("run_pipeline ended ...")

        except Exception as exp:

            logging.info("Error while run_pipeline > " + str(exp))

            sys.exit(1)
        
        return

    def create_spark_session(self):

        logging.info("create_spark_session started ...")

        pipline_config = self.read_config()

        self.spark = SparkSession\
            .builder\
            .appName("Python ETL")\
            .master(pipline_config.get("DB_CONFIGS","SPARK_MASTER")) \
            .config("spark.executor.memory", "512m") \
            .config("spark.cores.max", "1") \
            .config("spark.driver.extraClassPath",pipline_config.get("DB_CONFIGS","JDBC_JAR"))\
            .enableHiveSupport()\
            .getOrCreate()

                    #.master(pipline_config.get("DB_CONFIGS","SPARK_MASTER")) \

        logging.info("create_spark_session ended ...")

    def create_hive_table(self):

        logging.info("create_hive_table started ...")

        self.spark.sql("create database if not exists newdb")
        self.spark.sql("create table if not exists newdb.newtable(course_id string, course_name string, author_name string, no_of_reviews string)")

        self.spark.sql("insert into newdb.newtable values (11,'Java','FutureX',45)")
        self.spark.sql("insert into newdb.newtable VALUES (12,'Java','FutureXSkill',56)")
        self.spark.sql("insert into newdb.newtable VALUES (13,'Big Data','',100)")
        self.spark.sql("insert into newdb.newtable VALUES (14,'Linux','Future','')")
        self.spark.sql("insert into newdb.newtable VALUES (15,'Microservices','Future',100)")
        self.spark.sql("insert into newdb.newtable VALUES (16,'CMS','',100)")

        #Treat empty strings as null
        self.spark.sql("alter table newdb.newtable set tblproperties('serialization.null.format'='')")

        logging.info("create_hive_table ended ...")

    def read_config(self):

        logging.info("read_config started ...")

        config = configparser.ConfigParser()
        config.read("pipeline/resources/pipeline.ini")

        logging.info("read_config ended ...")

        return config

if __name__ == '__main__':

    logging.info("Application started ...")

    pipeline = Pipeline()
    pipeline.create_spark_session()
    #pipeline.create_hive_table()
    pipeline.run_pipeline()

    logging.info("Application ended ...")