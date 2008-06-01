package org.scajorp.json

import java.io.BufferedReader
import scala.collection.mutable.ArrayBuffer


/**
*
* Representation of a json request string. Example:
* 
* {"jsonrpc": "2.0", "method": "subtract", "params": [42, 23], "id": 1}
*
* @param reader  a BufferedReader wrapping a valid json string
* 
* @author Marco Behler
*/
class JSONRequest(reader: BufferedReader) {

    
    private val requestMap: Map[String, Any] = parse()

    val jsonrpc: String = getVersion()

    val method: String = getMethod()

    val params: Collection[Any] = getParams()

    val id: Int = getId()


    /**
    *  Returns this request's parameters as an array.
    *  Is necessary for invoking methods via Java's reflection framework.
    *
    *  @return this request's parameters as an array
    */
    def parametersToArray(): Array[AnyRef] = {
        val result = params match {
            case x: List[AnyRef] => x.toArray
        }
        result
    }

    def systemList() = method == "system.listMethods"
    

    /* --- construction helpers ---*/

    /**
    * Turns the json string underlying this request's reader into a Map[String,Any]
    * 
    * @return a requestMap request map
    */
    private def parse(): Map[String, Any] = JSONParser.parseAll(JSONParser.obj, reader).get

    
    /**
    * Tries to retrieve the version of the requestMap request.
    * 
    * @return the version number or a runtime error if none exists
    */
    private def getVersion(): String = {
        requestMap.get("jsonrpc") match {
            case Some(version: String) => version
            case _ => error("No version specified for request. Example: {\"jsonrpc\": \"2.0\"...}")
        }
    }

    /**
    * Tries to retrieve the method of the requestMap request.
    *
    * @return the method or a runtime error if none exists
    */
    private def getMethod(): String = {
        requestMap.get("method") match {
            case Some(method: String) => method
            case _ => error("No method specified for request. Example: {...\"method\": \"subtract\"...}")
        }
    }

    /**
    * Tries to retrieve the parameters of the requestMap request.
    *
    * @return the parameters or a runtime error if none exist
    */
    private def getParams(): Collection[Any] = {
        requestMap.get("params") match {
            case Some(params: Collection[_]) => params
            case _ => error("No params specified for request. If no parameters are needed, an empty list must be supplied. Example: {...\"params\": [42, 23]...}")
        }
    }

    /**
    * Tries to retrieve the id of the requestMap request.
    *
    * @return id or a runtime error if none exists
    */
    private def getId(): Int = {
        requestMap.get("id") match {
            case Some(id: Int) => id
            case _ => error("No id specified for request. Example: ...\"id\": 1}")
        }
    }


}
