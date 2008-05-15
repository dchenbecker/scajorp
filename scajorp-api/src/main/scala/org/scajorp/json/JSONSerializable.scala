package org.scajorp.json 

trait JSONSerializable {
    
    val opening_literal: String
    val closing_literal: String
    
    private val builder = new StringBuilder
    
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

    
    protected def appendValue(obj: Any) = {
        obj match {
            case (s: String) => builder.append("\"").append(s).append("\"")
            case (jsonObj : JSONObject) => builder.append(jsonObj.toString())
            case (jsonArray : JSONArray) => builder.append(jsonArray.toString())
            case _ => builder.append(obj)
        }
    }
    

    def createJSONPair(key: String, value: Any) {
            appendValue(key);
            appendSeparator()
            appendValue(value)            
            appendComma()
    }
    
    def createArrayValue(value: Any) {
         appendValue(value);
         appendComma()
    }

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
        builder.deleteCharAt(builder.length -1).append(closing_literal)
    }
      
    private def serialize():String = {
        appendOpening()       
        process()
        appendClosing()
        builder.toString()
    }
      
    protected def process(): Unit

    
}
