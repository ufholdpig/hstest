package models

import java.security.MessageDigest
import com.mongodb.casbah.Imports._
import play.api.libs.json._
//import scala.math.BigInt				// Improvement for LONG number within MongoDB

abstract class URLSTR(url: String)
case class LURL(url: String) extends URLSTR(url)
case class SURL(url: String) extends URLSTR(url)

/**
 * Main Object which supplied long and short URL converting
 */
object UrlShortener {

  /*
   * MongoDB database access address.
   * MongoHQ and MongoLab
   */
  //dharma.mongohq.com:10043/app17027224
  //ds035428.mongolab.com:35428/hootsuite

  val uri = MongoClientURI("mongodb://allen:allen88@ds035428.mongolab.com:35428/hootsuite")
  val co  = MongoClient(uri)("hootsuite")("hootsuite")

  /*
   * Local database for simple access
   */
  //val mongoClient = MongoClient("localhost", 27017)
  //val co = mongoClient("hootsuite")("hootsuite")

  /**
   * Method  toShortUrl(s: String): String
   * Input:  Long URL
   * Output: Shortened RUL
   * 
   * Description:
   *         Convert to short URL by giving long URL. Check from database if:
   *         1: Exist, get the corresponding short URL
   *         2: Non-Exist: convert to short one and append to database
   */
  def toShortUrl(s: String): String = {
    
    val rc = checkUrl(LURL(s))
    rc match {
      case None => calShortUrl(s)
      case _    => rc.get.getAs[String]("shortUrl").get
    }
  }
  
  /**
   * Method  toLongUrl(s: String): Option[String]
   * Input:  Short URL
   * Output: Option[String] for long RUL
   * 
   * Description:
   *         Check from database if:
   *         1: Exist, get the corresponding long URL
   *         2: Non-Exist: return None
   *         3: return Options to caller for re-directing to new page
   */
  def toLongUrl(s: String): Option[String] = {
    
    val rc = checkUrl(SURL(s))
    rc match {
      case None => None
      case _    => //val n = rc.get.getAs[Long]("count").get + 1
                   //co.update( MongoDBObject("shortUrl" -> s), 
                   //           MongoDBObject("$set" -> MongoDBObject("count" -> n))
                   //         )
                   //------------------------------------------------------------------
        		   // Use findAndModify to increase count field to avoid parallel issue
                   //------------------------------------------------------------------
                   co.findAndModify(query =  MongoDBObject("shortUrl" -> s),
                                    update = $inc("count" -> 1)
                                   )
                   rc.get.getAs[String]("longUrl")
    }
  }

  /**
   * Method  checkStatus(s: String): String
   * Input:  Short URL | Long URL | ALL
   * Output: Detail information about mapping of long and short URL, include count
   * 
   * Description:
   *         Check from database if:
   *         1: Exist, get the informations for reporting
   *         2: Non-Exist: return None
   */
  def checkStatus(s: String): String = {

    val query = s match {
      case "ALL"               => null
      case x if(x.length == 6) => MongoDBObject("shortUrl" -> s)
      case _                   => MongoDBObject("longUrl" -> s)
    }
    "[%s]".format(co.find(query,MongoDBObject("_id" -> 0)).toList.mkString(","))
  }

  /*
   * Assistant method to check URL from database
   */
  def checkUrl(u: URLSTR): Option[models.UrlShortener.co.T] = { 
    val query = u match {
      case LURL(s) => MongoDBObject("longUrl" -> s) 
      case SURL(s) => MongoDBObject("shortUrl" -> s)
      //case _       => null
    }
    co.findOne(query)
  }

  /*
   * Long URL shortener.
   * 
   * Method  calShortUrl(s: String): String
   * Input:  Long URL
   * Output: short URL
   * 
   * Description:
   *         Calculate URL string to generate hash code. Reform to a unique short code.
   *         Depends on MD5 hash code, get 4 6-characters code as short URL
   *         Need improve algorithm to ensure short code collisions. 
   */
  val dict = ('a' to 'z') ++ ('0' to '9') ++ ('A' to 'Z')

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

}
