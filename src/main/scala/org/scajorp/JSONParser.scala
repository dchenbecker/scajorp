package org.scajorp

import java.lang.reflect.Method
import scala.collection.mutable

import scala.collection.Map

class JSONParser {

  def resolve(jsonObject: Map[String, Any]): Option[Object] = {
    jsonObject("jsonClass") match {
        case className: String => createClassInstance(className, jsonObject.filterKeys(_ != "jsonClass"))
        case _ => None
    }

  }

  def createClassInstance(className: String, jsonFields: Map[String, Any]): Option[Object] = {
    try {
        val cls = Class.forName(className);
        val instance = cls.newInstance().asInstanceOf[Object];        

        val setterFields : Map[String, Method] = createPublicSetterFields(cls);

        for( (jsonFieldName, value) <- jsonFields)
            {
                setterFields(jsonFieldName) match {
                    //weird, does not allow me to add  case Some(method) or case _  -> unreachable code
                    //also I thought map(something) returns an option, but does not really in this case?
                    case method => method.invoke(instance, Array(value.asInstanceOf[Object]))

                  }
            }
        Some(instance)    
    }
        // should there simply be a check for Throwable
        // or is there gonna be e.g. a log message
        // specific to one exception/error (InstantiationException, SecurityException etc..)?
    catch {
        case genericError: Throwable => println(genericError); None;
    }

    

  }

  /**
    *   Creates a Map containing Method objects reflecting all public setter methods of the class or interface
    *   represented by the passed in Class object, as values. For keys, the stripped down fieldnames of the
    *   respective setter methods are used. Works with Scala setters_ and Java setMethods().
    *
    *   E.g. Scala: name_     :      map += ("name" -> method)
    *        Java: setName()  :      map += ("name" -> method)
    *
    **/
  private def createPublicSetterFields(cls: Class[_ <: Any]): Map[String, Method] = {

        val result = mutable.Map.empty[String, Method]

        val methods = cls.getMethods()

        for (method <- methods) {

            val name = method.getName
            
            // Scala style setter check
            if (name.lastIndexOf("_$eq") == name.length - 4) {
                result += (name.substring(0,name.lastIndexOf("_$eq")) -> method)

            }
            // Java style setter check
            else if (name.startsWith("set")) {
                result += (name.substring(3).toLowerCase -> method)
            }
        }

       result
    }





}
