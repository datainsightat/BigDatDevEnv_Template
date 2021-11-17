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

        df_agg = df.agg({"Salary":"avg","Age":"avg"}).collect()

        #df1 = df.na.drop()
        df1 = df.na.fill("Unknown", ["Country"])
        df2 = df1.na.fill(str(int(round(df_agg[0][1],0))), ["Age"])
        df3 = df2.na.fill(str(int(round(df_agg[0][0],0))), ["Salary"])

        logger.info("transform_data ended ...")

        return df3