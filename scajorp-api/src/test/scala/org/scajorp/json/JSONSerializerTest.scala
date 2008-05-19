package org.scajorp.json


import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._
import org.scajorp.dummies._

class JSONSerializerTest {
    
    var rambo = new JohnRambo()
       
    /* ----- (P)lain (O)ld (S)cala (O)bject tests ----- */
        
    @Test
    def poso() {
        val result = JSONSerializer.serialize(rambo);
        assertEquals("{\"age\":66,\"job\":\"warrior\",\"jsonClass\":\"org.scajorp.dummies.JohnRambo\",\"married\":false}", result)
    } 
    
    
    /* ----- Collection tests ----- */
    
    @Test
    def array_strings() {
        val names = Array[String]("John", "Dick","Jack")
        val result = JSONSerializer.serialize(names)
        assertEquals("[\"John\",\"Dick\",\"Jack\"]",result)
    }       
    
    @Test
    def list_strings() {
        val names = Array[String]("John", "Dick","Jack")
        val result = JSONSerializer.serialize(names)
        assertEquals("[\"John\",\"Dick\",\"Jack\"]",result)
    }
    
    @Test
    def map_strings() {
        val names = Map[String,String]("first" -> "Rambo", "last" -> "Wayne")
        val result = JSONSerializer.serialize(names)
        assertEquals("{\"first\":\"Rambo\",\"last\":\"Wayne\"}",result)
    }
    
    @Test
    def set_strings() {
        val names = Set[String]("John", "Dick","Jack")
        val result = JSONSerializer.serialize(names)
        assertEquals("[\"John\",\"Dick\",\"Jack\"]",result)
    }
    

   
    
    /* ---- Nested collection tests ----- */
    
    @Test
    def nested_maps() {
        val john = Map[String,String] ("name" -> "John")
        val eliott = Map[String,Any] ("name" -> "Eliott", "husband" -> john)
        val result = JSONSerializer.serialize(eliott)
        assertEquals("{\"husband\":{\"name\":\"John\"},\"name\":\"Eliott\"}",result)
    }
    
    @Test
    def nested_lists() {
        val list1 = List[String]("John", "Eliott")
        val list2 = List[Any]("friends", list1)        
        val result = JSONSerializer.serialize(list2)
        assertEquals("[\"friends\",[\"John\",\"Eliott\"]]",result)
    }
 
    
    @Test
    def nested_mixed {
        val friends = List[String]("Turk", "Cox")
        val john = Map[String,Any] ("name" -> "John", "friends" -> friends)
        val result = JSONSerializer.serialize(john)
        assertEquals("{\"friends\":[\"Turk\",\"Cox\"],\"name\":\"John\"}",result)
    }


}
