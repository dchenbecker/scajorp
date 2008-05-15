/*
 * JSONArray.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json

import scala.collection.jcl.ArrayList

class JSONArray extends ArrayList[Any] with JSONSerializable{
                
    override protected def serialize():String = {
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
    
    private def appendValues() {
        def doIt(value: Any) {
                appendValue(value);
                appendComma()
        }
        foreach(value =>  doIt(value))
                    
    }
    
  
   
  


}
