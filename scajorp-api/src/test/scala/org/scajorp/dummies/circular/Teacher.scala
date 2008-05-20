/*
 * Teacher.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.dummies.circular

class Teacher(val name: String, val boss: Principal) {
  val student: Student = new Student("Jack Bauer", this)
}
