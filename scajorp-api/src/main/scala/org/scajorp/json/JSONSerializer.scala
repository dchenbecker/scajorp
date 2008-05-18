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
     * Creates a serialized representation of the given object.
     */
    def serialize(obj :AnyRef):String = {
        val jsonObj = obj match {
            case (map: Map[String,Any]) => serializeMap(map)
            case (seq: Seq[_]) => serializeSequence(seq)            
            case _ => serializePOSO(obj, None)
        }        
        jsonObj.toString()            
    }
      
    private def serializePOSO(obj: AnyRef, requestedFields : Option[Set[String]]): JSONObject= {                        
        val map = getFieldMap(obj)      
        return serializeMap(map)                
    }
      
    private def serializeMap(map: Map[String,Any]):JSONObject = {
        val jsonObj = new JSONObject
        map.foreach( pair => jsonObj += (pair._1 -> serializeValue(pair._2)))        
        return jsonObj
    }
   
    private def serializeSequence(seq: Seq[Any]): JSONArray = {
        val jsonArray = new JSONArray
        seq.foreach( field => jsonArray += serializeValue(field))
        return jsonArray
    }
    
    private def getFieldMap(poso: AnyRef): Map[String,Any] =  {
        var fields = poso.getClass.getDeclaredFields()        
        val fieldMap = scala.collection.mutable.Map.empty[String,Any]
        
        fieldMap += (class_literal -> poso.getClass().getName())  
        for (field <- fields) {
            field.setAccessible(true)
            fieldMap += field.getName() -> field.get(poso)
        }                          
        fieldMap
    }
    
    
    def serializeValue(value: Any):Any = {
        value match {
                case (s:String) => value
                case (i:Integer) => value
                case (l:java.lang.Long) => value
                case (f:java.lang.Float) => value
                case (s:java.lang.Short) => value
                case (b:java.lang.Byte) => value
                case (b:java.lang.Boolean) => value
                case null => value                   
                case seq: Seq[Any] => serializeSequence(seq)                
                case map: Map[String,Any] => serializeMap(map)                    
                case obj: AnyRef => serializePOSO(obj, None)
            }
    } 

      
}
