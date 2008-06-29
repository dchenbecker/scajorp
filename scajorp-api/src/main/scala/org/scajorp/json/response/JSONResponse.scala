package org.scajorp.json.response

import java.io.Writer

/**
* Response and error case classes.
* 
* @author Marco Behler
*/
abstract class JSONResponse

case class ValidResponse(val jsonrpc: String, val result: Any, val id: Int) extends JSONResponse

case class ErrorResponse(val jsonrpc: String, val error: Any, val id: Int) extends JSONResponse

case class JSONError(val code: Int, val message: String)


