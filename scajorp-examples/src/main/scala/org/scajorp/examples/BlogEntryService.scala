package org.scajorp.examples

/** 
* Created by IntelliJ IDEA.
* User: marco
* Date: Jun 2, 2008
* Time: 8:36:11 AM
* To change this template use File | Settings | File Templates.
*/
class BlogEntryService {
  def getAll() = 
      List[BlogEntry](new BlogEntry("Wutzz up?", "John J."), new BlogEntry("Yesterday, all my troubles ...", "Beeatels"))
}