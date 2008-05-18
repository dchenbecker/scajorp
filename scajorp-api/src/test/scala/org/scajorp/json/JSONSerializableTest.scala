/*
 * JSONSerializableTest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before


class JSONSerializableTest {
    
    var jsonObject : JSONObject = _
  
    var jsonArray: JSONArray = _
    
    @Before
    def setUp() {
        jsonObject = new JSONObject
        jsonArray = new JSONArray
    }
    

    /* ----- JSON value tests in a JSONObject ----- */

    @Test
    def string() {      
        jsonObject += "name" -> "Dr. Cox"
        val result = jsonObject.toString()
        assertEquals("{\"name\":\"Dr. Cox\"}", result)
    }
  
    @Test
    def boolean() {      
        jsonObject += "married" -> true
        val result = jsonObject.toString()
        assertEquals("{\"married\":true}", result)
    }
  
    @Test
    def integer() {      
        jsonObject += "kids" -> 1
        val result = jsonObject.toString()
        assertEquals("{\"kids\":1}", result)
    }
    
    @Test
    def nul() {      
        jsonObject += "kids" -> null
        val result = jsonObject.toString()
        assertEquals("{\"kids\":null}", result)
    }
  
    @Test
    def fraction_float() {      
        jsonObject += "kids" -> 1.23f
        val result = jsonObject.toString()
        assertEquals("{\"kids\":1.23}", result)
    }
  
    @Test
    def fraction_double() {      
        jsonObject += "kids" -> 1.23d
        val result = jsonObject.toString()
        assertEquals("{\"kids\":1.23}", result)
    }
    
    
    /* ---- JSONArray ----*/
  
    @Test
    def array_allValues() {      
        jsonArray.addAll(List[Any]("Dr. Cox", true, 1, 1.23d, 1.23f))
        val result = jsonArray.toString()
        assertEquals("[\"Dr. Cox\",true,1,1.23,1.23]", result)
    }
    

    @Test
    def nested_object() {
        jsonObject += "kids" -> 5    
        val nestedObject = new JSONObject()
        nestedObject += "count" -> 5
        jsonObject += "grandkids" -> nestedObject
        val result = jsonObject.toString
        assertEquals("{\"grandkids\":{\"count\":5},\"kids\":5}", result)
    }
    
    @Test
    def nested_array() {
        jsonArray.addAll(List[String]("Barbara", "Fred"))
        val nestedArray = new JSONArray
        nestedArray.addAll(List[String]("Tom","Jones"))
        jsonArray += nestedArray
        val result = jsonArray.toString
        assertEquals("[\"Barbara\",\"Fred\",[\"Tom\",\"Jones\"]]", result)
    }
}
