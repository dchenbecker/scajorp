package org.scajorp.json 

trait JSONSerializable {
      
    val opening_literal: String
    val closing_literal: String
    
    private val builder = new StringBuilder
    
    /**
     * Classes implementing JSONSerializable (JSONArray, JSONObject)
     * can be serialized to a valid JSON String by invoking this method.
     */
    override def toString() = {  
        resetBuilder()
        serialize()        
    }
               
    /**
     * Appends a JSON key-value pair to this trait's StringBuilder
     * followed by a comma.
     */
    protected def appendJSONPair(key: String, value: Any) {
        appendValue(key);
        appendSeparator()
        appendValue(value)            
        appendComma()
    }
    
    /**
     * Appends a JSON array value to builder this trait's StringBuilder
     * followed by a comma.
     */
    protected def appendArrayValue(value: Any) {
        appendValue(value);
        appendComma()
    }


    /* ----- Helper methods ----- */
    
    private def appendComma() {
        builder.append(",")
    }
    
    private def appendSeparator() {
        builder.append(":")
    }

    private def appendOpening() {
        builder.append(opening_literal)
    }
    
    private def appendClosing() {
        deleteLastComma().append(closing_literal)
    }
    
    private def deleteLastComma() = {
        builder.deleteCharAt(builder.length -1)        
    }
    
    private def appendValue(obj: Any) = {
        obj match {
            case (s: String) => builder.append("\"").append(s).append("\"")
            case (jsonObj : JSONObject) => builder.append(jsonObj.toString())
            case (jsonArray : JSONArray) => builder.append(jsonArray.toString())
            case _ => builder.append(obj)
        }
    }
    
    /**
     * Clears this object's StringBuilder.  
     * TODO see if needed
     */
    private def resetBuilder() {        
        if (builder.length() > 1) builder.delete(0, builder.length() - 1)        
    }
      
    /**
    * TODO
    */
    private def serialize():String = {
        appendOpening()       
        process()
        appendClosing()
        builder.toString()
    }
      
    /**
    * TODO
    */
    protected def process(): Unit

    
}
