import pyspark
from pyspark.sql import SparkSession

import logging
import logging.config

class Transform:

    logging.config.fileConfig("pipeline/resources/configs/logging.conf")

    def __init__(self,spark):
        self.spark = spark

    def transform_data(self,df):

        logger = logging.getLogger("Transform")

        logger.info("transform_data started ...")

        # Basic DF operations
        # customer_df.show()
        # customer_df.describe().show()
        # customer_df.select("Country").show()
        # customer_df.groupBy("Country").count().show()
        # customer_df.filter("Salary > 30000").show
        # customer_df.groupBy("gender").agg({"Salary":"avg","Age":"max"}).show()
        # customer_df.orderBy("Salary").show()

        #df1 = df.na.drop()
        df1 = df.na.fill("Unknown", ["author_name"])
        df2 = df1.na.fill("0", ["no_of_reviews"])

        logger.info("transform_data ended ...")

        return df2