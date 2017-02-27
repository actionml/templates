package com.actionml.core.template

abstract class Engine[T](dataset: Dataset[T]) {

  def train()
  def input(d: T): Boolean
  def inputCol(dc: Seq[T]): Seq[Boolean]
  def query(): QueryResult

}

case class Query()
case class QueryResult()
