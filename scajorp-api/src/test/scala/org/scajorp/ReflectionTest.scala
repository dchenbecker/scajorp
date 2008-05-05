package org.scajorp

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

class ReflectionTest {

    val javaUser = new JavaUserDummy
    val userDummy = new UserDummy
    val javaClass = classOf[JavaUserDummy]
    val scalaClass = classOf[UserDummy]


   @Test
   def getJavaClassName() {   
        assertEquals("org.scajorp.JavaUserDummy", javaClass.getName())
   }


   @Test
   def getJavaMethod() {
        val method = javaClass.getMethod("setName", Array(classOf[String]));
         assertNotNull("Method not found", method);
         assertEquals("setName", method.getName());
   }


   // @Test(expected= classOf[NoSuchMethodException]) does not seem to work
   @Test
   def getNonExistingJavaMethod() {
        try {
            val method = javaClass.getMethod("setNonExisting", Array(classOf[String]));
            fail("Method was found but shouldn't have been");
        }
        catch { case expected: NoSuchMethodException => {}}
   }


   @Test
   def getJavaVoidReturnType() {
       val method = javaClass.getMethod("setName", Array(classOf[String]));
       val returnType = method.getReturnType();
       assertEquals("void", returnType.getName())
   }



//   @Test
//   def getAllScalaMethods() {
//         val methods = scalaClass.getMethods()
//         methods.foreach(method => println(method))
//    }

  @Test
  def getMethods() {
      val methods = javaClass.getMethods;
      assertEquals(15, methods.size)
  }
  
@Test
def getMethods_filtered() {
    val methods = javaClass.getMethods.filter(method => method.getDeclaringClass != classOf[AnyRef])
      assertEquals(6, methods.size)
}

   @Test
   def getScalaMethod() {
         val method = scalaClass.getMethod("age_$eq", Array(classOf[Int]));
         assertNotNull("Method not found", method);
         assertEquals("age_$eq", method.getName());
    }

   @Test
   def getScalaMethodReturnType() {
       val method = scalaClass.getMethod("age_$eq", Array(classOf[Int]));
       val returnType = method.getReturnType();
       assertEquals("void", returnType.getName())
   }


}
