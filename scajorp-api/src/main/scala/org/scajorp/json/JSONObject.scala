package org.scajorp.json
import scala.collection.jcl.TreeMap

/**
* Scala representation of a JSONObject. JSONObject is simply a HashMap
* that knows how to turn itself into a JSON String via the capabilities
* offered by trait JSONSerializable. Simply call toString() and the object
* will be converted to a proper JSON String. 
* Is not yet secured against circular dependencies.
* 
* @author Marco Behler 
*/
class JSONObject extends TreeMap[String,Any] with TJsonWriter{
       
}
