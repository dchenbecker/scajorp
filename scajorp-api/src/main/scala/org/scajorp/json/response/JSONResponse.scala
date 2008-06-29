package org.scajorp.json.response

import java.io.Writer

/** 
* Created by IntelliJ IDEA.
* User: waven
* Date: 28-Jun-2008
* Time: 13:43:47
* To change this template use File | Settings | File Templates.
*/
abstract class JSONResponse

case class ValidResponse(val jsonrpc: String, val result: Any, val id: Int) extends JSONResponse

case class ErrorResponse(val jsonrpc: String, val error: Any, val id: Int) extends JSONResponse

case class JSONError(val code: Int, val message: String)

