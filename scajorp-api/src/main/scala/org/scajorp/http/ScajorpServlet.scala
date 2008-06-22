package org.scajorp.http

import java.io.BufferedReader
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletConfig

import org.scajorp.json.JSONRequest
import org.scajorp.json.JSONResponse

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* A simple servlet which can handle json requests via GET and POST.
* This class is not meant to be subclassed or modified. 
* Subclass {@link ScajorpApplication} instead.
* 
* @author Marco Behler
*/
class ScajorpServlet extends HttpServlet {

    val logger = LoggerFactory.getLogger(classOf[ScajorpServlet])



    override def init() {
           val application = ScajorpServlet.createApplication(getServletConfig())
           application.developmentInfo()
    }

    override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        
        val jsonRequest = new JSONRequest(req.getReader())

        val application = ScajorpServlet.createApplication(getServletConfig())
        
        val jsonResponse = application.execute(jsonRequest)        

        jsonResponse.toWriter(resp.getWriter(), application.prettyPrint)

    }


}

object ScajorpServlet {

    /**
    * context parameter name that must contain the class name of the application
    */
    val application_class_param = "applicationClassName";

    def createApplication(config: ServletConfig): ScajorpApplication = {
        val applicationClassName = config.getInitParameter(application_class_param);
        val application = applicationClassName match {
            case className: String => createApplication(className)
            case null => error("servlet init param [" + application_class_param + "] is missing.")
        }
        application
    }

    private def createApplication(className: String): ScajorpApplication = {
        val obj = Class.forName(className).newInstance()
        val application = if (obj.isInstanceOf[ScajorpApplication]) {
            obj.asInstanceOf[ScajorpApplication]
        }
        else {
            error("Unable to create Scajorp Application[=" + className +"]")
        }
        application
    }
    
}