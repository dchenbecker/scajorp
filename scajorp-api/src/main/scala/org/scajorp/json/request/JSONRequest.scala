package org.scajorp.json.request

import java.io.BufferedReader
import java.io.StringReader

import scala.collection.mutable.ArrayBuffer


/**
* Scala representation of a json request string, which offers a couple of convenience
* methods. Can be constructed from any String or BufferedReader.
* 
* @param reader
*               a BufferedReader wrapping a valid json string
* 
* @author Marco Behler
*/
class JSONRequest(reader: BufferedReader) {
    

    val requestMap: Option[Map[String, Any]] = parse()

    val jsonrpc: Option[String] = getVersion()

    val method: Option[String] = getMethod()

    val params: Option[Collection[Any]] = getParams()

    val id: Option[Int] = getId()


    /**
    *  Secondary constructor.
    *
    *  @param json
    *           a valid json string
    */
    def this(json: String) = this (new BufferedReader(new StringReader(json)))



    /**
    *  Returns this request's parameters as an array.
    *  Is necessary for invoking methods via Java's reflection framework.
    *
    *  @return this request's parameters as an array
    */
    def parametersAsArray(): Array[AnyRef] = {
        val result = params match {
            case Some(x: List[AnyRef]) => x.toArray
            case _ => error("Only lists as parameters atm...")
        }
        result
    }




    def isSystemList = { method != None && method.get == "system.listMethods" }

    def hasParseErrors = { requestMap == None }
            
    def hasMissingParameters = { jsonrpc == None || method == None || params == None || id == None }



    /* --------------------------- */
    /* --- construction helpers -- */
    /* --------------------------- */


    /**
    * Turns the json string underlying this request's reader into a Map[String,Any].
    * 
    * @return a requestMap request map
    */
    private def parse(): Option[Map[String, Any]] = {
        val result = JSONParser.parseAll(JSONParser.obj, reader)
        if (result.successful) {
            Some(result.get)
        }
        else None
    }


    /**
    * Tries to retrieve the version of the request.
    * 
    * @return the version number or a runtime error if none exists
    */
    private def getVersion(): Option[String] = {
          if (requestMap != None) {
            requestMap.get.get("jsonrpc") match {
                case Some(version: String) => Some(version)
                case _ => None
            }
        }
        else None
    }

    /**
    * Tries to retrieve the method of the request.
    *
    * @return the method or a runtime error if none exists
    */
    private def getMethod(): Option[String] = {
        if (requestMap != None) {
            requestMap.get.get("method") match {
                case Some(method: String) => Some(method)
                case _ => None
            }
        }
        else None
    }

    /**
    * Tries to retrieve the parameters of the request.
    *
    * @return the parameters or a runtime error if none exist
    */
    private def getParams(): Option[Collection[Any]] = {
          if (requestMap != None) {
            requestMap.get.get("params") match {
                case Some(params: Collection[_]) => Some(params)
                case _ => None
            }
        }
        else None       
    }

    /**
    * Tries to retrieve the id of the request.
    *
    * @return id or a runtime error if none exists
    */
    private def getId(): Option[Int] = {
        if (requestMap != None) {
            requestMap.get.get("id") match {
                case Some(id: Int) => Some(id)
                case _ => None
            }
        }
        else None          
    }


}
