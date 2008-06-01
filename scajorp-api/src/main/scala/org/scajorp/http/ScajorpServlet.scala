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



class ScajorpServlet extends HttpServlet {


    val applicationFactory: TApplicationFactory = new ContextParamApplicationFactory()
    
    
    override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        
        val jsonRequest = new JSONRequest(req.getReader())

        val application = applicationFactory.createApplication(getServletConfig())
        
        val jsonResponse = application.execute(jsonRequest)

        jsonResponse.toWriter(resp.getWriter())

    }


}
