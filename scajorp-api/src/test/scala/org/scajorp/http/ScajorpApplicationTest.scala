package org.scajorp.http

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import org.scajorp.json.request.JSONRequest
import org.scajorp.json.response.{ValidResponse,ErrorResponse,JSONError}

/*
 * Tests concerning ScajorpApplication's execute() method
 * and the resulting JSONResponses.
 *
 * @author Marco Behler
 * 
 */
class ScajorpApplicationTest {
          
    val application = new BogusApplication()

    val calculateRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"calculator.sum\", \"params\": [4, 5], \"id\": 5}")
    
    val systemListRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"system.listMethods\", \"params\": [], \"id\": 1}")

    val invalidJsonRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"m}ethod\":.:.. \"calculator.sum\", \"params\": [10, 5], \"id\": 5}")

    val missingVersionRequest = new JSONRequest("{\"method\": \"calculator.sum\", \"params\": [4, 5], \"id\": 5}")

    val methodNotFoundRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"not.existing\", \"params\": [4, 5], \"id\": 5}")

    val invalidParamsRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"calculator.sum\", \"params\": [4, 5, 9], \"id\": 5}")

       
    @Before
    def setUp() {    
        assertEquals(true, application.methodRegistry.contains("calculator.sum"))
    }
        
    @Test
    def calculate() {
        val response = application.execute(calculateRequest)
        assertEquals(ValidResponse("2.0", 9, 5), response);
    }

    @Test
    def listMethods() {
        val response = application.execute(systemListRequest)       
        assertEquals(ValidResponse("2.0", List("calculator.sum"), 1), response);
    }

    @Test
    def invalidJSON() {
        val response = application.execute(invalidJsonRequest)        
        assertEquals(ErrorResponse("2.0", JSONError(-32700,"Parse Error"), -1), response)
    }

    @Test
    def missingVersion() {
        val response = application.execute(missingVersionRequest)
        assertEquals(ErrorResponse("2.0", JSONError(-32600,"Invalid Request"), -1), response)
    }

    @Test
    def methodNotFound() {
        val response = application.execute(methodNotFoundRequest)
        assertEquals(ErrorResponse("2.0", JSONError(-32601,"Method not found"), 5), response)
    }

    @Test
    def invalidParams() {
        val response = application.execute(invalidParamsRequest)        
        assertEquals(ErrorResponse("2.0", JSONError(-32602,"Invalid params"), 5), response)
    }
        
  
    


    
}
