package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import models.UrlShortener

object UrlController extends Controller {

  def getLongUrl(url: String) = Action {
    
    UrlShortener.toLongUrl(url) match {
      case Some(str) => Redirect(str)		// Redirect to original page
      case _         => Redirect("")		// Redirect to short url generator page
    }
  }
  
  def getShortUrl(url: String) = Action {
    Ok(UrlShortener.toShortUrl(url))
  }

  def getStat(url: String) = Action{
    Ok(UrlShortener.checkStatus(url)).as("application/json")
  }
}
