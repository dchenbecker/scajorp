package org.scajorp.json

import java.lang.reflect.Method
import java.lang.reflect.Field
import java.lang.reflect.Modifier

import scala.collection.Map
import scala.collection.mutable.{HashSet, Set}
import scala.StringBuilder

import org.scajorp.json.common.{TJSONSerializable,JSONArray,JSONObject}
import org.scajorp.json.response.{JSONResponse,ValidResponse,ErrorResponse, JSONError}

/**
 * Utility object, which serializes objects into their JSON representation. 
 *
 * @author Marco Behler
 * @author Derek Chen-Becker
 */
object JSONSerializer {
    

    val class_literal = "jsonClass"
    
    var throwOnCircularRefs = false
       
    var prettyPrint = false 


    
    /* ---------- PUBLIC API ---------- */
    
    
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



    

    /* ----------- INTERNAL API ---------- */
    

    /**
    * Turns any object into a TJSONSerializable and invokes its toString() method.   
    *    
    * @return the JSON string
    */
    private def serializeInternal(obj: AnyRef, includes: Option[Set[String]], excludes: Option[Set[String]]): String = {
        val seen = new HashSet[Any]()
        val jsonEntity = obj match {
            case collection: Collection[_] => serializeCollection(collection, seen)
            case _ => serializePOSO(obj, includes, excludes, seen)
        }
        jsonEntity.toString(prettyPrint)
    }


    
    /**
    * Serializes any (P)lain (O)ld (S)cala (O)bject, by converting it into 
    * a Map(fieldName -> fieldValue) and invoking serializeCollection()
    *
    * @return the JSONObject
    */
    private def serializePOSO(poso: AnyRef, includes: Option[Set[String]], excludes: Option[Set[String]], seen: Set[Any]): TJSONSerializable = {
        seen += poso
        val fieldMap = getFieldsMap(poso, includes, excludes)
        return serializeCollection(fieldMap, seen)
    }


   /**
    * Serializes collections. Will turn Sets and Sequences into {@link JSONArray}.
    * Turns Maps into {@link JSONObject}.
    *
    * @return the JSONArray
    */
    private def serializeCollection(collection: Collection[Any], seen: Set[Any]): TJSONSerializable = {
        seen += collection // TODO redundant when pojo, harmful?
        val result = collection match {
            case map: Map[String, Any] => createJSONObject(map, seen)
            case set: scala.collection.Set[Any] => createJSONArray(set, seen)
            case sequence: Seq[Any] => createJSONArray(sequence, seen)
            case _ => error("Not a collection[=" + collection + "]")
        }
        return result
    }

    
   /**
    * Creates a {@link JSONObject} from a Map[String,Any].
    *
    * @return the JSONObject 
    */
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

    
    /**
    * Creates a {@link JSONArray} from a Sequence or Set. 
    *
    * @return the JSONObject
    */
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




    /**
    * This method will return the JSON value of {@link Any}.
    *
    * All {@link AnyVal}s will remain unchanged. Sequences will be converted to {@link JSONArray}.
    * Maps and POSOs will naturally be converted to {@link JSONObject}s.    
    *
    * @return the appropriate json value (value, JSONObject or JSONArray)
    */
    private def jsonValue(value: Any, seen: Set[Any]) = {
        value match {
            case (s: String) => value
            case (i: Integer) => value
            case (l: java.lang.Long) => value
            case (f: java.lang.Float) => value
            case (s: java.lang.Short) => value
            case (b: java.lang.Byte) => value
            case (b: java.lang.Boolean) => value
            case (date: java.util.Date) => date.getTime()
            case null => value
            case collection: Collection[Any] => serializeCollection(collection, seen)
            case poso: AnyRef => serializePOSO(poso, None, None, seen)
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

        val className = poso.getClass().getName()
        if (className != classOf[ValidResponse].getName()
                && className != classOf[ErrorResponse].getName()
                && className != classOf[JSONError].getName()) {
            fieldMap += (class_literal -> className)    
        }
        
        for (field <- fields) {
            field.setAccessible(true)
            fieldMap += field.getName() -> field.get(poso)
        }
        fieldMap
    }


    
    def handleCircularRef(msg: String) = {
        if (throwOnCircularRefs) {
            throw new CircularReferenceException(msg)
        }
    }

 
}

class CircularReferenceException(message: String) extends Exception(message)