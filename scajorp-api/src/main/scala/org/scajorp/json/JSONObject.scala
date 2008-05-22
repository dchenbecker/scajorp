package org.scajorp.json

import scala.collection.jcl.TreeMap

/**
 * A JSON object can simply be represented as a Map.
 * This class is a TreeMap which's toString() method will turn it
 * into a valid JSON string. It does so by delegating all responsibility
 * to trait TTJSONSerializable. 
 *  
 * @author Marco Behler 
 * @see TJSONSerializable
 */

class JSONObject extends TreeMap[String,Any] with TJSONSerializable
