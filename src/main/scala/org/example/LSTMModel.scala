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

  }

  def encode(index: Map[String, Int], numWords: Int): Vector[Int] = {

  }

  private def prepad(seq: Vector[Int], to: Int, p: Int): Vector[Int] = {

  }

  private def truncate(seq: Vector[Int], to: Int): Vector[Int] = {

  }

  private def alignSequence(seq: Vector[Int], to: Int): Vector[Int] = seq match {

  }

}

class LSTMPreprocessedSeq[T](seq: IndexedSeq[T]) {
  def decode(inverseIndex: Map[T, String]): String = {

  }
}
