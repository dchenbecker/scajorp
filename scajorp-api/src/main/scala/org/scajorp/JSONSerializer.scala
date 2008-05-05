package org.scajorp

import java.lang.reflect.Method
import scala.collection.Map
import scala.StringBuilder

class JSONSerializer {
    
    def serialize(obj :AnyRef):String = { 
      
        var methods = obj.getClass.getMethods
                          .toList
        
        methods = methods.filter(method => method.getDeclaringClass != classOf[AnyRef]
                                 && !method.getName.equals("$tag")
                                 && !method.getName.endsWith("_$eq"));
        methods = methods.sort((s,t) => s.getName.charAt(0).toLowerCase <                                
                               t.getName.charAt(0).toLowerCase);
        val builder = new StringBuilder;
    
        builder.append("{")    
        methods.foreach(method =>  builder.append(buildJSONPair(obj, method)).append(","));       
        builder.deleteCharAt(builder.length -1);
        builder.append("}");    
        builder.toString;
    }


    private def buildJSONPair(obj: AnyRef, method: Method) = {           
        "\"" + method.getName + "\"" + ":\"" + method.invoke(obj, Array()) + "\""
    }

    
}
