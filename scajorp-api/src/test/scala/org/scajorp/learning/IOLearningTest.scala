/*
 * IOLearningTest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.learning

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter

class IOLearningTest {
   
  var name = "\"Bob Kelso asds\\addsadasdsadasadds\"//"
  
  @Test
  def byteArrayOutputStream() {    
    var os = new ByteArrayOutputStream()    
    val osWriter = new OutputStreamWriter(os, "UTF-8")    
    osWriter.write(name, 0, name.length)
    osWriter.flush() // never forget to flush!!    
    
  }
}
