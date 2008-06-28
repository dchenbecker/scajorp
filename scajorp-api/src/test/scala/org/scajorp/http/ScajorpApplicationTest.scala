/*
 * ScajorpApplicationTest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.http

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

import org.scajorp.json.request.JSONRequest
import org.scajorp.json.response.{ValidResponse,ErrorResponse}



class ScajorpApplicationTest {
      
    
    val application = new BogusApplication()

    val calculateRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"calculator.sum\", \"params\": [4, 5], \"id\": 5}")
    
    val systemListRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"system.listMethods\", \"params\": [], \"id\": 1}")

    val invalidJsonRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"m}ethod\":.:.. \"calculator.sum\", \"params\": [10, 5], \"id\": 5}")
    
       
    @Before
    def setUp() {    
        assertEquals(true, application.methodRegistry.contains("calculator.sum"))
    }
        
    @Test
    def calculate() {
        val response = application.execute(calculateRequest)
        assertEquals(response, ValidResponse("2.0", 9, 5));
    }

    @Test
    def listMethods() {
        val response = application.execute(systemListRequest)       
        assertEquals(response, ValidResponse("2.0", List("calculator.sum"), 1));
    }

    /*@Test
    def invalidJSON() {
        val response = application.execute(invalidJsonRequest)
        assertEquals(response, ErrorResponse("2.0", Map("code" -> -32700, "message" -> "Parse Error"), 1));
    }*/
        
  
    

    
}
