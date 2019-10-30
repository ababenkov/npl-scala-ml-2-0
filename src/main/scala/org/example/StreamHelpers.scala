package org.example

import java.util.Properties

import com.typesafe.config.Config
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer, FlinkKafkaProducer011}
import org.example.LSTMStreamingApp.env
import org.example.tcp.{SocketTextStreamSink, SocketTextStreamSource}
import org.apache.flink.streaming.api.scala._

trait StreamHelpers {

  val conf: Config

  def topicPrefix: String = {
    if (conf.getString("app.user").isEmpty) {
      throw new Exception("You should specify your username in application config")
    }
    conf.getString("app.user").replaceAll("\\.", "_")
  }

  def sentenceStream: DataStream[String] = conf.getString("app.interface") match {
    case "tcp" => env.addSource(new SocketTextStreamSource())
    case "kafka" =>
      implicit val stringTypeInfo: TypeInformation[String] = createTypeInformation[String]
      val properties = new Properties()
      properties.setProperty("bootstrap.servers", conf.getString("app.kafka.host"))
      env.addSource(new FlinkKafkaConsumer[String](
        s"${topicPrefix}_lecture10_input",
        new SimpleStringSchema(),
        properties
      ))
    case _ => throw new Exception("invalid interface config key!")
  }

  def writeResult(stream: DataStream[String]): DataStreamSink[String] = conf.getString("app.interface") match {
    case "tcp" => stream.addSink(new SocketTextStreamSink())
    case "kafka" =>
      val properties = new Properties()
      properties.setProperty("bootstrap.servers", conf.getString("app.kafka.host"))
      val producer = new FlinkKafkaProducer[String](
        s"${topicPrefix}_lecture10_output",
        new SimpleStringSchema(),
        properties
      )
      stream.addSink(producer)
    case  _ => throw new Exception("invalid interface config key!")
  }
}
