package org.scajorp

import java.lang.reflect.Method

class JSONParser {

  def createClassInstance(className: String, parameters: Map[String, Any]) = {

    val cls = Class.forName(className);
    val instance = cls.newInstance();

    for( (field,value) <- parameters)
    {
       //TODO classOf[String] -> classOf[value.type], value.getClass?
       val method = cls.getMethod(field + "_$eq", Array[Class[_ <: Any]](classOf[String]))
       method.invoke(instance, Array[Object](value.asInstanceOf[Object]))
    }

    instance
  }

  



}
