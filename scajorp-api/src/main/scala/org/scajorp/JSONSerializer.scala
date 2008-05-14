package org.scajorp

import java.lang.reflect.Method
import scala.collection.Map
import scala.StringBuilder

class JSONSerializer {

    def serialize(obj :AnyRef):String = {

        val getters = obj.getClass
        .getMethods.toList
        .filter(method => isGetter(method))
        .sort((s,t) => sortAscending(s, t))
        toJson(obj, getters);

    }

    private def toJson(obj: AnyRef, methods: List[Method]): String = {
        val builder = new StringBuilder;
        builder.append("{")
        methods.foreach(method =>  builder.append(buildJSONPair(obj, method)).append(","));
        builder.deleteCharAt(builder.length -1).append("}").toString;
    }

    /**
     * Checks if a given method is a Scala- OR Java style getter.
     */
    private def isGetter(method: Method): Boolean = {
        method.getDeclaringClass != classOf[AnyRef] &&
        !method.getName.equals("$tag") &&
        !method.getName.endsWith("_$eq")
    }
    
    /**
     * Checks if a given method is a Java style getter.
     */
    private def isJavaGetter(method: Method): Boolean = {
        return method.getName().startsWith("get");
    }

    /**
     * Compares two names and sorts them ascendingly. Takes into account
     * that a getter name might start with "get".
     */
    private def sortAscending(getter1: Method, getter2: Method): Boolean = {
        
        val name1 = getter1.getName()
        val name2 = getter2.getName()
        
        if (isJavaGetter(getter1) && isJavaGetter(getter2)) {
            Character.toLowerCase(name1.charAt(3)) < Character.toLowerCase(name2.charAt(3));
        }
        else if (isJavaGetter(getter1) && !isJavaGetter(getter2)) {
            Character.toLowerCase(name1.charAt(3)) < name2.charAt(0);
        }
        else if (!isJavaGetter(getter1) && isJavaGetter(getter2)) {
            name1.charAt(0) < Character.toLowerCase(name2.charAt(3))
        }
        else {
            name1.charAt(0) < name2.charAt(0);
        }
    }
    
   

    /**
     * Creates a json pair from a getter method and its return value.
     * See methods key() and value().
     */
    private def buildJSONPair(obj: AnyRef, getter: Method): String = {
        key(getter) + value(obj, getter);
    }

    /**
     * Creates a simple json-key string from a getter method.
     * Handles Java-style "getVariable" getters as well as Scala "variable"
     * getters.
     */
    private def key(getter: Method): String = {
        val builder = new StringBuilder;
        builder.append("\"")
        if (isJavaGetter(getter)) {
            builder.append(getter.getName().substring(3));
            builder.setCharAt(1, Character.toLowerCase(builder.charAt(1)));
        }
        else {
            builder.append(getter.getName());
        }
        builder.append("\":")
        builder.toString()
    }

    /**
     * Invokes a getter method and uses its return value as a valid json-value.
     * Strings will be properly quoted, other primitive types won't and
     * objects and lists will be converted to json-objects or json-arrays.
     *
     */
    private def value(obj: AnyRef, getter: Method) = {

        val methodResult = getter.invoke(obj, Array())
        methodResult match {
            case (s: String) => "\"" + s + "\""
            case _ => methodResult
        }
    }




}
