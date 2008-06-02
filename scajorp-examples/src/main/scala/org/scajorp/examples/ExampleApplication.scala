package org.scajorp.examples

import org.scajorp.http.ScajorpApplication

/** 
* Created by IntelliJ IDEA.
* User: marco
* Date: Jun 1, 2008
* Time: 5:55:08 PM
* To change this template use File | Settings | File Templates.
*/

class BogusApplication extends ScajorpApplication {

    override def init() {
        register("calculator", classOf[Calculator])
        register("blogEntryService", classOf[BlogEntryService])
    }

    

}