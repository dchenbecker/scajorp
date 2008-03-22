package org.scajorp

import java.lang.reflect.Method
import scala.collection.Map

class JSONSerializer {
  def serialize(jsonObject : _<: Any): String = {
    "Not yet implemented"
  }

   // TODO not yet working
  /*private def createPublicGetterFields(cls: Class[_ <: Any]): Map[String, Method] = {
        import scala.collection.immutable.Map

        val methods = cls.getMethods()

        methods.foldLeft(Map.empty[String, Method])({(resultMap, method) =>
            val name = method.getName()
            val params = method.getParameterTypes()
            // TODO improve getter detection
            if (params.length == 0) {
               // Java style getter check
                if (name.startsWith("get")) {
                  resultMap + Pair(name.substring(3).toLowerCase,method)
                }
                // Scala style getter check                
                else if (!name.endsWith("_$eq") && !name.startsWith("set")) {
                  resultMap + Pair(name.substring(0,name.lastIndexOf("_$eq")),method)
                }
                // failure, just pass through
                else {
                  resultMap
                }
           }

        })
    }*/
}
