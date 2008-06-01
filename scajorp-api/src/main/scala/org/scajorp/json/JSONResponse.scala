package org.scajorp.json

import java.io.Writer

/**
*
* Representation of a json response string. Example:
*
* {"jsonrpc": "2.0", "result": 19, "id": 1}
*
* @param jsonrpc   a rpc version number
* @param result    a result of type Any, i.e. AnyVal and AnyRef
* @param id        a rpc id corresponding to a jsonrequest
*
* @author Marco Behler
*/
class JSONResponse(val jsonrpc: String, val result: Any, val id: Int) {

    /**
    * First, serializes this response's data into a valid json string.
    * Afterwards, writes the string to a writer and flushes it.         
    */
    def toWriter(writer: Writer) {
        val response = JSONSerializer.serialize(this)
        writer.write(response)
        writer.flush()
    }
}