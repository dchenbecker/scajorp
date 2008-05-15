/*
 * JSONObject.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json
import scala.collection.mutable.HashMap

class JSONObject extends HashMap[String,Any]{
  
    val builder = new StringBuilder
    
    override def toString() = {  
        resetBuilder()
        serialize()        
    }
           
    /**
    * Resets this object's StringBuilder    
    */
    private def resetBuilder() {        
        if (builder.length() > 1) builder.delete(0, builder.length() - 1)        
    }

        
    private def serialize():String = {
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
    
    private def appendSeparator() {
        builder.append(":")
    }
    
    private def appendComma() {
        builder.append(",")
    }
    
    private def appendPairs() {
        for((key,value) <- this) {
            appendKey(key)
            appendSeparator()
            appendValue(value)            
            appendComma()
        }   
    }
   
   private def appendValue(obj: Any) = {
        obj match {
            case (s: String) => builder.append("\"").append(s).append("\"")
            case (jsonObj : JSONObject) => builder.append(jsonObj.toString())
            case _ => builder.append(obj)
        }
    }


}
