package com.actionml.templates.cb

import com.actionml.core.template.Dataset
import com.actionml.core.storage.Mongo
import java.io.{ObjectOutputStream, FileOutputStream, ObjectInputStream, FileInputStream}
import java.nio.file.{Files, Paths}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.joda.time.{DateTime, Period}

import scala.io.Source


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
    var input = Seq[CBEvent]() // Source.fromFile closes the Stream for each .map so use .foreach
    Source.fromFile(fileName).getLines().foreach { line =>
      implicit val formats = Formats
      implicit val defaultFormats = DefaultFormats
      val (event, errcode) = engine.parseAndValidateInput(line)
      if( errcode != 0) {
        println("Got and error validating string: " + line)
      } else {
        println("Event #" + i + ": " + event)
        println("Text: " + line)
        input = input :+ event
      }
      i += 1
    }

    engine.inputCol(input)

    println("The completed input: " + dataset)
    // training happens automatically for Kappa style
    // engine.train() should be triggered explicitly for Lambda

    val result = engine.query(CBQuery("pferrel", "group 1"))
    println("Queried and received variant: " + result.variant + " groupId: " + result.groupId)
    true
  }

}
