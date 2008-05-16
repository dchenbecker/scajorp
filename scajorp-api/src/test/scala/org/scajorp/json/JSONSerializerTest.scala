package org.scajorp.json


import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._
import org.scajorp.dummies._

class JSONSerializerTest {

    var stringDummy: StringDummy = new StringDummy()
    
    var integerDummy: IntegerDummy = new IntegerDummy()
    
    var booleanDummy: BooleanDummy = new BooleanDummy()
    
    var person: PersonDummy = new PersonDummy()
    
    val nested = new NestedDummy()

    @Before
    def setUp():Unit = {
        
    }


    @Test
    def stringObject() {        
        stringDummy.firstName = "John"
        stringDummy.lastName = "Rambo"        
        val result = JSONSerializer.serialize(stringDummy);
        assertEquals("{\"firstName\":\"John\",\"jsonClass\":\"org.scajorp.dummies.StringDummy\",\"lastName\":\"Rambo\"}", result)
    }
    
    @Test
    def integerObject() {
        integerDummy.age = 21
        val result = JSONSerializer.serialize(integerDummy);
        assertEquals("{\"age\":21,\"jsonClass\":\"org.scajorp.dummies.IntegerDummy\"}", result)
    }
    
    @Test
    def booleanObject() {
        booleanDummy.sad = true
        booleanDummy.happy = false
        val result = JSONSerializer.serialize(booleanDummy);
        assertEquals("{\"happy\":false,\"jsonClass\":\"org.scajorp.dummies.BooleanDummy\",\"sad\":true}", result)
    }
    
    @Test
    def person_allPrimitives() {
        person.age = 21
        person.firstName = "John"
        person.lastName = "Rambo"
        person.married = true        
        val result = JSONSerializer.serialize(person);
        assertEquals("{\"age\":21,\"firstName\":\"John\",\"jsonClass\":\"org.scajorp.dummies.PersonDummy\",\"lastName\":\"Rambo\",\"married\":true}", result)
    } 
    
    @Test
    def person_Nested() {
        val result = JSONSerializer.serialize(nested)
        println("-----" + result)
        assertEquals("{\"age\":21,\"jsonClass\":\"org.scajorp.dummies.NestedDummy\",\"name\":\"John\",\"person\":{\"age\":null,\"firstName\":null,\"jsonClass\":\"org.scajorp.dummies.PersonDummy\",\"lastName\":null,\"married\":false}}", result)
    }
    
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
        assertEquals("[1,22,3]",result)
    }
   

    /*

     @Test
     def simpleValueObject_AnyVal() {
     val result = JSONSerializer.serialize(address);
     assertEquals("{\"street\":\"Mulholland Drive\",\"city\":\"Los Angeles\",\"state\":\"CA\",\"zip\":\"12345\"}", result)

     }

     @Test
     def nestedSimpleValueObjects() {

     }

     @Test
     def nestedComplexObjects() {
    
     }

     @Test
     def excludingProperties_annotations() {

     }
    
     @Test
     def excludingProperties_syntax() {

     }*/

}
