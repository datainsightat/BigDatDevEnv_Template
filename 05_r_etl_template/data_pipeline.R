#install.packages("logger", repos = 'https://cloud.r-project.org')
#install.packages('dplyr', repos = 'https://cloud.r-project.org')
#install.packages("tidyr", repos = 'https://cloud.r-project.org')
#devtools::install_github("https://github.com/datainsightat/BigDatDevEnvR")

library(BigDatDevEnvR)
library(logger)
library(dplyr)
library(tidyr)

convert_dplyr <- function(spark, spark_dataframe) {

    hdfsDF_DF <- data.frame(hdfsDF)

    avg_age <- round(mean(hdfsDF_DF$Age,na.rm=TRUE),0)
    avg_salary <- round(mean(hdfsDF_DF$Salary,na.rm=TRUE),0)

    hdfsDF_DF_trans <- hdfsDF_DF %>% tidyr::replace_na(list(Age = avg_age, Salary = avg_salary, Country = "unknown"))

    print(hdfsDF_DF_trans)

    hdfsDF_trans <- sparklyr::sdf_copy_to(spark, hdfsDF_DF_trans)

    return(hdfsDF_trans)
}

convert_spark <- function(spark_dataframe) {

}

# Create Spark session
logger::log_info("Create Spark Session ...")
spark <- BigDatDevEnvR::create_spark_session("cluster")

# Read file from hdfs
logger::log_info("Read HDFS File ...")
hdfsDF <- BigDatDevEnvR::read_hdfs(spark,"examples/bank_prospects.csv")

print(hdfsDF)

# Do some transformation on the dataframe
logger::log_info("Transform DataFrame ...")

hdfsDF_trans <- convert_dplyr(spark, hdfsDF)

# Write dataframe to postgres db
logger::log_info("Write Spark Dataframe to Postgres DB ...")
BigDatDevEnvR::write_postgres(hdfsDF_trans,"newschema.bank_prospects", mode="append")

# Read updated table from postgres db

logger::log_info("Read Spark Dataframe from Postgres DB ...")
DF_check <- BigDatDevEnvR::read_postgres(spark,"newschema.bank_prospects")

print(DF_check)