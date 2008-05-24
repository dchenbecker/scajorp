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
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.JavaTokenParsers

class ScajorpServlet extends HttpServlet{
    
  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
      
      val jsonRequest = parseRequest(req)
      
//      val app = new ScajorpApplication()
//      app.register("calculator", classOf[Calculator])
//      
//      val result = app.execute(jsonRequest)
      
      val response = "{\"jsonrpc\":\"2.0\",\"result\":"+19+",\"id\":1}"
      
      resp.getWriter().write(response)
      resp.getWriter().flush()
  }
  
  def parseRequest(req: HttpServletRequest): JSONRequest = {
      val versionKey = "jsonrpc"
      val methodKey = "method"
      val paramsKey = "params"
      val idKey = "id"
      
      val jsonParser = new JSONParser()      
      val parsedResult = jsonParser.parseAll(jsonParser.obj, req.getReader())      
      
      //WTF!!!
      val result: scala.collection.Map[String, Any] = parsedResult.get 
      println("result: " + result)
       println("result.contains(\"method\"): " + result("method"))
      val version: String = result.get(versionKey) match {
          case Some(s: String) => s
          case None => error("Version is not a String")
      }
      val method: String = result.get(methodKey) match {
          case Some(s: String) => s
          case None => error("Method is not a String")
      }
      val params: Collection[Any] = result(paramsKey) match {
          case Some(col: Collection[Any]) => col
          case None => error("Params is not a Collection")
      }
      val id: Int = result(idKey) match {
          case Some(i: Int) => i
          case None => error("Id is not an int")
      }
      new JSONRequest(version, method, params, id)
  }
      
  
  
  
}
