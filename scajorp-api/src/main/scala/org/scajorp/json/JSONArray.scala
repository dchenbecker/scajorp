/*
 * JSONArray.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json

import scala.collection.jcl.ArrayList

class JSONArray extends ArrayList[Any] with JSONSerializable{
              
    val opening = "["
    val closing = "]"
  
    override protected def serialize():String = {
        appendOpening()       
        appendValues()
        appendClosing()
        builder.toString()
    }
          
    private def appendValues() {
        def doIt(value: Any) {
            appendValue(value);
            appendComma()
        }
        foreach(value =>  doIt(value))
                    
    }
    
  
   
  


}
