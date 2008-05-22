package org.scajorp.json

import scala.collection.jcl.ArrayList

/**
 * A JSON array can simply be represented as List (or Array).
 * This class is an ArrayList which's toString() method will turn it
 * into a valid JSON string. It does so by delegating all responsibility
 * to trait TJSONSerializable. 
 *  
 * @author Marco Behler 
 * @see TJSONSerializable
 */

class JSONArray extends ArrayList[Any] with TJSONSerializable
              
