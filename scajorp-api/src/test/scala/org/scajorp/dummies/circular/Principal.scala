/*
 * Principal.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.scajorp.dummies.circular

class Principal(val name: String) {
    
    val underlings = List(new Teacher("Jack", this), new Teacher("John", this))
    
}
