package com.mycomp

import org.apache.spark.sql.SparkSession

// /**
//  * @author ${user.name}
//  */
// object App {
  
//   def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
//   def main(args : Array[String]) {
//     println( "Hello World!" )
//     println("concat arguments = " + foo(args))
//   }

// }

object SparkScalaDemo {
    def main(args: Array[String]): Unit = {
        println("Hello Spark Scala")

        // Dependencies > pom.xml:
        //   https://mvnrepository.com/artifact/org.apache.spark/spark-core
        //   https://mvnrepository.com/artifact/org.apache.spark/spark-sql

        // Create Spark Session
        val spark = SparkSession
            .builder
            .appName(name="HelloSpark")
            .config("spark.master","local")
            .getOrCreate()

        println("Created Spark Session ...")

        val sampleSeq = Seq((1,"spark"),(2,"big data"))

        val df = spark.createDataFrame(sampleSeq).toDF(colNames = "course id", "course name")
        df.show()        

    }
}
