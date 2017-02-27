package com.actionml.templates.cb

import com.actionml.core.template.Dataset
import com.actionml.core.storage.Mongo
import java.io.{ObjectOutputStream, FileOutputStream, ObjectInputStream, FileInputStream}
import java.nio.file.{Files, Paths}
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.io.Source

case class Properties (
  testPeriodStart: String, // ISO8601 date
  pageVariants: Seq[String], //["17","18"]
  testPeriodEnd:String) // ISO8601 date

case class Event (
  eventId: String,
  event: String,
  entityType: Option[String] = None,
  entityId: Option[String] = None,
  properties: Option[Map[String, Any]] = None,
  eventTime: String, // ISO8601 date
  creationTime: String) // ISO8601 date


case class CBStub (config: CBCmdLineDriverConfig) {

  // Infant Template API, create a store, a dataset, and an engine
  // from then all input goes to the engine, which may or may not put it in the dataset using the store for
  // persistence. The engine may train with each new input or may train in batch mode, providing both Kappa
  // and Lambda style learning
  val store = new Mongo
  val dataset = new CBDataset("test-resource", store)

  implicit val formats = DefaultFormats

  val source = Source.fromFile(config.engineDefJSON)
  val engineJSON = try source.mkString finally source.close()

  //json4s style
  val params = parse(engineJSON).extract[CBEngineParams]
  // circe style ?????

  // Todo: params will eventually come from some store that is sharable
  val engine = new CBEngine(dataset, params)

  def readFile(fileName: String): Boolean = {
    var i = 0
    var input = Seq[Event]() // Source.fromFile closes the Stream for each .map so use .foreach
    Source.fromFile(fileName).getLines().foreach { line =>
      implicit val formats = Formats
      implicit val defaultFormats = DefaultFormats
      val obj = parse(line).extract[Event]
      println("Text: " + line)
      println("Event #" + i + ": " + obj)
      i += 1
      input = input :+ obj
    }

    engine.inputCol(input)

    println("The completed input: " + dataset)
    // training happens automatically for Kappa style
    // engine.train() should be triggered explicitly for Lambda
    true
  }

}
