package org.scajorp.json.response

import java.io.Writer

/** 
* Created by IntelliJ IDEA.
* User: waven
* Date: 28-Jun-2008
* Time: 13:43:47
* To change this template use File | Settings | File Templates.
*/
abstract class JSONResponse {
    
     def toJSON(prettyPrint: Boolean) = {
        if (prettyPrint) JSONSerializer.prettyPrint = true
        JSONSerializer.serialize(this)
    }

    /**
    * Transforms this object to a valid json string and outputs it to a writer.
    * Flushes the writer afterwards!
    */
    def toWriter(writer: Writer, prettyPrint: Boolean) {
        writer.write(this.toJSON(prettyPrint))
        writer.flush()
    }
}

case class ErrorResponse(val jsonrpc: String, val error: Any, val id: Int) extends JSONResponse
case class ValidResponse(val jsonrpc: String, val result: Any, val id: Int) extends JSONResponse