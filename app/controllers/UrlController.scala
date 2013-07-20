package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import models.UrlShortener

object UrlController extends Controller {

  def index=TODO

  def fromShortUrl(url: String) = Action {
    Redirect(UrlShortener.urlLong(url))
  }
  
  def fromLongUrl(url: String) = Action {
    Ok(UrlShortener.urlShort(url))
  }

  def urlStat(hash: String) = Action{
    Ok("{result:simply,hash:"+hash+"}")
  }
}
