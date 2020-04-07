import org.apache.spark.HashPartitioner
import org.apache.spark.sql.SparkSession

object PairRDDExamples {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().master("local").appName("Hello World").getOrCreate()

    val sc = sparkSession.sparkContext

    val marksRDD = sc.parallelize(List(("Sanjay", 91), ("Sudhanva",99), ("Maanya", 95),
      ("Vibha", 94), ("Sudhanva",100),("Vibha",96),("Maanya", 93), ("Sanjay", 90)))

    val hp = new HashPartitioner(4)
    marksRDD.partitionBy(hp)
    marksRDD.groupByKey().collect().foreach(println)
    marksRDD.reduceByKey((x,y) => (x+y)).collect().foreach(println)
    //println("Sort By Key:")
   // marksRDD.sortByKey().collect().foreach(println)
   // marksRDD.sortByKey(false)
   // marksRDD.filterByRange("A","N").collect().foreach(println)
    println("Filter elements with value greater than 95 :")

    marksRDD.filter( x => (x._2 > 95)).foreach(println)

    println(marksRDD.keys.collect().mkString(","))
    println(marksRDD.values.collect().mkString(","))

    println(marksRDD.mapValues(x => x + 100).collect().mkString(","))

    marksRDD.flatMapValues(x => x to 100).collect().foreach(println)

    println(marksRDD.flatMapValues(x => x-1 to x+1).collect().mkString(","))

    marksRDD.mapPartitions(words => {for (word <- words)yield(word,1)}).collect.foreach(println)
    marksRDD.mapPartitionsWithIndex((index, words) => {for (word <- words)yield(word,index)}).collect.foreach(println)



    val rdd5 = sc.parallelize(List("Good Morning", "Hello World", "hi", "Hello India", "Karnataka India"))

    rdd5.flatMap(x => x.split(" ")).map(x => (x,1)).groupByKey().map( x => (x._1,x._2.size)).collect.foreach(println)

    rdd5.flatMap(x => x.split(" ")).map((_,1)).reduceByKey((x,y) => x+y).collect().foreach(println)

    rdd5.flatMap(x => x.split(" ")).map((_,1)).reduceByKey(_ + _).collect().foreach(println)


    println("Enter string:")
    val str = scala.io.StdIn.readLine()
  }

}
