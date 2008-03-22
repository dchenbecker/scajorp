package org.scajorp


import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

class SerializationTest {

     val jsonSerializer = new JSONSerializer

    var address: AddressDummy = _

   @Before
    def setUp():Unit = {
        address = new AddressDummy
        address.street = "Mulholland Drive"
        address.city = "Los Angeles"
        address.state = "CA"
        address.zip = "12345"
    }


    @Test
    def simpleValueObject_Strings() {
        val result = jsonSerializer.serialize(address);
        assertEquals("{\"street\":\"Mulholland Drive\",\"city\":\"Los Angeles\",\"state\":\"CA\",\"zip\":\"12345\"}", result)

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
