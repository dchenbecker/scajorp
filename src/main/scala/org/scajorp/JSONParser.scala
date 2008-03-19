package org.scajorp

import java.lang.reflect.Method

import scala.collection.Map

class JSONParser {

  /**
   * Takes the given map representing a JSON Object and uses the map attributes to instantiate
   * an object of the correct type (specified by the "jsonClass" key).
   * 
   * @param jsonObject The map containing the jsonClass mapping along with mappings for each property of the
   * 	class to be instantiated.
   * 
   * @return The instantiated class or None if the class was not instantiable.
  */
  def resolve(jsonObject: Map[String, Any]): Option[Object] = jsonObject.get("jsonClass") match {
        case Some(className: String) => createClassInstance(className, jsonObject.filterKeys(_ != "jsonClass"))
        case _ => None
  }

  def createClassInstance(className: String, jsonFields: Map[String, Any]): Option[Object] = {
    try {
        val cls = Class.forName(className);
        val instance = cls.newInstance().asInstanceOf[Object];

        val setterFields : Map[String, Method] = createPublicSetterFields(cls);

        jsonFields.keys.foreach(fieldName => setterFields.get(fieldName) match {
          case Some(method) => {
            jsonFields(fieldName) match {
              // Handle nested objects. What to do if it doesn't resolve?
              case obj : Map[String,Any] => method.invoke(instance, Array(resolve(obj).get))
              // Handle nested arrays in a type-correct manner
              case array : List[Any] => method.getParameterTypes()(0).getSimpleName match {
                // Integral types
                case "byte[]" => {
                  val arg : Array[Byte] = array.asInstanceOf[List[Double]].map(_.toByte).toArray
                  method.invoke(instance, Array(arg))
                }
                case "short[]" => {
                  val arg : Array[Short] = array.asInstanceOf[List[Double]].map(_.toShort).toArray
                  method.invoke(instance, Array(arg))
                }
                case "int[]" => {
                  val arg : Array[Int] = array.asInstanceOf[List[Double]].map(_.toInt).toArray
                  method.invoke(instance, Array(arg))
                }
                case "long[]" => {
                  val arg : Array[Long] = array.asInstanceOf[List[Double]].map(_.toLong).toArray
                  method.invoke(instance, Array(arg))
                }
                // Float type
                case "float[]" => {
                  val arg : Array[Float] = array.asInstanceOf[List[Double]].map(_.toFloat).toArray
                  method.invoke(instance, Array(arg))
                }
                case "double[]" => {
                  val arg : Array[Double] = array.asInstanceOf[List[Double]].toArray
                  method.invoke(instance, Array(arg))
                }
                // Char type
                case "char[]" => {
                  val arg : Array[Char] = array.asInstanceOf[List[String]].map(_.charAt(0)).toArray
                  method.invoke(instance, Array(arg))
                }
                case "String[]" => {
                  val arg : Array[String] = array.asInstanceOf[List[String]].toArray
                  method.invoke(instance, Array(arg))
                }
              }
              // Handle simple values
              case value : Object => method.invoke(instance, Array(value))
            }
          }
          case None => format("No such method name: {0}\n", fieldName)
        })

        Some(instance)
    }
        // should there simply be a check for Throwable
        // or is there gonna be e.g. a log message
    catch {
        case cnf : ClassNotFoundException => throw cnf
        case genericError: Throwable => {
          println(genericError)
          //genericError.printStackTrace()
          None
        }
    }



  }

  /**
    *   Creates a Map containing Method objects reflecting all public setter methods of the class or interface
    *   represented by the passed in Class object, as values. For keys, the stripped down fieldnames of the
    *   respective setter methods are used. Works with Scala setters_ and Java setMethods().
    *
    *   E.g. Scala: name_     :      map += ("name" -> method)
    *        Java: setName()  :      map += ("name" -> method)
    *
    **/
  private def createPublicSetterFields(cls: Class[_ <: Any]): Map[String, Method] = {
        import scala.collection.immutable.Map

        val methods = cls.getMethods()

        methods.foldLeft(Map.empty[String, Method])({(resultMap, method) =>
            val name = method.getName

            // Scala style setter check
            if (name.endsWith("_$eq")) {
              resultMap + Pair(name.substring(0,name.lastIndexOf("_$eq")),method)
            }

            // Java style setter check
            else if (name.startsWith("set")) {
              resultMap + Pair(name.substring(3).toLowerCase,method)
            }

            // failure, just pass through
            else {
              resultMap
            }
        })
    }
}
