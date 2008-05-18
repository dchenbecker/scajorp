package org.scajorp.json


import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._
import org.scajorp.dummies._

class JSONSerializerTest {

    var stringDummy = new StringDummy()
    
    var integerDummy = new IntegerDummy()
    
    var booleanDummy = new BooleanDummy()
    
    var person = new PersonDummy()        
    
    val nested = new NestedDummy()

    
    /* ----- (P)lain (O)ld (S)cala (O)bject tests ----- */
    
    @Test
    def object_withStrings() {        
        stringDummy.firstName = "John"
        stringDummy.lastName = "Rambo"        
        val result = JSONSerializer.serialize(stringDummy);
        assertEquals("{\"firstName\":\"John\",\"jsonClass\":\"org.scajorp.dummies.StringDummy\",\"lastName\":\"Rambo\"}", result)
    }
    
    @Test
    def object_withIntegers() {
        integerDummy.age = 21
        val result = JSONSerializer.serialize(integerDummy);
        assertEquals("{\"age\":21,\"jsonClass\":\"org.scajorp.dummies.IntegerDummy\"}", result)
    }
    
    @Test
    def object_withBooleans() {
        booleanDummy.sad = true
        booleanDummy.happy = false
        val result = JSONSerializer.serialize(booleanDummy);
        assertEquals("{\"happy\":false,\"jsonClass\":\"org.scajorp.dummies.BooleanDummy\",\"sad\":true}", result)
    }
    
    @Test
    def person_poso() {
        person.age = 21
        person.firstName = "John"
        person.lastName = "Rambo"
        person.married = true        
        val result = JSONSerializer.serialize(person);
        assertEquals("{\"age\":21,\"firstName\":\"John\",\"jsonClass\":\"org.scajorp.dummies.PersonDummy\",\"lastName\":\"Rambo\",\"married\":true}", result)
    } 
    
    @Test
    def person_posoNested() {
        val result = JSONSerializer.serialize(nested)
        println("-----" + result)
        assertEquals("{\"age\":21,\"jsonClass\":\"org.scajorp.dummies.NestedDummy\",\"name\":\"John\",\"person\":{\"age\":null,\"firstName\":null,\"jsonClass\":\"org.scajorp.dummies.PersonDummy\",\"lastName\":null,\"married\":false}}", result)
    }
    


    /* ----- Collection tests ----- */
    
    @Test
    def array_strings() {
        val names = Array[String]("John", "Dick","Jack")
        val result = JSONSerializer.serialize(names)
        assertEquals("[\"John\",\"Dick\",\"Jack\"]",result)
    }
    
    @Test
    def array_boolean() {
        val names = Array[java.lang.Boolean](true, false, true)
        val result = JSONSerializer.serialize(names)
        assertEquals("[true,false,true]",result)
    }
    
    @Test
    def array_integer() {
        val names = Array[Integer](1,2,3)
        val result = JSONSerializer.serialize(names)
        assertEquals("[1,2,3]",result)
    }
    
    @Test
    def list_strings() {
        val names = Array[String]("John", "Dick","Jack")
        val result = JSONSerializer.serialize(names)
        assertEquals("[\"John\",\"Dick\",\"Jack\"]",result)
    }
    
    @Test
    def list_integer() {
        val names = Array[Integer](1, 2,3)
        val result = JSONSerializer.serialize(names)
        assertEquals("[1,2,3]",result)
    }

    // TODO
//    @Test
//    def set_strings() {
//        val names = Set[String]("John", "Dick","Jack")
//        val result = JSONSerializer.serialize(names)
//        assertEquals("[\"John\",\"Dick\",\"Jack\"]",result)
//    }
//    
    @Test
    def map_strings() {
        val names = Map[String,String]("first" -> "Rambo", "last" -> "Wayne")
        val result = JSONSerializer.serialize(names)
        assertEquals("{\"first\":\"Rambo\",\"last\":\"Wayne\"}",result)
    }
    
    @Test
    def map_integers() {
        val names = Map[String,Int]("first" -> 1, "last" -> 2)
        val result = JSONSerializer.serialize(names)
        assertEquals("{\"first\":1,\"last\":2}",result)
    }
   
    
    /* ---- Nested tests ----- */
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
    def nested_arrays() {
        val array1 = Array[String]("John", "Eliott")
        val array2 = Array[AnyRef]("friends", array1)        
        val result = JSONSerializer.serialize(array2)
        assertEquals("[\"friends\",[\"John\",\"Eliott\"]]",result)
    }
    
    @Test
    def nested_mixed_obj_first() {
        val friends = List[String]("Turk", "Cox")
        val john = Map[String,Any] ("name" -> "John", "friends" -> friends)
        val result = JSONSerializer.serialize(john)
        assertEquals("{\"friends\":[\"Turk\",\"Cox\"],\"name\":\"John\"}",result)
    }
    
    @Test
    def nested_mixed_lists_first() {
      val john = Map[String,Any] ("name" -> "John")
      val friends = List[Any]("Turk", john)
      val result = JSONSerializer.serialize(friends)
      assertEquals("[\"Turk\",{\"name\":\"John\"}]",result)
    }
    
    @Test
    def list_with_pojo() {        
        val friends = List[Any](nested)
        val result = JSONSerializer.serialize(friends)
        assertEquals("[{\"age\":21,\"jsonClass\":\"org.scajorp.dummies.NestedDummy\",\"name\":\"John\",\"person\":{\"age\":null,\"firstName\":null,\"jsonClass\":\"org.scajorp.dummies.PersonDummy\",\"lastName\":null,\"married\":false}}]",result)
    }
  
   

}
