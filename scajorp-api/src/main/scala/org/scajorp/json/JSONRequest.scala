/*
 * JSONRequest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json

import scala.collection.mutable.ArrayBuffer

class JSONRequest(val version: String, val method: String, val params: Collection[Any], val id: Int ) {
        
    // TODO error prone
   // def this(map: Map[String,Any]) = this(map("version"), map("method"), map("params"), map("id"))                          
        
    def parametersToArray():Array[AnyRef] = {
        val result = params match {
            case x:List[AnyRef] => x.toArray
        }
        result
    }
    
}
