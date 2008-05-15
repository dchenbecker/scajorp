package org.scajorp.json
import scala.collection.mutable.HashMap

/**
* Scala representation of a JSONObject. JSONObject is simply a HashMap
* that knows how to turn itself into a JSON String via the capabilities
* offered by trait JSONSerializable. Simply call toString() and the object
* will be converted to a proper JSON String. 
* Is not yet secured against circular dependencies.
* 
* @author Marco Behler 
*/
class JSONObject extends HashMap[String,Any] with JSONSerializable{
       
    val opening_literal = "{"
    val closing_literal = "}"
     
    override protected def process() {        
        for((key,value) <- this) {
            appendJSONPair(key, value)
        }   
    }
   
   


}
