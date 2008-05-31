/*
 * ScajorpServlet.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.http

import java.io.BufferedReader
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scajorp.json.JSONRequest
import org.scajorp.JSONParser



class ScajorpServlet extends HttpServlet{
    
  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
      
      val jsonRequest = parseRequest(req)
      
      //testing, will be removed later
      val app = new ScajorpApplication()
      app.register("calculator", classOf[Calculator])
      
      val result = app.execute(jsonRequest)
      
      val response = "{\"jsonrpc\":\"2.0\",\"result\":"+result+",\"id\":1}"
      
      resp.getWriter().write(response)
      resp.getWriter().flush()
  }
  
  def parseRequest(req: HttpServletRequest): JSONRequest = {
      val requestMap = JSONParser.parseAll(JSONParser.obj,req.getReader()).get
      return newRequest(requestMap)
  }
  
  // todo refactor to JSONRequest constructor
   private def newRequest(map: Map[String,Any]): JSONRequest = {              
       val version = map.get("jsonrpc") match {
          case Some(v: String) => v
          case _ => error("No version specified for request. Example: {\"jsonrpc\": \"2.0\"...}")
        }
        val method = map.get("method") match {
            case Some(m: String) => m
            case _ => error("No method specified for request. Example: {...\"method\": \"subtract\"...}")
        }
        val params = map.get("params") match {
            case Some(col: Collection[_]) => col
            case _ => error("No params specified for request. If no parameters are needed, an empty list must be supplied. Example: {...\"params\": [42, 23]...}")                
        }
        val id = map.get("id") match {
            case Some(i : Int) => i
            case Some(f: Float) => f.toInt
            case Some(d: Double) => d.toInt
            case _ => error("No id specified for request. Example: ...\"id\": 1}")
        }          
        new JSONRequest(version, method, params, id)
  }
      
  
  
  
}
