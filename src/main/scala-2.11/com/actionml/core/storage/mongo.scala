package com.actionml.core.storage

import com.mongodb.casbah.Imports._

class Mongo(master: String = "localhost", port: Int = 27017) extends Store {
  lazy val client = MongoClient("master", port)


}
