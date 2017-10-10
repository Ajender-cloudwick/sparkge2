/**
  * Created by AjenderNeelam on 9/28/17.
  */

  import org.apache.kafka.common.serialization.StringDeserializer
  import org.apache.spark.SparkConf
  import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
  import org.apache.spark.streaming.{Seconds, StreamingContext}

  /**
    * Consumes messages from one or more topics in Kafka and does wordcount.
    * Usage: DirectKafkaWordCount <brokers> <topics>
    *   <brokers> is a list of one or more Kafka brokers
    *   <topics> is a list of one or more kafka topics to consume from
    *
    * Example:
    *    $ bin/run-example streaming.DirectKafkaWordCount broker1-host:port,broker2-host:port \
    *    topic1,topic2
    */
  object DirectKafkaWordCount {

    //StreamingExamples.setStreamingLogLevels()

    //val Array(brokers, topics) = args
    val brokers = "localhost:9092"
    val topics = "test"
    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount").setMaster("spark://Ajenders-MacBook-Pro.local:7077")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))

    // Get the lines, split them into words, count the words and print
    val lines = messages.map(_.value)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()

    // Start the computation
    ssc.start()
    ssc.awaitTermination()

  }

