package org.clulab.wm.eidos.apps

import java.io.PrintWriter
import java.util.concurrent.ForkJoinPool

import org.clulab.processors.fastnlp.FastNLPProcessor
import org.clulab.serialization.DocumentSerializer
import org.clulab.wm.eidos.utils.FileUtils

import scala.collection.parallel.ForkJoinTaskSupport

object Annotator {

  collection.parallel.ForkJoinTasks.defaultForkJoinPool
  val nCores = 16

  val proc = new FastNLPProcessor()
  val serializer = new DocumentSerializer
  val files = FileUtils.findFiles("/work/zhengtang/foodsecurity/papers", "txt").par // .par makes this a parallel array
  files.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(nCores))

  for (f <- files){
    val text = FileUtils.getTextFromFile(f)
    val pw = FileUtils.printWriterFromFile(s"/work/zhengtang/foodsecurity/papers/${f.getName}.fastnlp")
    // read text
    // annotate
    val doc = proc.annotate(text)
    // serialize to a new file
    serializer.save(doc, pw)
    pw.close()
  }

}
