package org.clulab.wm.eidos.utils

trait Namer {
  def name: String

  override def toString(): String = name
}