Problem I'm trying to solve

As a publisher of social media content I want to shorten my URLs so that I can include URLs in my messages but also keep my messages within a character limit as specified by a social network.

Satisfaction Criteria

	* A public URL where we can see this URL-shortening interface in action

	* Your code on github 

	* Three API endpoints

		-- take a URL and return a hash

		-- redirect a shortened URL to the original

		## provide basic click statistics for any given hash in JSON format

	* Use REST as the communication mechanism, choose your own web library/framework

	* Programmed in Scala

	* Expect requests in parallel

	* Bonus points for

		-- Horizontally scalable

			we should be able to boot your code on multiple machines in parallel
			and it should Just Work.

		-- Unique ID generation with no coordination between nodes

			when we boot multiple instances in parallel, the nodes should generate
			completely unique hashes that have a total ordering


Developed Environment:

	Play2 + SBT + Scala + MongoDB + Heroku


Achievement:

   *
   * Method  toShortUrl(s: String): String
   * Input:  Long URL
   * Output: Shortened RUL
   *

   *
   * Method  toLongUrl(s: String): Option[String]
   * Input:  Short URL
   * Output: Option[String] for long RUL
   *

   *
   * Method  checkStatus(s: String): String
   * Input:  Short URL | Long URL | ALL
   * Output: Detail information about mapping of long and short URL, include count
   *

  Need more work on handling Json format query string and get enrichment result.
  
  Due to the test purpose, this program has not done for user input/output interface, rather than
  showing up system framework, developing language, builder, database and deploy mechanisms.


  //---------------------------Updated--------------------------------
  //
  // Use findAndModify to increase count field to avoid parallel issue
  //------------------------------------------------------------------


Index:

Url shortener (Test version)


1. Convert long Url to 6-characters short tag. Usage: http://hs-test.herokuapp.com/url/http://www.xxx.com/yyyy

2. Test short Url and re-direct to the original page. Usage: http://hs-test.herokuapp.com/abcdef

3. Check statics of an address. Usage:

http://hs-test.herokuapp.com/stat/[LONG URL]

http://hs-test.herokuapp.com/stat/[SHORT URL]

http://hs-test.herokuapp.com/stat/ALL
