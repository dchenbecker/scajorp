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
import org.scajorp.json.JSONResponse





class ScajorpServlet extends HttpServlet{
    
  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
      
      val jsonRequest = new JSONRequest(req.getReader())

      //testing, will be removed later
      val app = new ScajorpApplication()
      app.register("calculator", classOf[Calculator])
      //

      val jsonResponse = app.execute(jsonRequest)
      
      jsonResponse.toWriter(resp.getWriter())
      
  }
  

    
}
