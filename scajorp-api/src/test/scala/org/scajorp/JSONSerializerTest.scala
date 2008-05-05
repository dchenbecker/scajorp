package org.scajorp


import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

class JSONSerializerTest {

    val jsonSerializer = new JSONSerializer

    var person: PersonDummy = _

    @Before
    def setUp():Unit = {
        person = new PersonDummy();        
    }


    @Test
    def simpleObject_Strings() {        
        person.firstName = "John"
        person.lastName = "Rambo"        
        val result = jsonSerializer.serialize(person);
        assertEquals("{\"firstName\":\"John\",\"lastName\":\"Rambo\"}", result)
    }
    
   

    /*

     @Test
     def simpleValueObject_AnyVal() {
     val result = jsonSerializer.serialize(address);
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
