import pyspark
from pyspark.sql import SparkSession
from pyspark.sql.types import IntegerType

import logging
import logging.config

import psycopg2
import pandas
import pandas.io.sql as sqlio

import configparser

class Ingest:

    logging.config.fileConfig("pipeline/resources/configs/logging.conf")

    def __init__(self,spark):
        self.spark = spark

    def read_from_csv(self):

        logger = logging.getLogger("Ingest")

        logger.info("read_from_csv started ...")
        
        # Dummy Dataframe
        # my_list = [1,2,3]
        # df = self.spark.createDataFrame(my_list,IntegerType())
        # df.show()

        # Read local CSV
        #customer_df = self.spark.read.csv("retailstore.csv", header=True)
        course_df = self.spark.sql("select * from newdb.newtable")

        logger.info("read_from_csv ended ...")

        return course_df

    def read_from_pg(self,pipline_config):

        logger = logging.getLogger("Ingest")

        logger.info("read_from_pg started ...")

        connection = psycopg2.connect(user="root",password="root",host="pg_container",port=5432,database="newdb")
        
        sql_query = "select * from " + pipline_config.get("COURSES","PG_TABLE")
        pdDF = sqlio.read_sql_query(sql_query,connection)
        sparkDF = self.spark.createDataFrame(pdDF)

        logger.info("read_from_pg ended ...")

        return sparkDF

    def read_from_pg_jdbc(self,pipline_config):

        logger = logging.getLogger("Ingest")

        logger.info("read_from_pg_jdbc started ...")

        jdbcDF = self.spark.read \
            .format("jdbc") \
            .option("url","jdbc:postgresql://pg_container:5432/newdb") \
            .option("dbtable",pipline_config.get("COURSES","PG_TABLE")) \
            .option("user","root") \
            .option("password","root") \
            .load()

        logger.info("read_from_pg_jdbc ended ...")

        return jdbcDF

    def read_from_hdfs(self,pipline_config):

        logger = logging.getLogger("Ingest")

        logger.info("read_from_hdfs started ...")

        hdfsDF = self.spark.read.csv(pipline_config.get("DB_CONFIGS","HDFS_URL")+pipline_config.get("BANK_PROSPECTS","HDFS_PATH"),header=True)

        logger.info("read_from_hdfs ended ...")

        return hdfsDF