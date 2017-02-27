package com.actionml.core.template

abstract class Engine[T, P](d: Dataset[T], p: P) {

  val dataset = d
  val params = p

  def train()
  def input(d: T): Boolean
  def inputCol(dc: Seq[T]): Seq[Boolean]
  def query(): QueryResult

}

case class Query()
case class QueryResult()
trait Params

