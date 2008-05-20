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
    def serialize(obj :AnyRef) : String = serializeInternal(obj, None, None)

    def serializeOnly(obj : AnyRef, wanted : Set[String]) : String = serializeInternal(obj, Some(wanted), None)

    def serializeExcept(obj : AnyRef, unwanted : Set[String]) : String = serializeInternal(obj, None, Some(unwanted))

    private def serializeInternal(obj : AnyRef, wanted : Option[Set[String]], unwanted : Option[Set[String]]) : String = {
      val jsonObj = obj match {
        case (map: Map[String,Any]) => createJSONObject(map, wanted, unwanted)
        case (set: Set[Any]) => createJSONArray(set)
        case (seq: Seq[_]) => createJSONArray(seq)            
        case _ => createJSONObject(obj, wanted, unwanted)
      }        
      jsonObj.toString()            
    }


    /**
    * Create a JSONObject from any (P)lain (O)ld (S)cala (O)bject.
    *
    * @return the JSONObject
    */
    private def createJSONObject(poso: AnyRef, wantedFields : Option[Set[String]], unwantedFields : Option[Set[String]]): JSONObject= {
        val map = getFieldsMap(poso, wantedFields, unwantedFields)
        return createJSONObject(map)                
    }
      
    /**
    * Create a JSONObject from a Map.
    *
    * @return the JSONObject
    */
    private def createJSONObject(map: Map[String,Any],wantedFields : Option[Set[String]], unwantedFields : Option[Set[String]]):JSONObject = {
        val filteredMap = (wantedFields,unwantedFields) match {
	  case (None, None) => map
	  case (Some(wanted), _) => map.filter(field => wanted.contains(field.getName()))
	  case (None, Some(unwanted)) => map.filter(field => ! unwanted.contains(field.getName()))
	}

        val result = new JSONObject
        filteredMap.foreach( pair => result += (pair._1 -> jsonValue(pair._2)))        
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
    * e.g.: Map("name" -> "Dr. Cox"). wantedFields and unwantedFields are mutually exclusive; wantedFields 
    * always takes priority and will, in effect, override any supplied unwantedFields Set.
    *
    * @param wantedFields Optional set to request that only certain fields be serialized
    * @param unwantedFields Optional set to request that certain fields be excluded from serialization
    * 
    * @return the Map(fieldName -> fieldValue)
    */
    private def getFieldsMap(poso: AnyRef, wantedFields : Option[Set[String]], unwantedFields : Option[Set[String]]): Map[String,Any] = {
      var fields = (wantedFields,unwantedFields, poso.getClass.getDeclaredFields()) match {
	case (None, None, decFields) => decFields
	case (Some(wanted), _, decFields) => decFields.filter(field => wanted.contains(field.getName()))
	case (None, Some(unwanted), decFields) => decFields.filter(field => ! unwanted.contains(field.getName()))
      }
      
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
