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
        val jsonObj = createJSONObject(obj, None)
        jsonObj.toString()            
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
    
    private def createJSONObject(obj: AnyRef, requestedFields : Option[Set[String]]) : JSONObject = {
        val jsonObj = new JSONObject        
        jsonObj += ("jsonClass" -> obj.getClass().getName())

        // Either fetch all fields or only those requested
        val fields = requestedFields match {
            case Some(names) => obj.getClass.getDeclaredFields().filter({field => names.contains(field.getName)})
            case None => obj.getClass.getDeclaredFields
        }

        def doIt(field: Field) = {            
            field.setAccessible(true)
            jsonObj += (field.getName() -> field.get(obj))
        }

        fields.foreach(field => doIt(field))
        jsonObj
    }
    
}
