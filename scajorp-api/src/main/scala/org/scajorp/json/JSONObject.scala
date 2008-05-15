/*
 * JSONObject.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json
import scala.collection.mutable.HashMap

class JSONObject extends HashMap[String,Any] with JSONSerializable{
          
    override protected def serialize():String = {
        appendOpening()       
        appendPairs()
        appendClosing()
        builder.toString()
    }

    private def appendKey(key: String) {        
        builder.append("\"").append(key).append("\"")                
    }
    
    private def appendOpening() {
        builder.append("{")
    }
    
    private def appendClosing() {
        builder.deleteCharAt(builder.length -1).append("}")
    }
    
   
    
    private def appendPairs() {
        for((key,value) <- this) {
            appendKey(key)
            appendSeparator()
            appendValue(value)            
            appendComma()
        }   
    }
   
   


}
