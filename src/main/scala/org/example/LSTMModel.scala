package org.example

import java.io.{BufferedReader, InputStreamReader}
import java.util.stream.Collectors

import io.circe.generic.auto._
import io.circe.parser.{decode => decodeJson}
import io.circe.syntax._
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.api.ndarray.INDArray

import scala.collection.JavaConverters._
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.io.ClassPathResource

trait LSTMModel {
  val index: Map[String, Int] = {
    val is = getClass.getResourceAsStream("/word_index.json")
    val br = new BufferedReader(new InputStreamReader(is))
    val featuresJson: String = br.lines().collect(Collectors.joining())
    val decoded = decodeJson[Map[String, Int]](featuresJson)
    decoded.left.foreach { e =>
      throw new Exception(s"Decode error! ${e}")
    }
    decoded.right.get
  }

  val inverseIndex: Map[Int, String] = index.map(x => (x._2, x._1))

  val model: MultiLayerNetwork = KerasModelImport.importKerasSequentialModelAndWeights(
    new ClassPathResource("clf.h5").getFile.getPath
  )

  implicit def str2lstmstr(str: String): LSTMPreprocessableString = new LSTMPreprocessableString(str)
  implicit def seq2lstmseq[T](indexedSeq: IndexedSeq[T]): LSTMPreprocessedSeq[T] = new LSTMPreprocessedSeq[T](indexedSeq)
}

class LSTMPreprocessableString(str: String) {

  def encodeAlign(index: Map[String, Int], to: Int, numWords: Int): Vector[Int] = {
    alignSequence(encode(index, numWords), to)
  }

  def encode(index: Map[String, Int], numWords: Int): Vector[Int] = {
    val indexes: Array[Option[Int]] = str.split(" ").map(_.toLowerCase).map(index.get)
    indexes.map(_.filter(_ < numWords + 4)).map(_.getOrElse(index("<UNK>"))).toVector
  }

  private def prepad(seq: Vector[Int], to: Int, p: Int): Vector[Int] = {
    (0 until to - seq.size).map(_ => p).toVector ++ seq
  }

  private def truncate(seq: Vector[Int], to: Int): Vector[Int] = {
    seq.takeRight(to)
  }

  private def alignSequence(seq: Vector[Int], to: Int): Vector[Int] = seq match {
    case x if x.size > to => truncate(seq, to)
    case x if x.size < to => prepad(seq, to, 0)
    case x => x
  }

}

class LSTMPreprocessedSeq[T](seq: IndexedSeq[T]) {
  def decode(inverseIndex: Map[T, String]): String = {
    seq.map(inverseIndex).mkString(" ")
  }
}
