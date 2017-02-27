package com.actionml.templates.cb

import com.actionml.core.template.{Engine, Params, QueryResult}

class CBEngine(dataset: CBDataset, params: CBEngineParams)
  extends Engine[Event, CBEngineParams](dataset, params) {

  def train() = {
    println("All data received: ")
    dataset.events.foreach( event => println("Event: " + event))
  }

  def input(d: Event): Boolean = {
    println("Got a single Event: " + d)
    println("Kappa learing happens every event, starting now.")
    // todo should validate input value and return Boolean indicating that they were validated
    dataset.append(d)
    train()
    true
  }

  def inputCol(ds: Seq[Event]): Seq[Boolean] = {
    println("Got a Seq of " + ds.size + " Events")
    // todo should validate input values and return Seq of Bools indicating that they were validated
    println("Kappa learing happens every input of events, starting now.")
    dataset.appendAll(ds)
    train()
    Seq(true)
  }

  def query(): QueryResult = {QueryResult()}

}

case class CBEngineParams(
    id: String = "", // required
    dataset: String = "", // required, readFile now
    maxIter: Int = 100, // the rest of these are VW params
    regParam: Double = 0.0,
    stepSize: Double = 0.1,
    bitPrecision: Int = 24,
    modelName: String = "model.vw",
    namespace: String = "n",
    maxClasses: Int = 3)
  extends Params
