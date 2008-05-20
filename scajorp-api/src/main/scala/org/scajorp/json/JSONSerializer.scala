package org.scajorp.json

import java.lang.reflect.Method
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import scala.collection.Map
import scala.StringBuilder


import org.scajorp.json._

/**
 * Utility object to handle the serialization of objects into their JSON
 * representation.
 *
 * @author Marco Behler
 * @author Derek Chen-Becker
 */
object JSONSerializer {
    
    val class_literal = "jsonClass"
    
    var parents = List[AnyRef]()
    
    /**
     * Turns the given object into a JSON string.     
     *
     * @return the JSON string
     */
    def serialize(obj :AnyRef):String = {
        val jsonObj = obj match {            
            case (set: Set[Any]) => createJSONArray(set)
            case (seq: Seq[_]) => createJSONArray(seq)                        
            case _ => createJSONObject(obj, None)
        }        
        jsonObj.toString()            
    }
      
    /**
     * Create a JSONObject from any (P)lain (O)ld (S)cala (O)bject.
     *
     * @return the JSONObject
     */
    private def createJSONObject(obj: AnyRef, requestedFields : Option[Set[String]]): JSONObject= {    
        val result = new JSONObject       
        parents += obj
        val map = obj match{
            case (m:Map[String,AnyRef]) => m
            case _ => getFieldsMap(obj) 
        }        
        map.foreach((pair) => addObjectPair(pair._1, pair._2, result))                      
        return result
    }
  
   
    /**
     * Create a JSONArray from a Sequence (Lists, Arrays etc.).
     *
     * @return the JSONArray
     */
    private def createJSONArray(seq: Seq[Any]): JSONArray = {
        parents += seq
        val result = new JSONArray
        seq.foreach( field => addArrayValue(field, result))
        return result
    }
    
    /**
     * Create a JSONArray from a Set.
     *
     * @return the JSONArray
     */
    private def createJSONArray(set: Set[Any]): JSONArray = {
        parents += set
        val result = new JSONArray
        set.foreach( field => addArrayValue(field, result))
        return result
    }
    

    /**
     * Fed with any (P)lain (O)ld (S)cala (O)bject will return a Map
     * with the poso's field names mapped to the field's value.
     * e.g.: Map("name" -> "Dr. Cox")
     * 
     * @return the Map(fieldName -> fieldValue)
     */
    private def getFieldsMap(poso: AnyRef): Map[String,Any] =  {
        var fields = poso.getClass.getDeclaredFields()        
        val fieldMap = scala.collection.mutable.Map.empty[String,Any]
        
        fieldMap += (class_literal -> poso.getClass().getName())  
        for (field <- fields) {
            field.setAccessible(true)
            fieldMap += field.getName() -> field.get(poso)
        }                          
        fieldMap
    }
    
    /**
     * This method will return the JSON value of Any. All AnyVals will remain
     * unchanged, whereas all Sequences will be converted to JSONArrays and all
     * maps will be converted to JSONObjects. POSOs will naturally be converted
     * to JSONObject's as well.
     *
     * @return the json value (value, JSONObject or JSONArray)
     */
    def jsonValue(value: Any):Any = {
        value match {
            case (s:String) => value
            case (i:Integer) => value
            case (l:java.lang.Long) => value
            case (f:java.lang.Float) => value
            case (s:java.lang.Short) => value
            case (b:java.lang.Byte) => value
            case (b:java.lang.Boolean) => value
            case null => value                   
            case seq: Seq[Any] => createJSONArray(seq)                
            case set: Set[Any] => createJSONArray(set)
            case a: AnyRef => createJSONObject(a, None)                                
        }
    } 
    

     
    private def addObjectPair(key: String, value: Any, result: JSONObject) {
        println("Circular reference? " + parents.contains(value))
        if (!parents.contains(value)) {
            result += (key -> jsonValue(value))
        }
    }
         
    private def addArrayValue(value: Any, result: JSONArray) {
        println("Circular reference? " + parents.contains(value))
        if (!parents.contains(value)) {
            result += jsonValue(value)
        }
    }    

      
}
