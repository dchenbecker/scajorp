package org.scajorp.http

import java.io.BufferedReader
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scajorp.json.JSONRequest
import org.scajorp.json.JSONResponse


/**
* A simple servlet which can handle json requests via GET and POST.
* This class is not meant to be subclassed or modified. 
* Subclass {@link ScajorpApplication} instead.
* 
* @author Marco Behler
*/
class ScajorpServlet extends HttpServlet {

    /**
    * The factory used to create acdpplication objects
    */
    val applicationFactory: TApplicationFactory = new ContextParamApplicationFactory()
    

    override def init() {
           val application = applicationFactory.createApplication(getServletConfig())
           application.developmentInfo()
    }

    /**
    * POST-request handler    
    */
    override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        
        val jsonRequest = new JSONRequest(req.getReader())

        val application = applicationFactory.createApplication(getServletConfig())
        
        val jsonResponse = application.execute(jsonRequest)

        jsonResponse.toWriter(resp.getWriter(), application.prettyPrint)

    }


}
