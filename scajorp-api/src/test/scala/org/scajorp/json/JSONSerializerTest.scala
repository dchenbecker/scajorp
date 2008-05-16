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
    
    @Test
    def set_strings() {
        val names = Set[String]("John", "Dick","Jack")
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
    def map_integers() {
        val names = Map[String,Int]("first" -> 1, "last" -> 2)
        val result = JSONSerializer.serialize(names)
        assertEquals("{\"first\":1,\"last\":2}",result)
    }
   
    

   

   

}
