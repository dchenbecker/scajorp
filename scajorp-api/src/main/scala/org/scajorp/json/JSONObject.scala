package org.scajorp.json

import scala.collection.jcl.TreeMap

/**
 * A JSON object can simply be represented as a Map.
 * This class is a TreeMap which's toString() method will turn it
 * into a valid JSON string. It does so by delegating all responsibility
 * to trait TJSONWriter. 
 *  
 * @author Marco Behler 
 * @see TJSONWriter
 */

class JSONObject extends TreeMap[String,Any] with TJSONWriter
