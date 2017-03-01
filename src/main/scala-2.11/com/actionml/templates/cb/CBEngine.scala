package com.actionml.templates.cb

import com.actionml.core.template.{Query, Engine, Params, QueryResult}
import org.joda.time.DateTime
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, Formats}

// Kappa style calls train with each input, may wait for explicit triggering of train for Lambda
class CBEngine(dataset: CBDataset, params: CBEngineParams)
  extends Engine[CBEvent, CBEngineParams, CBQuery, CBQueryResult](dataset, params) {

  def train() = {
    println("All data received: ")
    dataset.events.foreach( event => println("CBEvent: " + event))
  }

  def input(d: CBEvent): Boolean = {
    println("Got a single CBEvent: " + d)
    println("Kappa learing happens every event, starting now.")
    // todo should validate input value and return Boolean indicating that they were validated
    dataset.append(d)
    train()
    true
  }

  def inputCol(ds: Seq[CBEvent]): Seq[Boolean] = {
    println("Got a Seq of " + ds.size + " Events")
    // todo should validate input values and return Seq of Bools indicating that they were validated
    println("Kappa learing happens every input of events, starting now.")
    dataset.appendAll(ds)
    train()
    Seq(true)
  }

  def parseAndValidateInput(json: String): (CBEvent, Int) = {
    implicit val formats = Formats
    implicit val defaultFormats = DefaultFormats
    val event = parse(json).extract[CBEvent]
    (event, 0)
  }


  def query(query: CBQuery): CBQueryResult = {CBQueryResult()}

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

/*
Query
{
  "user": "psmith",
  "testGroupId": "testGroupA"
}

Results
{
  "variant": "variantA",
  "testGroupId": "testGroupA"
}

*/
case class CBQuery(
    user: String,
    groupId: String)
  extends Query

case class CBQueryResult(
    variant: String = "",
    groupId: String = "")
  extends QueryResult

case class Properties (
    testPeriodStart: DateTime, // ISO8601 date
    pageVariants: Seq[String], //["17","18"]
    testPeriodEnd: DateTime) // ISO8601 date

case class CBEvent (
    eventId: String,
    event: String,
    entityType: Option[String] = None,
    entityId: Option[String] = None,
    //properties: Option[Map[String, Any]] = None,
    properties: Option[Properties] = None,
    eventTime: String, // ISO8601 date
    creationTime: String) // ISO8601 date
  extends Query
