package com.actionml.templates.cb

import com.actionml.core.storage.Store
import com.actionml.core.template.Dataset

class CBDataset(resourceId: String, store: Store) extends Dataset[Event](resourceId, store) {

  var events = List[Event]() // todo: this will go in a Store eventually

  // todo: we may want to use some operators overloading if this fits some Scala idiom well, but uses a Store so
  // may end up more complicated.

  // add one datum, possibley an Event, to the beginning of the dataset
  def append(datum: Event): Boolean = {
    events = datum :: events
    true
  }
  // takes a collection of data to append to the Dataset
  def appendAll(data: List[Event]): Seq[Boolean] = {
    events = events ::: data
    Seq[Boolean](true)
  }


}
