package org.scajorp

class UserDummy() {

    var name: String = _
    var age: Int = _

    override def toString() = "User -> Name[=" + name + "], Age[=" + age + "]" 


   def getAge() = age
}
