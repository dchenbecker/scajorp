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
      
      //testing, will be removed later
      val app = new ScajorpApplication()
      app.register("calculator", classOf[Calculator])
      
      val result = app.execute(jsonRequest)
      
      val response = "{\"jsonrpc\":\"2.0\",\"result\":"+result+",\"id\":1}"
      
      resp.getWriter().write(response)
      resp.getWriter().flush()
  }
  
  def parseRequest(req: HttpServletRequest): JSONRequest = {
    JSONParser.parseAll(req.getReader()) match {
      case Some(result : Map[String,Any]) =>
        println("result: " + result)
        result.keys.foreach(key => println("key \"" + key + "\", val = " + result(key)))
	println("result.contains(\"method\"): " + result.get("method"))
	println("result.contains(\"id\"):" + result.get("id"))
      
	//new JSONRequest(version, method, params, id)
	new JSONRequest("2.0", "calculator.sum", List(10,9), 1)
      case _ => new JSONRequest("2.0", "invalid!!!", List(), 0)
    }
  }
      
  
  
  
}
