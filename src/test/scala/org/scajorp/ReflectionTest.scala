package org.scajorp

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

class ReflectionTest {

    val jsonParser = new JSONParser

    var jsonObject: Map[String, Any] = _


    //val className = "org.scajorp.UserDummy"

    @Before
    def setUp():Unit = {
        jsonObject = Map("jsonClass" -> "org.scajorp.UserDummy",
                                            "name" -> "Johnny",
                                             "age" -> 99,
                                             "married" -> true)
    }



    @Test
    def resolveObject() {
        jsonParser.resolve(jsonObject) match {
            case Some(user) => assertNotNull("Error while creating user occurred", user)
            case None => fail("Resolving error")
        }
    }


    @Test
    def assignBooleanValue() {
            jsonParser.resolve(jsonObject) match {
                case Some(user) => assertEquals(true, user.asInstanceOf[UserDummy].married)
                case None => fail("Resolving error")
        }
    }

    @Test
    def assignIntegerValue() {
            jsonParser.resolve(jsonObject) match {
                case Some(user) => assertEquals(99, user.asInstanceOf[UserDummy].getAge)
                case None => fail("Resolving error")
        }
    }

    @Test
    def assignStringValue() {
            jsonParser.resolve(jsonObject) match {
                case Some(user) => assertEquals("Johnny", user.asInstanceOf[UserDummy].name)
                case None => fail("Resolving error")
        }
    }

    @Test
    def assignPrivateValues() {
          jsonObject += ("loverCount" -> 20)
          jsonParser.resolve(jsonObject) match {
                case Some(user) => assertEquals(5, user.asInstanceOf[UserDummy].getLoverCount)
                case None => fail("Resolving error")
        }
    }

    @Test
    def assignNonExistingValue() {
            jsonObject += ("nonExistingField" -> 99)
            jsonParser.resolve(jsonObject) match {
                case Some(user) => assertNotNull("Error while creating user occurred", user)
                case None => fail("Resolving error")
        }
    }




                
}
