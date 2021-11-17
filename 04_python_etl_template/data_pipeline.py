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

            pg_table = self.read_config_pg_table()

            ingest_process = ingest.Ingest(self.spark)
            #df = ingest_process.read_from_csv()
            #df = ingest_process.read_from_pg(pg_table)
            df = ingest_process.read_from_pg_jdbc(pg_table)
            df.show()

            transform_process = transform.Transform(self.spark)
            transformed_df = transform_process.transform_data(df)
            transformed_df.show()
            
            persist_process = persist.Persist(self.spark)
            #persist_process.write_to_hdfs_local(transformed_df)
            #persist_process.write_to_pg(pg_table)
            persist_process.write_to_pg_jdbc(transformed_df,pg_table)

            logging.info("run_pipeline ended ...")

        except Exception as exp:

            logging.info("Error while run_pipeline > " + str(exp))

            sys.exit(1)
        
        return

    def create_spark_session(self):

        logging.info("create_spark_session started ...")

        self.spark = SparkSession\
            .builder\
            .appName("Python ETL")\
            .config("spark.driver.extraClassPath","pipeline/postgresql-42.3.1.jar")\
            .enableHiveSupport()\
            .getOrCreate()

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

    def read_config_pg_table(self):

        logging.info("read_config_pg_table started ...")

        config = configparser.ConfigParser()
        config.read("pipeline/resources/pipeline.ini")
        target_table = config.get("DB_CONFIGS","TARGET_PG_TABLE")
        logging.info("pg table is " + target_table)

        logging.info("read_config_pg_table ended ...")

        return target_table

if __name__ == '__main__':

    logging.info("Application started ...")

    pipeline = Pipeline()
    pipeline.create_spark_session()
    #pipeline.create_hive_table()
    pipeline.run_pipeline()

    logging.info("Application ended ...")