package org.scajorp.json


import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._
import org.scajorp.dummies._
import org.scajorp.dummies.circular._

class JSONSerializerTest {
    
    val rambo = new JohnRambo()
    
    val drCox = new DrCox()
    
   // val teacher = new Teacher("Jack", new Principal("God"))
    val principal = new Principal("God")
       
    /* ----- (P)lain (O)ld (S)cala (O)bject tests ----- */
        
    @Test
    def poso() {
        val result = JSONSerializer.serialize(rambo);
        assertEquals("{\"age\":66,\"job\":\"warrior\",\"jsonClass\":\"org.scajorp.dummies.JohnRambo\",\"married\":false}", result)
    } 
    
    @Test
    def poso_complex() {
        val result = JSONSerializer.serialize(drCox);
        assertEquals("{\"alphaMale\":true,\"girlfriends\":[{\"jsonClass\":\"org.scajorp.dummies.Friend\",\"name\":\"Carla\"},{\"jsonClass\":\"org.scajorp.dummies.Friend\",\"name\":\"JD\"},{\"jsonClass\":\"org.scajorp.dummies.Friend\",\"name\":\"Janitor\"}],\"jsonClass\":\"org.scajorp.dummies.DrCox\"}", result);
    }
    
    @Test
    def poso_circular_no_throw() {
        val result = JSONSerializer.serialize(principal)        
        assertEquals("{\"jsonClass\":\"org.scajorp.dummies.circular.Principal\",\"name\":\"God\"," +
                     "\"underlings\":[" +
                           "{\"jsonClass\":\"org.scajorp.dummies.circular.Teacher\",\"name\":\"Jack\",\"student\":{\"jsonClass\":\"org.scajorp.dummies.circular.Student\",\"name\":\"Jack Bauer\"}}," +
                           "{\"jsonClass\":\"org.scajorp.dummies.circular.Teacher\",\"name\":\"John\",\"student\":{\"jsonClass\":\"org.scajorp.dummies.circular.Student\",\"name\":\"Jack Bauer\"}}" +
                           "]}", result)
    }

    @Test
    def poso_circular_throw() {
      try {
	JSONSerializer.throwOnCircularRefs = true
        val result = JSONSerializer.serialize(principal)
	fail("Exception should have been thrown on circular reference")
      } catch {
	case cre : CircularReferenceException => "test" //nop
	case e : Exception => fail("Unknown exception: " + e.getMessage())
      } finally {
	JSONSerializer.throwOnCircularRefs = false
      }
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
