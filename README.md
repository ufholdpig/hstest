Problem I'm trying to solve

As a publisher of social media content I want to shorten my URLs so that I can include URLs in my messages but also keep my messages within a character limit as specified by a social network.

Satisfaction Criteria

	A public URL where we can see this URL-shortening interface in action

	Your code on github 

	Three API endpoints

		take a URL and return a hash

		redirect a shortened URL to the original

		provide basic click statistics for any given hash in JSON format

	Use REST as the communication mechanism, choose your own web library/framework

	Programmed in Scala

	Expect requests in parallel

	Bonus points for

		Horizontally scalable

			we should be able to boot your code on multiple machines in parallel
			 and it should Just Work.

		Unique ID generation with no coordination between nodes

			when we boot multiple instances in parallel, the nodes should generate
			 completely unique hashes that have a total ordering


Developped evniroment:

	Play2 + SBT + Scala + MongoDB

