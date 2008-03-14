package org.scajorp

import java.lang.reflect.Method
import scala.collection.mutable

import scala.collection.Map

class JSONParser {

  def createClassInstance(className: String, fields: Map[String, Any]) = {

    val cls = Class.forName(className);
    val instance = cls.newInstance();
    val fieldMap = createSetterFieldMap(cls)

    for( (field,value) <- fields)
    {
       fieldMap(field) match {
            case method: Method => method.invoke(instance, Array[Object](value.asInstanceOf[Object]));
       }

    }

    instance
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
  private def createSetterFieldMap(cls: Class[_ <: Any]): mutable.Map[String, Method] = {

        val result = mutable.Map.empty[String, Method]

        val methods = cls.getMethods()

        for (method <- methods) {

            val name = method.getName

            if (name.lastIndexOf("_$eq") == name.length - 4) {
                //println("Adding Scala type setter => " + name)
                result += (name.substring(0,name.lastIndexOf("_$eq")) -> method)

            }
            else if (name.startsWith("set")) {
                //println("Adding Java type setter => " + name)
                result += (name.substring(3).toLowerCase -> method)
            }
        }

       result
    }





}
