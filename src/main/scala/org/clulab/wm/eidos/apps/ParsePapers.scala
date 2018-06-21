package org.clulab.wm.eidos.apps

import java.io.PrintWriter

import org.clulab.embeddings.word2vec.Word2Vec
import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.groundings.{ConceptEmbedding, OntologyNode}

object ParsePapers extends App {

  val reader = new EidosSystem()
  val w2v = reader.wordToVec


  def loadOtherOntology(file: String): Seq[ConceptEmbedding] = {
    val source = scala.io.Source.fromFile(file)
    val lines = source.getLines().toSeq
    val ces = for {
      line <- lines
      fields  = line.split("\t")
      path = fields(0).split(",").map(Word2Vec.sanitizeWord(_))
      examples = fields(1).split(",").map(Word2Vec.sanitizeWord(_))
      embedding = reader.wordToVec.makeCompositeVector(examples)
    } yield new ConceptEmbedding(path.mkString(OntologyNode.SEPARATOR), embedding)

    //source.close()

    ces
  }

  // todo: do I need to split on underscore -- you betcha!
  def getParents(path: String): Seq[String] = {
    path.split(OntologyNode.SEPARATOR).flatMap(elem => elem.split("[ |_]"))
  }

  def weightedParentScore(path1: String, path2: String): Double = {
    val parents1 = getParents(path1).reverse.map(Word2Vec.sanitizeWord(_))
    val parents2 = getParents(path2).reverse.map(Word2Vec.sanitizeWord(_))
    val k = math.min(parents1.length, parents2.length)
    var avg: Double = 0.0
    for (i <- 0 until k) {
      val score = w2v.stringSimilarity(parents1(i), parents2(i))
      avg += score / (i + 1)
    }
    avg
  }

  def dotProduct(v1:Array[Double], v2:Array[Double]):Double = {
    assert(v1.length == v2.length) //should we always assume that v2 is longer? perhaps set shorter to length of longer...
    var sum = 0.0
    var i = 0
    while(i < v1.length) {
      sum += v1(i) * v2(i)
      i += 1
    }
    sum
  }

  def pairwiseScore(ce1: ConceptEmbedding, ce2: ConceptEmbedding): Double = {
    val examplesScore = dotProduct(ce1.embedding, ce2.embedding)
    val structureScore = weightedParentScore(ce1.concept, ce2.concept)

    // todo: sum??
    examplesScore + structureScore
  }

  def mostSimilarIndicators(concepts: Seq[ConceptEmbedding], indicators: Seq[ConceptEmbedding], n: Int = 10): Seq[(String, Seq[(String, Double)])] = {
    concepts.map(c => (c.concept, mostSimilar(c, indicators, n)))
  }

  def mostSimilar(concept: ConceptEmbedding, indicators: Seq[ConceptEmbedding], n: Int): Seq[(String, Double)] = {
    val comparisons = indicators.map(indicator => (indicator.concept, pairwiseScore(concept, indicator)))
    if (n > 0) {
      comparisons.sortBy(- _._2).take(n)
    } else {
      comparisons.sortBy(- _._2)
    }
  }

  println("Getting started!")

  val sofiaFile = "/Users/bsharp/ech/onts/Sofia_Ontology.tbs"
  val bbnFile = "/Users/bsharp/ech/onts/terms.v3.txt"


  println(s"number of eidos ontologies - ${reader.loadableAttributes.ontologyGrounders.length}")
  val eidosConceptEmbeddings = reader.loadableAttributes.ontologyGrounders.head.conceptEmbeddings
  val sofiaConceptEmbeddings = loadOtherOntology(sofiaFile)
  val bbnConceptEmbeddings = loadOtherOntology(bbnFile)
  println("Finished loading other ontologies")


  // Make the table
  val eidos2Sofia = mostSimilarIndicators(eidosConceptEmbeddings, sofiaConceptEmbeddings, 5)
  eidos2Sofia.foreach(mapping => println(s"eidos: ${mapping._1} --> most similar sofia: ${mapping._2.mkString(",")}"))
  val eidos2BBN = mostSimilarIndicators(eidosConceptEmbeddings, bbnConceptEmbeddings, 5)
  eidos2BBN.foreach(mapping => println(s"eidos: ${mapping._1} --> most similar BBN: ${mapping._2.mkString(",")}"))
  val sofia2BBN = mostSimilarIndicators(sofiaConceptEmbeddings, bbnConceptEmbeddings, 5)
  sofia2BBN.foreach(mapping => println(s"sofia: ${mapping._1} --> most similar BBN: ${mapping._2.mkString(",")}"))


//  val eidosWDIConceptEmbeddings = reader.loadableAttributes.ontologyGrounders(1).conceptEmbeddings
//  val eidosFAOConceptEmbeddings = reader.loadableAttributes.ontologyGrounders(2).conceptEmbeddings
//
//  val mostSimilar = mostSimilarIndicators(eidosConceptEmbeddings, eidosFAOConceptEmbeddings ++ eidosWDIConceptEmbeddings)





}
