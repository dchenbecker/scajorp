package org.scajorp.json 

/**
 * ATTENTION: This trait is only supposed to be mixed-in/extended by JSONObject/JSONArray.
 * Its sole purpose is to turn either into a valid JSON string by overriding
 * their toString() method. TJSONWriter does not know how to process anything
 * but JSONObjects/JSONArrays and AnyVals. Another class (JSONSerializer) is 
 * therefore needed to create proper JSONObjects and JSONArrays.
 * 
 * @author Marco Behler 
 * @see JSONSerializer
 */
trait TJSONWriter {
    
    private val builder = new StringBuilder
 
    var opening_literal :String = _  // { or [
    
    var closing_literal :String = _ // } or [
    
            
    override def toString() = {  
        resetBuilder()
        serialize()        
    }
      
    private def serialize():String = {
        setWrappingLiterals()
        appendOpening()       
        processBody()
        appendClosing()
        builder.toString()
    }
      

    private def processBody(): Unit = {
        this match {
            case obj: JSONObject => processObject(obj)
            case array: JSONArray => processArray(array)
            case _ => error(">>> ERR: This isn't a JSONObject or JSONArray. Cannot process body <<<.")
        }
    }
    
    private def processObject(jsonObject: JSONObject) {     
        jsonObject.foreach(pair => appendObjectPair(pair._1, pair._2))        
    }
    
    private def processArray(jsonArray: JSONArray) {       
        jsonArray.foreach(value => appendArrayValue(value))                    
    }
  
    /* ----- string building helpers ------ */
    
    private def append(obj: Any) = {
        obj match {
            case (s: String) => builder.append("\"").append(s).append("\"")
            case (jsonObj : JSONObject) => builder.append(jsonObj.toString())
            case (jsonArray : JSONArray) => builder.append(jsonArray.toString())
            case _ => builder.append(obj)
        }
    }
    
    private def appendObjectPair(key: String, value: Any) {
        append(key);
        appendSeparator()
        append(value)            
        appendComma()
    }
    
    private def appendArrayValue(value: Any) {
        append(value);
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
        deleteLastComma().append(closing_literal)
    }
    
    private def deleteLastComma() = {
        builder.deleteCharAt(builder.length -1)        
    }
        
    private def resetBuilder() {        
        if (builder.length() > 1) builder.delete(0, builder.length() - 1)        
    }
      
    private def setWrappingLiterals() {
        this match {
            case obj: JSONObject => setObjectLiterals()
            case array: JSONArray => setArrayLiterals()   
            case _ => error(">>> ERR: This isn't a JSONObject or JSONArray. Cannot determine literals. <<<")                
        }
    }
           
    private def setObjectLiterals(){
        opening_literal = "{"
        closing_literal = "}"
    }
    
    private def setArrayLiterals(){
        opening_literal = "["
        closing_literal = "]"
    }
        

   
               
}
