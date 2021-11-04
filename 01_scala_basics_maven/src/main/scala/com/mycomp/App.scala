package com.mycomp

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

object App {

    def main(args: Array[String]): Unit = {
        println("Learning Scala Basics")
        demoVarVal()
        variableDeclaration()
        val sMain = demoLoops(str = "Big Data")
        println(sMain)
        ifElse(int = 4)
        forLoop()
    }

    def demoVarVal(): Unit = {
        var a = 3
        val b = 5
        a = 8
        println("a " + a)
        println("b " + b)
    }

    def variableDeclaration(): Unit = {
        val a = 3
        val b : Int = 5
        val c : Double = 4.5
        val d : Double = 5

        var myArrayInt : Array[Int] = Array(10,20,30,40)
        println("myArrayInt index 0 " + myArrayInt(0))
        println("myArrayInt index 1 " + myArrayInt(1))

        var myArrayStr : Array[String] = Array("a","b","c","d")
        println("myArrayStr index 0 " + myArrayStr(0))
        println("myArraystr index 1 " + myArrayStr(1))

        val myList = List(10,20,"Big Data",40)
        println("myList 1 " + myList(0))
        println("myList 2 " + myList(1))
        println("myList 3 " + myList(2))
        println(myList.size)

        val myTuple = (1,2,3,4)
        println("myTuple 1 " + myTuple._1)
        println("myTuple 2 " + myTuple._2)
        println("myTuple 3 " + myTuple._3)

        val mapValue = Map(("key1",10),("key2",20))
        println("mapValue Key 2 is " + mapValue("key2"))
    }

    def demoLoops(str : String): String = {
        val sFunction = str + " Loop demo done"
        sFunction
    }

    def ifElse(int : Int): Unit = {
        if (int <= 3) {
            println("inside if")
        } else if (int <= 5) {
            println("inside else if")
        } else {
            println("instide else")
        }
    }

    def forLoop(): Unit = {
        
        for(i <- 1 to 10) {
            println(i)
        }
        println("For Loop done")

        for(i <- 1 until 5) {
            println(i)
        }
        println("For Until Loop done")

        for(i <- List(10,20,30)) {
            println(i)
        }
        println("For List Loop done")

        var i = 1
        while(i < 5) {
            println(i)
            i = i + 1
        }
        println("While Loop done")

    }


}