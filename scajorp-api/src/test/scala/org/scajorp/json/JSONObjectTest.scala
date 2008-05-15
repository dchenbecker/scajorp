/*
 * JSONObject.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.json

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

class JSONObjectTest {
    
    @Test
    def strings_one() {        
        val jsonObject = new JSONObject()
        jsonObject += ("name" -> "Johnny")
        val result = jsonObject.toString()
        assertEquals("{\"name\":\"Johnny\"}", result)                
    }
    
    @Test
    def strings_several() {        
        val jsonObject = new JSONObject()
        jsonObject += ("name" -> "Johnny")
        jsonObject += ("gender" -> "male")
        jsonObject += ("tall" -> "yes")
        val result = jsonObject.toString()
        assertEquals("{\"gender\":\"male\",\"tall\":\"yes\",\"name\":\"Johnny\"}", result)                
    }
    
    @Test
    def primitives_all() {
        val jsonObject = new JSONObject()
        jsonObject += ("name" -> "Johnny")
        jsonObject += ("age" -> 21)
        jsonObject += ("tall" -> true)
        jsonObject += ("mutti" -> null)
        val result = jsonObject.toString()
        assertEquals("{\"age\":21,\"mutti\":null,\"tall\":true,\"name\":\"Johnny\"}", result)                
    }
    
    @Test
    def nestedObjects() {
        val jsonObject = new JSONObject()
        jsonObject += ("name" -> "Johnny")
        val nestedObject = new JSONObject()
        nestedObject += ("age" -> 21)
        nestedObject += ("name" -> "Picard")
        jsonObject += ("commander" -> nestedObject)
        val result = jsonObject.toString()
        assertEquals("{\"commander\":{\"age\":21,\"name\":\"Picard\"},\"name\":\"Johnny\"}", result)                
    }
}
