package org.scajorp.json

import java.lang.reflect.Method
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import scala.collection.Map
import scala.collection.mutable. {HashSet, Set}
import scala.StringBuilder


import org.scajorp.json._

class CircularReferenceException(message: String) extends Exception(message)

/**
 * Utility object to handle the serialization of objects into their JSON
 * representation.
 *
 * @author Marco Behler
 * @author Derek Chen-Becker
 */
object JSONSerializer {
    
    var throwOnCircularRefs = false
    
    val class_literal = "jsonClass"
   
    var prettyPrint = false // TODO revise
    
    /**
    * Turns the given object into a JSON string.
    *
    * @return the JSON string
    */
    def serialize(obj: AnyRef): String = serializeInternal(obj, None, None)
    

    /**
    *
    * Turns the given object into a JSON string, serializing however
    * only the values specified as includes.
    *
    * @return the JSON string
    */
    def serializeOnly(obj: AnyRef, includes: Set[String]): String = serializeInternal(obj, Some(includes), None)


    /**
    *
    * Turns the given object into a JSON string, serializing the object without
    * all fields specified as excludes.
    *
    * @return the JSON string
    */
    def serializeExcept(obj: AnyRef, excludes: Set[String]): String = serializeInternal(obj, None, Some(excludes))

    /**
    * Does the heavy lifting. Creates a JSONArray (from a collection) or a JSONObject
    * (from a poso) and simply invokes its toString() method.    
    *    
    * @return the JSON string
    */
    private def serializeInternal(obj: AnyRef, includes: Option[Set[String]], excludes: Option[Set[String]]): String = {
        val seen = new HashSet[Any]()
        val jsonEntity = obj match {
            case collection: Collection[_] => processCollection(collection, seen)
            case _ => processPOSO(obj, includes, excludes, seen)
        }
        jsonEntity.toString(prettyPrint)
    }

    /**
    * Create a JSONObject from any (P)lain (O)ld (S)cala (O)bject. Will turn
    * the pojo into a Map(fieldName -> fieldValue) and delegate responsibilty
    * to processCollection().
    *
    * @return the JSONObject
    */
    private def processPOSO(poso: AnyRef, includes: Option[Set[String]], excludes: Option[Set[String]], seen: Set[Any]): TJSONSerializable = {
        seen += poso
        val fieldMap = getFieldsMap(poso, includes, excludes)
        return processCollection(fieldMap, seen)
    }


    private def processCollection(collection: Collection[Any], seen: Set[Any]): TJSONSerializable = {
        seen += collection // TODO redundant when pojo, harmful?
        val result = collection match {
            case map: Map[String, Any] => createJSONObject(map, seen)
            case set: scala.collection.Set[Any] => createJSONArray(set, seen)
            case sequence: Seq[Any] => createJSONArray(sequence, seen)
            case _ => error("Not a collection[=" + collection + "]")
        }
        return result
    }

    private def createJSONObject(map: Map[String, Any], seen: Set[Any]): JSONObject = {
        val result = new JSONObject()
        map.foreach({
            case (key, value) =>
                if (seen.contains(value)) {
                    handleCircularRef("Circular reference on " + key + ", " + value)
                } else {
                    result += (key -> jsonValue(value, seen))
                }
        })
        return result
    }

    private def createJSONArray(col: Collection[Any], seen: Set[Any]): JSONArray = {
        val result = new JSONArray()
        col.foreach(value => {
            if (seen.contains(value)) {
                handleCircularRef("Circular ref in collection on value " + value)
            } else {
                result += jsonValue(value, seen)
            }
        })
        return result
    }

    def handleCircularRef(msg: String) = {
        if (throwOnCircularRefs) {
            throw new CircularReferenceException(msg)
        }
    }


    /**
    * This method will return the JSON value of Any. All AnyVals will remain
    * unchanged, whereas all Sequences will be converted to JSONArrays and all
    * maps will be converted to JSONObjects. POSOs will naturally be converted
    * to JSONObject's as well.
    *
    * @return the json value (value, JSONObject or JSONArray)
    */
    def jsonValue(value: Any, seen: Set[Any]) = {
        value match {
            case (s: String) => value
            case (i: Integer) => value
            case (l: java.lang.Long) => value
            case (f: java.lang.Float) => value
            case (s: java.lang.Short) => value
            case (b: java.lang.Byte) => value
            case (b: java.lang.Boolean) => value
            case null => value
            case collection: Collection[Any] => processCollection(collection, seen)
            case poso: AnyRef => processPOSO(poso, None, None, seen)
        }
    }



    /**
    * Fed with any (P)lain (O)ld (S)cala (O)bject will return a Map
    * with the poso's field names mapped to the field's value.
    * e.g.: Map("name" -> "Dr. Cox"). include and exclude are mutually exclusive; include
    * always takes priority and will, in effect, override any supplied exclude Set.
    *
    * @param includes Optional set to request that only certain fields be serialized
    * @param excludes Optional set to request that certain fields be excluded from serialization
    *
    * @return the Map(fieldName -> fieldValue)
    */
    private def getFieldsMap(poso: AnyRef, includes: Option[Set[String]], excludes: Option[Set[String]]): Map[String, Any] = {
        val fields =
        (poso.getClass.getDeclaredFields(), includes, excludes) match {
            case (fields, None, None) => fields
            case (fields, Some(includes), _) => fields.filter(field => includes.contains(field.getName()))
            case (fields, None, Some(excludes)) => fields.filter(field => !excludes.contains(field.getName()))
        }

        val fieldMap = scala.collection.mutable.Map.empty[String, Any]

        // hack, will be removed soon
        val className = poso.getClass().getName()
        if (className != classOf[JSONResponse].getName()) {
            fieldMap += (class_literal -> className)    
        }
        
        for (field <- fields) {
            field.setAccessible(true)
            fieldMap += field.getName() -> field.get(poso)
        }
        fieldMap
    }

 
}
