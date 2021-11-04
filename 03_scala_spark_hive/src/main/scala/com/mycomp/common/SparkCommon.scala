package common

import org.apache.spark.sql.SparkSession

object SparkCommon {
  def createSparkSession(): SparkSession = {

        println("createSparkSession started ...")
        
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

        println("createSparkSession ended ...")

        spark
        //println("Created Spark Session ...")

        //val sampleSeq = Seq((1,"spark"),(2,"big data"))

        //val df = spark.createDataFrame(sampleSeq).toDF(colNames = "course id", "course name")
        //df.show()
        //df.write.format(source="csv").mode(SaveMode.Overwrite).save(path="samplesq")
  }
}
