package models

import java.security.MessageDigest
import com.mongodb.casbah.Imports._
import play.api.libs.json._

case class RECORD(longUrl: String, shortUrl: String, count: Long)

object UrlShortener {

  val dict = ('a' to 'z') ++ ('0' to '9') ++ ('A' to 'Z')
  val mongoClient = MongoClient("localhost", 27017)
  //val db = mongoClient("hootsuite")
  val co = mongoClient("hootsuite")("hootsuite")

  def urltoShort(s: String): String = {
    val md5 = MessageDigest.getInstance("MD5")
    md5.reset()
    md5.update(s.getBytes)
    md5.digest().sliding(4,4).map { x =>
      val m = ((x.toList.head & 0x3F) :: x.toList.tail).map("%02x".format(_)).mkString
      val n = Integer.parseInt(m, 16)
      (0 to 5).map { i => dict((n >> i*5) & 0x3d) }.mkString
    }.mkString("--")
  }

  def urltoLong(s: String): String = {
    "http://www.wenxuecity.com"
  }

  def urlStastic(s: String): String = {

      "Hello World"
    }
  }

}
