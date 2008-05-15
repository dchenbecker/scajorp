/*
 * JSONArray.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json

import scala.collection.jcl.ArrayList

class JSONArray extends ArrayList[Any]{
    
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
        appendValues()
        appendClosing()
        builder.toString()
    }


    
    private def appendOpening() {
        builder.append("[")
    }
    
    private def appendClosing() {
        builder.deleteCharAt(builder.length -1).append("]")
    }
    
    private def appendComma() {
        builder.append(",")
    }
    
    private def appendValues() {
        def doIt(value: Any) {
                appendValue(value);
                appendComma()
        }
        foreach(value =>  doIt(value))
                    
    }
   
   private def appendValue(obj: Any) = {
        obj match {
            case (s: String) => builder.append("\"").append(s).append("\"")
            case (jsonObj : JSONObject) => builder.append(jsonObj.toString())
            case (jsonArray : JSONArray) => builder.append(jsonArray.toString())
            case _ => builder.append(obj)
        }
    }


}
