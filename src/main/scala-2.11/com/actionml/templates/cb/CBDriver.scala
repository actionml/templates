/*
 * Copyright ActionML, LLC under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.actionml.templates.cb

// driver for running Contextual Bandit as an early scaffold
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.io.Source

case class CBDriverConfig(
  modelOut: String = "", // dir model storage
  inputEvents: String = "", // events file
  engineDefJSON: String = ""  // engine.json file
)

case class CBEngineConfig(
  id: String = "", // required
  dataset: String = "", // required, file now
  maxIter: Int = 100, // the rest of these are VW params
  regParam: Double = 0.0,
  stepSize: Double = 0.1,
  bitPrecision: Int = 24,
  modelName: String = "model.vw",
  namespace: String = "n",
  maxClasses: Int = 3
)

object CBDriver {

  def main(args: Array[String]): Unit = {
    val parser = new scopt.OptionParser[CBDriverConfig]("scopt") {
      head("scopt", "3.x")

      opt[String]('m', "model").action((x, c) =>
        c.copy(modelOut = x)).text("Model storage location, eventually from the Engine config")

      opt[String]('d', "dataset").action((x, c) =>
        c.copy(inputEvents = x)).text("Event dataset input location, eventually fome the Engine config")

      opt[String]('e', "engine").required().action((x, c) =>
        c.copy(engineDefJSON = x)).text("Engine config, JSON file now, but eventually from shared key/value store")

      help("help").text("prints this usage text")

      note("some notes.")

    }

    // parser.parse returns Option[C]
    parser.parse(args, CBDriverConfig()) match {
      case Some(config) =>
        process(config)

      case None =>
      // arguments are bad, error message will have been displayed
    }
  }

  def process( config: CBDriverConfig ): Unit = {

    implicit val formats = DefaultFormats

    val source = Source.fromFile(config.engineDefJSON)
    val engineJSON = try source.mkString finally source.close()

    val params = org.json4s.native.JsonMethods.parse(engineJSON).extract[CBEngineConfig]
    val modelName = params.modelName
    val debug = true
  }
}
