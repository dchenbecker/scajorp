package org.scajorp

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

class ReflectionTest {

    val jsonParser = new JSONParser

    var jsonObject: Map[String, Any] = _


    val className = "org.scajorp.UserDummy"

    @Before
    def setUp():Unit = {
        jsonObject = Map("jsonClass" -> "org.scajorp.UserDummy",
                                            "name" -> "Johnny",
                                             "age" -> 99,
                                             "married" -> true)
    }


    @Test
    def createClassInstance() {
        val filtered = jsonObject.filterKeys(_ != "jsonClass")
        val user: UserDummy = (jsonParser.createClassInstance(className, filtered)).asInstanceOf[UserDummy]
        assertNotNull("Error while creating user occurred", user)
    }

    @Test
    def assignValues() {
        val filtered = jsonObject.filterKeys(_ != "jsonClass")
        val user: UserDummy = (jsonParser.createClassInstance(className, filtered)).asInstanceOf[UserDummy]
        assertEquals("Johnny", user.name)
        assertEquals(99, user.getAge)
        assertEquals(true, user.married)
    }
    
    @Test
    def assignNonExistingValues() {
        val filtered = jsonObject.filterKeys(_ != "jsonClass")
        jsonObject += ("nonExistingField" -> 99)
        val user: UserDummy = (jsonParser.createClassInstance(className, filtered)).asInstanceOf[UserDummy]
        assertNotNull("Error while creating user occurred", user)
    }

    @Test
    def assignProtectedValues() {
        val filtered = jsonObject.filterKeys(_ != "jsonClass")
        jsonObject += ("loverCount" -> 20)
        val user: UserDummy = (jsonParser.createClassInstance(className, filtered)).asInstanceOf[UserDummy]
        assertEquals(5, user.getLoverCount())
    }

                
}
