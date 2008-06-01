package org.scajorp.http

import javax.servlet.ServletConfig

/**
 * A factory interface used by scajorp's servlet to create application objects.
 *
 * @credit This idea is blatantly copied from Wicket (http://wicket.apache.org/)
 * 
 * @author Marco Behler
 */

trait TApplicationFactory {

    /**
     * Creates an application object
     *
     * @param config    scajorp's servlet config
     *
     * @return application object instance
     */
    def createApplication(config: ServletConfig): ScajorpApplication 



}