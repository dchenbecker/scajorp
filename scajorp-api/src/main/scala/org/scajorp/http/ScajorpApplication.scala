/*
 * ScajorpApplication.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.http

import java.lang.reflect.Method
import org.scajorp.json.request.JSONRequest
import org.scajorp.json.response.{JSONResponse,ValidResponse,ErrorResponse}

import scala.collection.mutable.HashMap


abstract class ScajorpApplication {

    val rpc_version = "2.0"

    val methodRegistry = new HashMap[String, Method]

    var prettyPrint = false


    /** constructor **/
    init()

    /**
    * Executes a JSONRequest's method with its parameters.
    *
    * @return the method's result
    */
    def execute(jsonRequest: JSONRequest): JSONResponse = {
        val result =
            if (jsonRequest.systemList()) {
                systemList()
            }
            else {
                invoke(jsonRequest.method, jsonRequest.parametersToArray)
            }                                      
        return new ValidResponse(rpc_version, result, jsonRequest.id)
    }


    def enablePrettyResults() = (prettyPrint = true)

        
    /**
    * Initialisation method. Override this method in your subclass and register
    * any (p)lain (o)ld (s)cala (o)bjects in it. 
    */
    def init(): Unit

    /**    
    * Adds all public methods of the specified class to the application methodRegistry.
    * A methodRegistry entry will be in the form of: 
    * "className.methodname" => "calculator.sum""    
    */
    protected def register(className: String, cls: Class[_]) {
        val methods = cls.getDeclaredMethods()
        methods.foreach(method => if (method.getName() != "$tag") methodRegistry.put(className + "." + method.getName(), method))        
    }


   /**
    * Prints out a list with all methods registered with this application. 
    */
    def developmentInfo()
    {
        println("******************************************************************************\n"
            + "*** Scajorp is running in DEVELOPMENT mode.                                 ***\n"
            + "***                       ^^^^^^^^^^^                                       ***\n"
            + "*** Registered Methods:                                                     ***\n"
            + "***" + systemList()+ "***\n"
            + "*******************************************************************************\n");
    }



    /**
    * Invokes a method that is registered with this application. If there is no such message,
    * a runtime error will be thrown.
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

    private def systemList(): List[String] = {
         var result = List[String]()
         methodRegistry.keys.foreach(value => result = result + value)
         println("SYSTEM " + result)
         result
    }

}
