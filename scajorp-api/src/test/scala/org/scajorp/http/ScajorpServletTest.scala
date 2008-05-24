/*
 * ScajorpServlet.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.http

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

import java.io.ByteArrayOutputStream
import java.io.PrintWriter

class ScajorpServletTest {
    
    val scajorpServlet = new ScajorpServlet()
    
    val os = new MockServletOutputStream(new ByteArrayOutputStream())
    
    // already hardcoded into mockhttp servlet request
    // val request = {\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}
    
    val response = "{\"jsonrpc\":\"2.0\",\"result\":19,\"id\":1}"
    
    @Test
    def simulateRequest() {
        val req = new MockHttpServletRequest
        val resp = new MockHttpServletResponse(new PrintWriter(os), os)
        scajorpServlet.doPost(req, resp)        
        assertEquals(response, os.os.toString())
    }
    
}
