package com.actionml.templates.cb

import com.actionml.core.template.QueryResult

class CBEngine(dataset: CBDataset) {

  def train() = {
    println("All data received: ")
    dataset.events.foreach( event => println("Event: " + event))
  }

  def input(d: Event): Boolean = {
    println("Got a single Event: " + d)
    println("Kappa learing happens every event, starting now.")
    // todo should validate input value and return Boolean indicating that they were validated
    train()
    true
  }

  def inputCol(ds: Seq[Event]): Seq[Boolean] = {
    println("Got a Seq of Events: " + ds)
    // todo should validate input values and return Seq of Bools indicating that they were validated
    println("Kappa learing happens every input of events, starting now.")
    train()
    Seq(true)
  }

  def query(): QueryResult = {QueryResult()}

}
