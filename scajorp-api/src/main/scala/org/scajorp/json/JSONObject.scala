/*
 * JSONObject.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json
import scala.collection.mutable.HashMap

class JSONObject extends HashMap[String,Any] with JSONSerializable{
       
    val opening = "{"
    val closing = "}"
    
    override protected def serialize():String = {
        appendOpening()       
        appendPairs()
        appendClosing()
        builder.toString()
    }

    
    private def appendPairs() {
        def appendKey(key: String) {        
            builder.append("\"").append(key).append("\"")                
        }
        for((key,value) <- this) {
            appendKey(key)
            appendSeparator()
            appendValue(value)            
            appendComma()
        }   
    }
   
   


}
