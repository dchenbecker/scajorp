package org.scajorp

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert._

class ReflectionTest {

    val javaUser = new JavaUserDummy
    val cls = classOf[JavaUserDummy]




   @Test
   def getClassName() {   
        assertEquals("org.scajorp.JavaUserDummy", cls.getName())
    }

//    @Test
//    def getJavaVoidReturnType() {
//        val method = cls.  get
//
//    }


//    @Test
//    def getParameterTypes() {
//        // TODO
//    }
//
//
//    @Test
//    def getModifiers() {
//        // TODO
//    }
}
