package org.clulab.wm.eidos.apps

import java.io.PrintWriter

import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.groundings.{ConceptEmbedding, OntologyNode}

object ParsePapers extends App {

  val reader = new EidosSystem()


  def loadOtherOntology(file: String): Seq[ConceptEmbedding] = {
    val source = scala.io.Source.fromFile(file)
    val lines = source.getLines().toSeq
    val ces = for {
      line <- lines
      fields  = line.split("\t")
      path = fields(0).split(",")
      examples = fields(1).split(",")
      embedding = reader.wordToVec.makeCompositeVector(examples)
    } yield new ConceptEmbedding(path.mkString(OntologyNode.SEPARATOR), embedding)

    source.close()

    ces
  }


  // todo: do I need to split on underscore -- you betcha!

  val sofiaFile = ""

  println(s"number of ontologies - ${reader.loadableAttributes.ontologyGrounders.length}")
  val eidosConceptEmbeddings = reader.loadableAttributes.ontologyGrounders.head.conceptEmbeddings
  val sofiaConceptEmbeddings = loadOtherOntology(sofiaFile)


  val pw = new PrintWriter("/Users/ech/eidos.un.tsv")
  reader.loadableAttributes.ontologyGrounders.head.conceptEmbeddings.foreach(ce =>
    pw.write(s"${ce.concept.replaceAll("/", ",")}\t\t
  )
  pw.close()

}
