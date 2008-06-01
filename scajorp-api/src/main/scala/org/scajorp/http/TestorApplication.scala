package org.scajorp.http

/**
* Created by IntelliJ IDEA.
* User: marco
* Date: Jun 1, 2008
* Time: 11:27:02 AM
* To change this template use File | Settings | File Templates.
*/

class TestorApplication extends ScajorpApplication {

    override def init() {
        register("calculator", classOf[Calculator])
    }

}