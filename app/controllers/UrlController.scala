package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import models.UrlShortener

object UrlController extends Controller {

  def index=TODO

  def fromShortUrl(url: String) = Action {
    Redirect(UrlShortener.urltoLong(url))
  }
  
  def fromLongUrl(url: String) = Action {
    Ok(UrlShortener.urltoShort(url))
  }

  def urlStat(hash: String) = Action{
    Ok(UrlShortener.urlStastic(hash))
  }
}
