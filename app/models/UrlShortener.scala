package models

import java.security.MessageDigest
import com.mongodb.casbah.Imports._
import play.api.libs.json._
//import scala.math.BigInt

abstract class URLSTR(url: String)
case class LURL(url: String) extends URLSTR(url)
case class SURL(url: String) extends URLSTR(url)

object UrlShortener {

  val dict = ('a' to 'z') ++ ('0' to '9') ++ ('A' to 'Z')

  //dharma.mongohq.com:10043/app17027224
  //ds035428.mongolab.com:35428/hootsuite

  val uri = MongoClientURI("mongodb://allen:allen88@ds035428.mongolab.com:35428/hootsuite")
  val co  = MongoClient(uri)("hootsuite")("hootsuite")

  //val mongoClient = MongoClient("localhost", 27017)
  //val co = mongoClient("hootsuite")("hootsuite")

  def toShortUrl(s: String): String = {
    
    val rc = checkUrl(LURL(s))
    rc match {
      case None => calShortUrl(s)
      case _    => rc.get.getAs[String]("shortUrl").get
    }
  }
  
  def toLongUrl(s: String): Option[String] = {
    
    val rc = checkUrl(SURL(s))
    rc match {
      case None => None
      case _    => val n = rc.get.getAs[Long]("count").get + 1
                   co.update( MongoDBObject("shortUrl" -> s), 
                              MongoDBObject("$set" -> MongoDBObject("count" -> n))
                            )
      			   rc.get.getAs[String]("longUrl")
    }
  }

  def checkUrl(u: URLSTR): Option[models.UrlShortener.co.T] = { 
    val query = u match {
      case LURL(s) => MongoDBObject("longUrl" -> s) 
      case SURL(s) => MongoDBObject("shortUrl" -> s)
      //case _       => null
    }
    co.findOne(query)
  }

  def calShortUrl(s: String): String = {
    
    /*
     * Will consider SHA1 (20 bytes), SHA256 or SHA512, up to 40 bytes hash code,
     * MD5 has 16 bytes
     */
    val keyStr = MessageDigest.getInstance("MD5")
    keyStr.reset()
    keyStr.update(s.getBytes)
    
    val urlList = keyStr.digest().sliding(4,4).map { x =>
      val m = ((x.toList.head & 0x3F) :: x.toList.tail).map("%02x".format(_)).mkString
      val n = Integer.parseInt(m, 16)
      (0 to 5).map { i => dict((n >> i*5) & 0x3d) }.mkString
    }.toList
    
    /*
     * Will consider the short url collision, and pick another one
     * but Base62(a-zA-Z0-9) for 6 characters giving us 62 ^ 6 short urls 
     */
    co.save( MongoDBObject("shortUrl" -> urlList.head, "longUrl" -> s, "count" -> 1.toLong) )
    urlList.head
  }

  def checkStatus(s: String): String = {

    val result = {
      if( s == "ALL" ) co.find()
      else { 
        val query = if(s.length == 6) MongoDBObject("shortUrl" -> s)
                    else MongoDBObject("longUrl" -> s)
        co.find(query) 
      }
    }
    result.map(_.tail.toMap).mkString    //Json.toJson(x.toString)
  }

}
