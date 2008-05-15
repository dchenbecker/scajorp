package org.scajorp.json

import scala.collection.jcl.ArrayList

/**
 * Scala representation of a JSONArray. JSONArray is simply an ArrayList
 * that knows how to turn itself into a JSON String via the capabilities
 * offered by trait JSONSerializable. Is not yet secured against circular dependencies.
 * 
 * @author Marco Behler 
 */
class JSONArray extends ArrayList[Any] with JSONSerializable{
              
    val opening_literal = "["
    val closing_literal = "]"
                
    override protected def process() {       
        foreach(value => createArrayValue(value))                    
    }
    
  
   
  


}
