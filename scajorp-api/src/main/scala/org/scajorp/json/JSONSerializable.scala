/*
 * JSONSerializable.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json 

trait JSONSerializable {
    
    val opening: String
    val closing: String
    
    protected val builder = new StringBuilder
    
    override def toString() = {  
        resetBuilder()
        serialize()        
    }
    
    /**
     * Resets this object's StringBuilder    
     */
    protected def resetBuilder() {        
        if (builder.length() > 1) builder.delete(0, builder.length() - 1)        
    }

    
    protected def appendValue(obj: Any) = {
        obj match {
            case (s: String) => builder.append("\"").append(s).append("\"")
            case (jsonObj : JSONObject) => builder.append(jsonObj.toString())
            case (jsonArray : JSONArray) => builder.append(jsonArray.toString())
            case _ => builder.append(obj)
        }
    }
    
    protected def appendComma() {
        builder.append(",")
    }
    
    protected def appendSeparator() {
        builder.append(":")
    }

    protected def appendOpening() {
        builder.append(opening)
    }
    
    protected def appendClosing() {
        builder.deleteCharAt(builder.length -1).append(closing)
    }
  
    
    protected def serialize(): String
}
