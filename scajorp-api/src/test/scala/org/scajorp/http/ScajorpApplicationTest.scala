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
import org.scajorp.json.response.ValidResponse
import java.io.BufferedReader
import java.io.StringReader

class ScajorpApplicationTest {
      
    
    val application = new BogusApplication()

    val jsonRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"calculator.sum\", \"params\": [4, 5], \"id\": 5}")
    
    val systemListRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"system.listMethods\", \"params\": [], \"id\": 1}")

    val invalidJsonRequest = new JSONRequest("{\"jsonrpc\": \"2.0\", \"method\": \"calculator.sum\", \"params\": [10, 9,6], \"id\": 5}")
    

       
    @Before
    def setUp() {    
        assertEquals(true, application.methodRegistry.contains("calculator.sum"))
    }
        
    @Test
    def execute() {
        val response = application.execute(jsonRequest)
        assertEquals(response, ValidResponse("2.0", 9, 5));
    }
        
    @Test{val expected = classOf[IllegalArgumentException]}
    def execute_wrongParams() {        
        application.execute(invalidJsonRequest)         
    }

    @Test
    def listMethods() {
        val response = application.execute(systemListRequest)
        assertEquals("{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[\"calculator.sum\"]}", response.toJSON(false))
    }

    
}
