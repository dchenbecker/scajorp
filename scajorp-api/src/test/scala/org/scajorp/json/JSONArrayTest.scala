/*
 * JSONArray.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._


class JSONArrayTest {
    @Test
    def primitives_all() {
        val jsonArray = new JSONArray()
        jsonArray += "Johnny"
        jsonArray += 21
        jsonArray += true
        jsonArray += null
        val result = jsonArray.toString()
        assertEquals("[\"Johnny\",21,true,null]", result)                
    }
    
    @Test
    def complex_nested() {
        val jsonArray = new JSONArray()
        jsonArray += "Johnny"
        jsonArray += 21
     
        val jsonObject = new JSONObject()
        jsonObject += ("name" -> "Johnny")
        jsonObject += ("gender" -> "male")
        jsonObject += ("array" -> jsonArray)
        
        
        val result = jsonObject.toString()
        assertEquals("{\"array\":[\"Johnny\",21],\"gender\":\"male\",\"name\":\"Johnny\"}", result)

    }
}
