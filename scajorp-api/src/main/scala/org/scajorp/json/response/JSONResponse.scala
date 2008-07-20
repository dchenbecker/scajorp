package org.scajorp.json.response

import java.io.Writer

/**
* JSONResponse case classes.
* 
* @author Marco Behler
*/
abstract class JSONResponse  {

    /**
    * Writes this response to a {@link Writer} and flushes
    * the writer immediately afterwards. 
    */
    def toWriter(writer: Writer, prettyPrint : Boolean) {
        JSONSerializer.prettyPrint = prettyPrint
        writer.write(JSONSerializer.serialize(this))
        writer.flush()
    }
}

case class ValidResponse(val jsonrpc: String, val result: Any, val id: Int) extends JSONResponse

case class ErrorResponse(val jsonrpc: String, val error: Any, val id: Int) extends JSONResponse

case class JSONError(val code: Int, val message: String)


