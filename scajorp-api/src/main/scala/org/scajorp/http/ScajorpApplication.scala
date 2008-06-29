/*
 * ScajorpApplication.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.http

import java.lang.reflect.Method
import org.scajorp.json.request.JSONRequest
import org.scajorp.json.response.{JSONResponse,ValidResponse,ErrorResponse, JSONError}

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
        
        if (jsonRequest.hasParseErrors) {
            ErrorResponse(rpc_version, JSONError(-32700,"Parse Error"), -1)
        }
        else if (jsonRequest.hasMissingParameters) {
            ErrorResponse(rpc_version, JSONError(-32600,"Invalid Request"), -1)
        }
        else if (jsonRequest.isSystemList) {
            ValidResponse(rpc_version, systemList(), jsonRequest.id.get)
        }
        else {
            invoke(jsonRequest)
        }        
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
    private def invoke(jsonRequest: JSONRequest): JSONResponse = {
        
        def doInvoke(method: Method) = {
            val instance = method.getDeclaringClass().newInstance()
            try {
                ValidResponse(rpc_version, method.invoke(instance, jsonRequest.parametersAsArray), jsonRequest.id.get)
            }
            catch {
                case illegalArgs :IllegalArgumentException =>
                     ErrorResponse(rpc_version, JSONError(-32602,"Invalid params"), jsonRequest.id.get)
            }

        }
        
        methodRegistry.get(jsonRequest.method.get) match {
            case Some(method: Method) => doInvoke(method)
            case None => ErrorResponse(rpc_version, JSONError(-32601,"Method not found"), jsonRequest.id.get)
        }        

    }

    
    private def systemList(): List[String] = {
         var result = List[String]()
         methodRegistry.keys.foreach(value => result = result + value)        
         result
    }

}
