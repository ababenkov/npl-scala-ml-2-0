package org.example

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.flink.streaming.api.scala._
import org.example.tcp.{SocketConnection, SocketTextStreamSink, SocketTextStreamSource}
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

object LSTMStreamingApp extends LSTMModel with StreamHelpers {

  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  val conf: Config = ConfigFactory.load()
  lazy val conn = new SocketConnection(conf.getString("app.tcp.host"), conf.getInt("app.tcp.port"))


  def predictSequence(seq: Vector[Float]): Float = {
    val input = Nd4j.create(Array[Int](1, seq.length), seq.toArray)
    model.output(input).getFloat(0)
  }

  def testEncoding() = {
    val testCase = "I love this awesome film"
    val testCaseEncoded = testCase.encode(index, 20000)
    assert(testCase == testCaseEncoded.decode(inverseIndex))
  }

  def main(args: Array[String]): Unit = {

    testEncoding()

    val predictionStream = sentenceStream
      .map(somePreprocessing(_))
      .map(x => predictSequence(x).toString)

    writeResult(predictionStream)

    env.execute("Flink LSTM Example")
  }

}
