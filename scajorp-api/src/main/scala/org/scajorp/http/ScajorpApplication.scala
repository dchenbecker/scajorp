/*
 * ScajorpApplication.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.http

import java.lang.reflect.Method
import org.scajorp.json.JSONRequest
import org.scajorp.json.JSONResponse
import scala.collection.mutable.HashMap

class ScajorpApplication {


    val rpc_version = "2.0"

    val methodRegistry = new HashMap[String, Method]
  
    /**
    * Executes a JSONRequest's method with its parameters.
    *
    * @return the method's result
    */
    def execute(jsonRequest: JSONRequest): JSONResponse = {        
        val result = invoke(jsonRequest.method, jsonRequest.parametersToArray)
        return new JSONResponse(rpc_version, result, 1)
    }
    
    /**    
    * Adds all public methods of the specified class to the application methodRegistry.
    * A methodRegistry entry will be in the form of: 
    * "className.methodname" => "calculator.sum""    
    */
    def register(className: String, cls: Class[_]) {
        val methods = cls.getDeclaredMethods()        
        methods.foreach(method => methodRegistry.put(className + "." + method.getName(), method))                                
    }
    
    /**
    * Invokes a method that is registered with this application. If not, an error
    * message will be printed out.
    *
    * @return the method's result
    */
    private def invoke(methodName: String, parameters: Array[AnyRef]): Any = {
        def doInvoke(method: Method) = {                             
            val instance = method.getDeclaringClass().newInstance()            
            method.invoke(instance, parameters)
        }
        val result = methodRegistry.get(methodName) match {
            case Some(method: Method) => doInvoke(method)
            case None => error("There is no method with name[=" + methodName + "] registered with this service.")
        }
        result
    }

}
