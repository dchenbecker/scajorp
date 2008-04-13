package org.scajorp

import java.lang.reflect.Method
import scala.collection.Map

class JSONSerializer {
  def serialize(obj : _<:Any):String = {
  
   // val getters = createPublicGetterFields(scalaClass)
    "{not yet implemented}"
  //  "{\"street\":\"Mulholland Drive\",\"city\":\"Los Angeles\",\"state\":\"CA\",\"zip\":\"12345\"}"
  }


   // TODO not yet working
 /* private def createPublicGetterFields(obj: _ <:Any): Map[String, Method] = {
          
        var jsonString = "{"
        
        val methods = cls.getMethods()

        for( method <- methods)
        {
            val name = method.getName()
            val params = method.getParameterTypes()

            if (params.length == 0) {
            
               // Java style getter check
                if (name.startsWith("get")) {
                  jsonString = jsonString + name + method.                  
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
            

        }
        jsonString = jsonString +"}"
        jsonString
        } */
    
}
