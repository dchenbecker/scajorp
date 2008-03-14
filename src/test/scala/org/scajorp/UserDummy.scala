package org.scajorp

class UserDummy() {

    var name: String = _
    
    private[this] var age = 0

    override def toString() = "User -> Name[=" + name + "], Age[=" + age + "]" 

    def getAge() = age

    def age_=(a:Int) {age = a} 

    def setSomething(a:Int) {age = a}
}

