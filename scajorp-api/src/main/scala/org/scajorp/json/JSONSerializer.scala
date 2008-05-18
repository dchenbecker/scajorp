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
        map.foreach( (pair) => valueMatch(pair._1, pair._2, jsonObj))        
        return jsonObj
    }
    
    private def getFieldMap(poso: AnyRef): Map[String,Any] =  {
        var fields = poso.getClass.getDeclaredFields()        
        val map = scala.collection.mutable.Map.empty[String,Any]
        
        map += (class_literal -> poso.getClass().getName())  
        for (field <- fields) {
            field.setAccessible(true)
            map += field.getName() -> field.get(poso)
        }                          
        map
    }
    
    def serializeSequence(seq: Seq[Any]): JSONArray = {
        val jsonArray = new JSONArray                
        for (value <-seq) {
            value match {
                case (s:String) => jsonArray+= value
                case (i:Integer) => jsonArray+= value
                case (l:java.lang.Long) => jsonArray+= value
                case (f:java.lang.Float) => jsonArray+= value
                case (s:java.lang.Short) => jsonArray+= value
                case (b:java.lang.Byte) => jsonArray+= value
                case (b:java.lang.Boolean) => jsonArray+= value                                    
                case null => jsonArray+= value                   
                case seq: Seq[Any] => jsonArray += serializeSequence(seq)                
                case map: Map[String,Any] => jsonArray += serializeMap(map)                    
                case obj: AnyRef => jsonArray+= serializePOSO(obj, None)
            } 
        }     
        jsonArray
    }
         
    def valueMatch(name: String, value: Any, jsonObj: JSONObject):Unit = {
        value match {
                case (s:String) => jsonObj+= (name -> value)
                case (i:Integer) => jsonObj+= (name -> value)
                case (l:java.lang.Long) => jsonObj+= (name -> value)
                case (f:java.lang.Float) => jsonObj+= (name -> value)
                case (s:java.lang.Short) => jsonObj+= (name -> value)
                case (b:java.lang.Byte) => jsonObj+= (name -> value)
                case (b:java.lang.Boolean) => jsonObj+= (name -> value)                                    
                case null => jsonObj+= (name -> value)                   
                case seq: Seq[Any] => jsonObj += name -> serializeSequence(seq)                
                case map: Map[String,Any] => jsonObj +=  name -> serializeMap(map)                    
                case obj: AnyRef => jsonObj+= (name -> serializePOSO(obj, None))
            }
    } 

      
}
