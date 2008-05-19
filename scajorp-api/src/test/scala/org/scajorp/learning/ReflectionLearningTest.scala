/*
 * ReflectionLearningTest.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.learning

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before
import org.scajorp.dummies.Friend

class ReflectionLearningTest {
    
  val friend = new Friend("Kelso")
  
  @Test
  def classParameters_access() {    
    assertEquals("Kelso", friend.name)
  }
  
  @Test 
  def classParameters_reflection() {    
    val fields = friend.getClass().getDeclaredFields()        
    assertEquals("name", fields(0).getName())
  }
    
}
