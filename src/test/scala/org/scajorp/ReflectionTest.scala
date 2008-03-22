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


    /* ------  Construction Test(s) ----- */ 

    @Test
    def resolveObject() {
        jsonParser.resolve(jsonObject) match {
            case Some(user) => assertNotNull("Error while creating user occurred", user)
            case None => fail("Resolving error")
        }
    }


    /* ------ Primitive Types Test(s) ------ */

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


     /* ------ Nested Types Test(s) ------ */

    @Test
    def assignNestedObject() {
      val addressMap = Map("jsonClass" -> "org.scajorp.AddressDummy",
                           "street" -> "123 Nowhere Lane",
                           "city" -> "Nowheretown",
                           "state" -> "Nothing",
                           "zip" -> "12345")
      jsonObject += ("address" -> addressMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested object", user.address)
          assertEquals("12345", user.address.zip)
        }
        case None => fail("Resolving nested object")
      }
    }

    @Test
    def assignNestedByteArray() {
      val byteMap = List[Double](0xde, 0xad, 0xbe, 0xef)
      jsonObject += ("favoriteBytes" -> byteMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested byte array", user.favoriteBytes)
          assertEquals(4, user.favoriteBytes.size)
        }
        case None => fail("Resolving nested object")
      }
    } 

    @Test
    def assignNestedShortArray() {
      val shortMap = List[Double](384, 768)
      jsonObject += ("favoriteShorts" -> shortMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested short array", user.favoriteShorts)
          assertEquals(2, user.favoriteShorts.size)
          assertEquals(768 : Short, user.favoriteShorts(1))
        }
        case None => fail("Resolving nested object")
      }
    } 

    @Test
    def assignNestedIntArray() {
      val luckyNumbersMap = List[Double](11, 42, 1776)
      jsonObject += ("luckyNumbers" -> luckyNumbersMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested int array", user.luckyNumbers)
          assertEquals(3, user.luckyNumbers.size)
        }
        case None => fail("Resolving nested object")
      }
    }   

    @Test
    def assignNestedLongArray() {
      val longMap = List[Double](123678912, 987654321)
      jsonObject += ("favoriteLongs" -> longMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested long array", user.favoriteLongs)
          assertEquals(2, user.favoriteLongs.size)
          assertEquals(987654321 : Long, user.favoriteLongs(1))
        }
        case None => fail("Resolving nested object")
      }
    } 
       
    @Test
    def assignNestedFloatArray() {
      val floatMap = List[Double](3.14159, 1.21, 1.11111111)
      jsonObject += ("favoriteFloats" -> floatMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested float array", user.favoriteFloats)
          assertEquals(3, user.favoriteFloats.size)
          assertEquals(3.14159f : Float, user.favoriteFloats(0))
        }
        case None => fail("Resolving nested object")
      }
    }  

    @Test
    def assignNestedDoubleArray() {
      val doubleMap = List[Double](7.5462, 12.443, 8.2343, -0.112)
      jsonObject += ("favoriteDoubles" -> doubleMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested double array", user.favoriteDoubles)
          assertEquals(4, user.favoriteDoubles.size)
          assertEquals(-0.112, user.favoriteDoubles(3), Double.Epsilon)
        }
        case None => fail("Resolving nested object")
      }
    }   

    @Test
    def assignNestedCharArray() {
      val charMap = List[String]("W", "h", "a", "t", "?")
      jsonObject += ("favoriteChars" -> charMap)
      //println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested char array", user.favoriteChars)
          assertEquals(5, user.favoriteChars.size)
          assertEquals('a' : Char, user.favoriteChars(2))
        }
        case None => fail("Resolving nested object")
      }
    } 

    @Test
    def assignNestedStringArray() {
      val stringMap = List[String]("What", "is", "this", "?")
      jsonObject += ("favoriteStrings" -> stringMap)
      println("User = " + jsonObject)
      jsonParser.resolve(jsonObject) match {
        case Some(user : UserDummy) => {
          assertNotNull("Error resolving nested string array", user.favoriteStrings)
          assertEquals(4, user.favoriteStrings.size)
          assertEquals("this", user.favoriteStrings(2))
        }
        case None => fail("Resolving nested object")
      }
    } 
}
