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


object CBStub {


  val store = new Mongo
  val dataset = new CBDataset("test-resource", store)

  def readFile(fileName: String): Boolean = {
    val engine = new CBEngine(dataset)
    val input = Source.fromFile(fileName).getLines.map { line =>
      implicit val formats = Formats
      implicit val defaultFormats = DefaultFormats
      val obj = parse(line).extract[Event]
      println("Text: " + line)
      println("Event case class: " + obj)
      obj
    }.toSeq

    engine.inputCol(input)

    println("The completed input: " + dataset)
    // training happens automatically for Kappa style
    // engine.train()
    true
  }

}
