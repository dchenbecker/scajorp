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
    
    /**
     * Turns the given object into a JSON string.     
     *
     * @return the JSON string
     */
    def serialize(obj :AnyRef):String = {
        val jsonObj = obj match {
            case (map: Map[String,Any]) => createJSONObject(map)
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
    private def createJSONObject(poso: AnyRef, requestedFields : Option[Set[String]]): JSONObject= {                        
        val map = getFieldsMap(poso)      
        return createJSONObject(map)                
    }
      
    /**
    * Create a JSONObject from a Map.
    *
    * @return the JSONObject
    */
    private def createJSONObject(map: Map[String,Any]):JSONObject = {
        val result = new JSONObject
        map.foreach( pair => result += (pair._1 -> jsonValue(pair._2)))        
        return result
    }
    
    /**
    * Create a JSONArray from a Sequence (Lists, Arrays etc.).
    *
    * @return the JSONArray
    */
    private def createJSONArray(seq: Seq[Any]): JSONArray = {
        val result = new JSONArray
        seq.foreach( field => result += jsonValue(field))
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
                case map: Map[String,Any] => createJSONObject(map)                    
                case obj: AnyRef => createJSONObject(obj, None)
            }
    } 

      
}
