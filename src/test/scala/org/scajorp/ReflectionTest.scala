package org.scajorp

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

import java.lang.reflect.Method

class ReflectionTest {

    val jsonParser = new JSONParser

    val jsonObject: Map[String, Any] = Map("name" -> "Johnny", "age" -> 99)




    val className = "org.scajorp.UserDummy"

    @Test
    def createClassInstance {        
        val user: UserDummy = (jsonParser.createClassInstance(className, jsonObject)).asInstanceOf[UserDummy]
        assertNotNull("User could not be instantiated", user)
    }

    @Test
    def createClassInstanceFiltered {        
	val filtered = jsonObject.filterKeys(_ == "name")
        val user: UserDummy = (jsonParser.createClassInstance(className, filtered)).asInstanceOf[UserDummy]
        assertNotNull("User could not be instantiated", user)
    }

    @Test
    def assignValues {
        val user: UserDummy = (jsonParser.createClassInstance(className, jsonObject)).asInstanceOf[UserDummy]
        assertEquals("Johnny", user.name)
        assertEquals(99, user.getAge)
    }

                
}
