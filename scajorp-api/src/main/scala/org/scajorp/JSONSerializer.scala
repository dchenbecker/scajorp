package org.scajorp

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
    

    
 /*   private def toJson(obj: AnyRef, methods: List[Method]): String = {
        val builder = new StringBuilder;
        builder.append("{\"jsonClass\":\"").append(obj.getClass().getName()).append("\",")
        
        methods.foreach(method =>  builder.append(buildJSONPair(obj, method)).append(","));
        builder.deleteCharAt(builder.length -1).append("}").toString;
    } */

    /**
     * Checks if a given method is a Scala- OR Java style getter.
     */
   /* private def isGetter(method: Method): Boolean = {
        method.getDeclaringClass != classOf[AnyRef] &&
        !method.getName.equals("$tag") &&
        !method.getName.endsWith("_$eq")
    } */
    

   /*  private def standardisedGetter(method: Method): String = {   
        
        var name = method.getName()        
        if (name.startsWith("get")) {                
            name.substring(3,4).toLowerCase() + name.substring(4)           
        }
        else name
    } */
    /**
     * Compares two names and sorts them ascendingly. Takes into account
     * that a getter name might start with "get".
     */
 /*   private def sortAscending(getter1: Method, getter2: Method): Boolean = {
        
        val name1 = standardisedGetter(getter1)
        val name2 = standardisedGetter(getter2)
              
        name1.charAt(0) < name2.charAt(0);
      
    } */
    
   

    /**
     * Creates a json pair from a getter method and its return value.
     * See methods key() and value().
     */
   /* private def buildJSONPair(obj: AnyRef, getter: Method): String = {
        key(getter) + value(obj, getter);
    } */

    /**
     * Creates a simple json-key string from a getter method.
     * Handles Java-style "getVariable" getters as well as Scala "variable"
     * getters.
     */
    /* private def key(getter: Method): String = {
        val builder = new StringBuilder;
        builder.append("\"").append(standardisedGetter(getter)).append("\":")                   
        builder.toString()
    } */

    /**
     * Invokes a getter method and uses its return value as a valid json-value.
     * Strings will be properly quoted, other primitive types won't and
     * objects and lists will be converted to json-objects or json-arrays.
     *
     */
  /*  private def value(obj: AnyRef, getter: Method) = {

        val methodResult = getter.invoke(obj, Array())
        methodResult match {
            case (s: String) => "\"" + s + "\""
            case _ => methodResult
        }
    } */




}
