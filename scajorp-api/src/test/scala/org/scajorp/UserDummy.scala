package org.scajorp

class UserDummy() {

    var name: String = _

    var married: Boolean = _

    var address: AddressDummy = _

    var luckyNumbers : Array[Int] = _

    var favoriteBytes : Array[Byte] = _

    var favoriteShorts : Array[Short] = _

    var favoriteLongs : Array[Long] = _

    var favoriteFloats : Array[Float] = _

    var favoriteDoubles : Array[Double] = _

    var favoriteChars : Array[Char] = _

    var favoriteStrings : Array[String] = _

    private[this] var age = 0

    private[this] var loverCount = 5


    def getAge() = age

    def age_=(a:Int) {age = a}
    
    private def loverCount_=(c:Int) { loverCount = c }
    
    def getLoverCount() = loverCount

    override def toString() = "User -> Name[=" + name + "], Age[=" + age + "]"
}

