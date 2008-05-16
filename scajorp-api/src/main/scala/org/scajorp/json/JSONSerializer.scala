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
    /**
     * Creates a serialized representation of the given object.
     */
    def serialize(obj :AnyRef):String = {
        val jsonObj = obj match {
            case (collection: Collection[_]) => processCollection(collection)
            case _ => createJSONObject(obj, None)
        }        
        jsonObj.toString()            
    }
    

    private def processCollection(collection: Collection[_]) ={
        val json = collection match {
            case (map: Map[String,Any]) => convertMap(map)
            case _ => createJSONArray(collection, None)
        }
        json
    }


    
    /**
     * Creates a serialized representation of the given object, but only for
     * the fields in the requestedFields set. This method exists so that you 
     * can effectively hide internal fields if needed.
     */
    def serialize(obj : AnyRef, requestedFields : Set[String]) : String = {
        val jsonObj = createJSONObject(obj, Some(requestedFields))
        jsonObj.toString()
    }
    
  
    
    private def addField(field: Field, obj: AnyRef, jsonObj: JSONObject) = {
         
        field.setAccessible(true)            
        val name = field.getName()
        val value = field.get(obj)            
        value match {
            case (s:String) => jsonObj+= (name -> value)
            case (i:Integer) => jsonObj+= (name -> value)
            case (l:java.lang.Long) => jsonObj+= (name -> value)
            case (f:java.lang.Float) => jsonObj+= (name -> value)
            case (s:java.lang.Short) => jsonObj+= (name -> value)
            case (b:java.lang.Byte) => jsonObj+= (name -> value)
            case (b:java.lang.Boolean) => jsonObj+= (name -> value)                                    
            case null => jsonObj+= (name -> value)                   
            case obj: AnyRef => jsonObj+= (name -> createJSONObject(obj, None))
        }
    }

    private def createJSONObject(obj: AnyRef, requestedFields : Option[Set[String]]): JSONObject= {        
        val jsonObj = new JSONObject                
        jsonObj += ("jsonClass" -> obj.getClass().getName())

        val fields = getFields(obj, requestedFields)
     
        fields.foreach(field => addField(field,obj, jsonObj))
        jsonObj
    }
    
    private def createJSONArray(collection: Collection[_], requestedFields : Option[Set[String]]): JSONArray = {        
        val jsonArray = new JSONArray                
        collection.foreach(field => jsonArray += field)       
        jsonArray
    }
    
    
    private def convertMap(map: Map[String,Any]): JSONObject = {
        val jsonObject = new JSONObject
        for ((key, value) <- map) {
            jsonObject += (key -> value)
        }
        return jsonObject
    }
        

    private def getFields(obj: AnyRef, requestedFields : Option[Set[String]]) =  {
        // Either fetch all fields or only those requested
        val fields = requestedFields match {
            case Some(names) => obj.getClass.getDeclaredFields().filter({field => names.contains(field.getName)})
            case None => obj.getClass.getDeclaredFields
        }
        fields
    }
}
