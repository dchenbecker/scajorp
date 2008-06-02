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
import org.scajorp.json.JSONRequest
import java.io.BufferedReader
import java.io.StringReader

class ScajorpApplicationTest {
      
    
    val application = new BogusApplication()


    val requestReader = new BufferedReader(new StringReader("{\"jsonrpc\": \"2.0\", \"method\": \"calculator.sum\", \"params\": [4, 5], \"id\": 5}"))

    val systemListRequest = new JSONRequest(new BufferedReader(new StringReader("{\"jsonrpc\": \"2.0\", \"method\": \"system.listMethods\", \"params\": [], \"id\": 1}")))

    val jsonRequest = new JSONRequest(requestReader)
    
    val invalidJsonRequest = new JSONRequest(new BufferedReader(new StringReader("{\"jsonrpc\": \"2.0\", \"method\": \"calculator.sum\", \"params\": [10, 9,6], \"id\": 5}")))
       
    @Before
    def setUp() {    
        assertEquals(true, application.methodRegistry.contains("calculator.sum"))
    }
        
    @Test
    def execute() {
        val response = application.execute(jsonRequest)
        assertEquals(9, response.result)
    }
    
    @Test{val expected = classOf[IllegalArgumentException]}
    def execute_wrongParams() {        
        application.execute(invalidJsonRequest)         
    }

    @Test
    def listMethods() {
        val response = application.execute(systemListRequest)
        assertEquals("{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":[\"calculator.sum\"]}", response.toJSONString())
    }

    
}
