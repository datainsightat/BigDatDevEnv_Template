import pyspark
from pyspark.sql import SparkSession

import logging
import logging.config

import psycopg2

class Persist:

    logging.config.fileConfig("pipeline/resources/configs/logging.conf")

    def __init__(self,spark):
        self.spark = spark

    def write_to_hdfs_local(self,df):

        logger = logging.getLogger("Persist")

        try:

            logger.info("persist_data started ...")

            df.coalesce(1).write.mode("overwrite").option("header","true").csv("retailstore_transformed")

            logger.info("persist_data ended ...")

        except Exception as exp:

            logger.error("Error while persist_data > "+str(exp))

            raise Exception(exp)

    def write_to_pg(self,pipline_config):

        logger = logging.getLogger("Persist")

        logger.info("write_to_pg started ...")

        connection = psycopg2.connect(user="root",password="root",host="pg_container",port=5432,database="newdb")
        
        cursor = connection.cursor()

        sql_query = "insert into " + pipline_config.get("COURSES","PG_TABLE") + "(course_id,course_name,author_name,no_of_reviews) values (%s,%s,%s,%s)"
        insert_tuple = (13,"Machine Learning","new Author",5)

        cursor.execute(sql_query,insert_tuple)
        cursor.close()
        connection.commit()

        logger.info("write_to_pg ended ...")

        return 0

    def write_to_pg_jdbc(self,df,pipline_config):

        logger = logging.getLogger("Persist")

        try:

            logger.info("write_to_pg_jdbc started ...")

            df.coalesce(1).write \
                .mode("append") \
                .format("jdbc") \
                .option("url",pipline_config.get("DB_CONFIGS","PG_URL")) \
                .option("dbtable",pipline_config.get("BANK_PROSPECTS","PG_TABLE")) \
                .option("user","root") \
                .option("password","root") \
                .save()

            logger.info("write_to_pg_jdbc ended ...")

        except Exception as exp:

            logger.error("Error while write_to_pg_jdbc > "+str(exp))

            raise Exception(exp)

        return 0