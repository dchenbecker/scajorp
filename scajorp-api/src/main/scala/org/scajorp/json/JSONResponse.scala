package org.scajorp.json

import java.io.Writer

/**
* Scala representation of a json response string, which offers a couple of convenience
* methods. Knows how to turn itself to a json String or output itself to a Writer.
*
* @param jsonrpc   a rpc version number
* @param result    a result of type Any, i.e. AnyVal and AnyRef
* @param id        a rpc id corresponding to a jsonrequest
*
* @author Marco Behler
*/
class JSONResponse(val jsonrpc: String, val result: Any, val id: Int) {


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