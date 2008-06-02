package org.scajorp.json 

/**
 * ATTENTION: This trait is only supposed to be mixed-in/extended by JSONObject/JSONArray.
 * Its sole purpose is to turn either into a valid JSON string by overriding
 * their toString() method. TJSONSerializable does not know how to process anything
 * but JSONObjects/JSONArrays and AnyVals. Another class (JSONSerializer) is 
 * therefore needed to create proper JSONObjects and JSONArrays.
 * 
 * @author Marco Behler 
 * @see JSONSerializer
 */

import java.io.OutputStream
import java.io.OutputStreamWriter

trait TJSONSerializable {
    
    private val builder = new StringBuilder
 
    val default_encoding = "UTF-8"

    var prettyPrint = false
    
    var opening_literal :String = _  // { or [
    
    var closing_literal :String = _ // } or [


            
    override def toString():String = {  
        resetBuilder()
        serialize()
    }

    def toString(prettyPrint: Boolean):String = {
        this.prettyPrint = prettyPrint
        toString()
    }
    
    def toOutputStream(outputStream: OutputStream) {
        val writer = new OutputStreamWriter(outputStream, default_encoding)    
        val json = toString()        
        writer.write(json, 0, json.length)
        writer.flush() // never forget to flush!!
    }
      
    private def serialize():String = {
        setWrappingLiterals()
        opening()
        newlineIfPretty()
        processBody()
        closing()
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
        tabIfPretty()
        append(key)
        tabIfPretty()
        separator()
        spaceIfPretty()
        append(value)        
        comma()
        newlineIfPretty()
    }
    
    private def appendArrayValue(value: Any) {
        append(value);
        comma()
        newlineIfPretty()
    }

    private def comma():Unit = builder.append(",")

    private def separator() = builder.append(":")
            
    private def opening() = builder.append(opening_literal)

    private def closing() = deleteLastComma().append(closing_literal)
                
    private def tabIfPretty() = if (prettyPrint) builder.append("\t")

    private def newlineIfPretty() = if (prettyPrint) builder.append("\n")
    
    private def spaceIfPretty() = if (prettyPrint) builder.append(" ")
        
    private def deleteLastComma() = {
        if (prettyPrint) builder.deleteCharAt(builder.length -2)
        else builder.deleteCharAt(builder.length -1)
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
