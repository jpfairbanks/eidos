package org.clulab.wm.eidos.groundings

import java.io.File

import org.clulab.embeddings.word2vec.Word2Vec
import org.clulab.wm.eidos.test.TestUtils._
import org.clulab.wm.eidos.utils.{Sourcer, Timer}

class TestCompactWord2Vec extends ContraptionTest {

  protected def matches(array1: CompactWord2Vec.ArrayType, array2: Array[Double], epsilon: Double): Boolean = {
    array1.zip(array2).forall { case (value1, value2) =>
      math.abs(value1 - value2) < epsilon
    }
  }

  protected def matches(array1: CompactWord2Vec.ArrayType, array2: CompactWord2Vec.ArrayType): Boolean = {
    array1.zip(array2).forall { case (value1, value2) =>
      math.abs(value1 - value2) == 0
    }
  }

  //val filename = "/test_vectors.txt"
  val filename = "./cache/english/glove.840B.300d.vectors.txt.serialized"
//  val fullsizeText = new Word2Vec(Sourcer.sourceFromResource(filename), None)
//  val compactText = CompactWord2Vec(filename, resource = true, cached = false)
//  val tmpFile = File.createTempFile("test_vectors.", ".txt")
//  compactText.save(tmpFile.getAbsolutePath())
  val compactBin = Timer.time("Time to load compact vectors") {
    CompactWord2Vec(filename, resource = false, cached = true)
  }
  val epsilon = 0.000001

//  behavior of "compactText version"
//
//  it should "have the same size" in {
//    compactText.rows should be (fullsizeText.matrix.size)
//    compactText.columns should be (fullsizeText.dimensions)
//  }
//
//  it should "have the same contents" in {
//    compactText.keys.toSet should be (fullsizeText.matrix.keys.toSet)
//
//    compactText.keys.foreach { key =>
//      matches(compactText.get(key).get, fullsizeText.matrix.get(key).get, epsilon) should be (true)
//    }
//  }
//
//  it should "be normalized" in {
//    0.until(compactText.rows).foreach { row =>
//      val result = compactText.dotProduct(row, row)
//
//      (math.abs(result - 1) < epsilon) should be (true)
//    }
//  }
//
//  it should "get the same results" in {
//    compactText.keys.foreach { key1 =>
//      compactText.keys.foreach { key2 =>
//
//        val result1a = compactText.avgSimilarity(Array(key1), Array(key2))
//        val result2a = fullsizeText.avgSimilarity(Array(key1), Array(key2))
//
//        (math.abs(result1a - result2a) < epsilon) should be(true)
//
//        val result1b = compactText.avgSimilarity(Array(key1, key2), Array(key2, key1))
//        val result2b = fullsizeText.avgSimilarity(Array(key1, key2), Array(key2, key1))
//
//        (math.abs(result1b - result2b) < epsilon) should be(true)
//      }
//    }
//  }
//
//
//  behavior of "compactBin version"
//
//  it should "have the same size" in {
//    compactText.rows should be (compactBin.rows)
//    compactText.columns should be (compactBin.columns)
//  }
//
//  it should "have the same contents" in {
//    compactText.keys should be (compactBin.keys)
//
//    compactText.keys.foreach { key =>
//      matches(compactText.get(key).get, compactBin.get(key).get) should be (true)
//    }
//  }
//
//  it should "get the same results" in {
//    compactText.keys.foreach { key1 =>
//      compactText.keys.foreach { key2 =>
//
//        val result1a = compactText.avgSimilarity(Array(key1), Array(key2))
//        val result2a = compactBin.avgSimilarity(Array(key1), Array(key2))
//
//        (math.abs(result1a - result2a) == 0) should be(true)
//
//        val result1b = compactText.avgSimilarity(Array(key1, key2), Array(key2, key1))
//        val result2b = compactBin.avgSimilarity(Array(key1, key2), Array(key2, key1))
//
//        (math.abs(result1b - result2b) == 0) should be(true)
//      }
//    }
//  }
}
