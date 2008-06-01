package org.scajorp.http

import javax.servlet.ServletConfig

/**
 * Factory that creates application objects based on the class name specified in the
 * {@link ContextParamWebApplicationFactory#application_class_param} context variable.
 *
 * @credit This idea is blatantly copied from Wicket (http://wicket.apache.org/)
 * 
 * @author Marco Behler
 */


class ContextParamApplicationFactory extends TApplicationFactory
{
	/**
	 * context parameter name that must contain the class name of the application
	 */
	val application_class_param = "applicationClassName";


    override def createApplication(config: ServletConfig): ScajorpApplication = {
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
