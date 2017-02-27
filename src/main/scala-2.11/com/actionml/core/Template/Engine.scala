package com.actionml.core.template

abstract class Engine[T](dataset: Dataset[T]) {

  def train()
  def input()
  def query(): QueryResult

}

case class Query()
case class QueryResult()
