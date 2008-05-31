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

class ScajorpApplicationTest {
      
    
    val application = new ScajorpApplication()
    
    val jsonRequest = new JSONRequest("2.0", "calculator.sum", List(4,5), 1 )
    
    val invalidJsonRequest = new JSONRequest("2.0", "calculator.sum", List(4,5,6), 1)

    // TODO makeover
    
   /* @Before
    def setUp() {
        application.register("calculator", classOf[Calculator])
        assertEquals(true, application.methodRegistry.contains("calculator.sum"))
    }
        
    @Test
    def execute() {
        application.register("calculator", classOf[Calculator])
        val result = application.execute(jsonRequest)
        assertEquals(9, result)
    }
    
    @Test{val expected = classOf[IllegalArgumentException]}
    def execute_wrongParams() {
        application.register("calculator", classOf[Calculator])
        application.execute(invalidJsonRequest)         
    }*/

    
}
