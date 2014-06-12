ChatApp

Run chatapp-1.0-fat.jar, preferably  with the conf.json file loaded 
(java -jar chatapp-1.0-fat.jar -conf conf.json)

Go to http://localhost:2014
(Port is configurable, default is 2014)

Features:
Message of the day: Greets user as they join chatroom with a configurable message (including weather info), can also be requested with /motd command
Message Filter: Filters foul language in messages, configurable list of filtered words
Private messaging: Send a user a private message using "/msg <recipient> <message>"
/me: Send a unique message in the form "<username> <action>", e.g.:
will: /me is cool
will is cool
