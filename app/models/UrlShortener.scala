package models

import java.security.MessageDigest
import com.mongodb.casbah.Imports._
import play.api.libs.json._

case class RECORD(longUrl: String, shortUrl: String, count: Long)

object UrlShortener {

  val dict = ('a' to 'z') ++ ('0' to '9') ++ ('A' to 'Z')

//mongodb://<user>:<password>@dharma.mongohq.com:10043/app17027224
//ds035428.mongolab.com:35428/hootsuite

  val mongoClient = MongoClient("ds035428.mongolab.com",35428)

  //val mongoClient = MongoClient("localhost", 27017)
  //val db = mongoClient("hootsuite")
  //val co = db("hootsuite")

  //val co = mongoClient("heroku_app17027224")("heroku_app17027224")
  val co = mongoClient("hootsuite")("hootsuite")

  def urltoShort(s: String): String = {
    val md5 = MessageDigest.getInstance("MD5")
    md5.reset()
    md5.update(s.getBytes)
    md5.digest().sliding(4,4).map { x =>
      val m = ((x.toList.head & 0x3F) :: x.toList.tail).map("%02x".format(_)).mkString
      val n = Integer.parseInt(m, 16)
      val ss = (0 to 5).map { i => dict((n >> i*5) & 0x3d) }.mkString
co.save( MongoDBObject("shortUrl" -> ss, "longUrl" -> s, "count" -> 1) )
    }.mkString("******")
  }

  def urltoLong(s: String): String = {
    "http://www.google.com"
  }

  def urlStastic(s: String): String = {

    //val rc = if( s == "ALL" ) co.find() else co.find( MongoDBObject("shortUrl" -> s), MongoDBObject("_id" -> 0) )
/*
    val rc = co.find()

    if( rc.isEmpty )  {
      //Json.toJson("{result:error, hash:"+s+"}")
      "{result:error, hash:"+s+"}"
    }
    else {
      //for( x <- rc ) yield {
      //  x.getAs[String]("longUrl").get.toStrig
      //}.mkString
      //Json.toJson(rc)
      rc.mkString + "hello world"
    }
*/
    val rc = co.find().toList

      "hello world " +rc.mkString + s 
  }

}
