package org.scajorp

class UserDummy() {

    var name: String = _

    var married: Boolean = _

    private[this] var age = 0

    private[this] var loverCount = 5


    def getAge() = age

    def age_=(a:Int) {age = a}
    
    private def loverCount_=(c:Int) { loverCount = c }
    
    def getLoverCount() = loverCount

    override def toString() = "User -> Name[=" + name + "], Age[=" + age + "]"
}

