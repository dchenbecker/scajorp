package org.scajorp

import java.lang.reflect.Method
import scala.collection.Map
import scala.StringBuilder

class JSONSerializer {
    
    def serialize(obj :AnyRef):String = { 
      
        val getters = obj.getClass
                           .getMethods.toList
                            .filter(method => isGetter(method))
                              .sort((s,t) => s.getName.charAt(0) <                                
                                t.getName.charAt(0));
         toJson(obj, getters);                                
        
    }
    
    private def toJson(obj: AnyRef, methods: List[Method]): String = {
        val builder = new StringBuilder;    
        builder.append("{")    
        methods.foreach(method =>  builder.append(buildJSONPair(obj, method)).append(","));       
        builder.deleteCharAt(builder.length -1).append("}").toString;        
    }
    

    private def isGetter(method: Method): Boolean = {
        method.getDeclaringClass != classOf[AnyRef] &&
        !method.getName.equals("$tag") &&
        !method.getName.endsWith("_$eq")                     
    }


    private def buildJSONPair(obj: AnyRef, method: Method) = {           
        "\"" + method.getName + "\"" + ":\"" + method.invoke(obj, Array()) + "\""
    }

    
}
