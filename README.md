## HackathonTheGame
We thought of the scene in The Social Network where they play coding games against each other. We wanted to make a web app for that, so we can easily play against each other and find out who can solve programming challenges the fastest. The idea is that this should be a social game where you sit together and play. This makes it different than sites like HackerRank and Project Euler. 

**Gameplay**
The game is a minimum viable product, and could be extended with multiple features. 
Currently you can login using a username, and then you get the challenge. This is the current challenge on the server and all connected players get the same challenge. You will be presented with a code editor (We use Microsoft's Monaco editor and a submit button. When you press submit, we run our tests and if you pass them you win. All players are notified and then the game moves on to the next challenge for all. 

**Compiler and code runner**
The code you submit is sent to our server. We compile it and then run a program which loads the classfile and runs some tests. These tests are declared in a JSON file. This makes it easy for people to add new challenges as well. The runner is a JAR file of it's own which we run via the ProcessBuilder class in java. We compile it using Javac.
The user will get back a string which contains error output if there was any, otherwise it shows some information about how well you did on tests. 

**Security** 
The code runner is not as safe as it should be. Optimally it should spawn a docker instance or another container to run the user inputted code in. Otherwise it should run the program with some restrictions. You could use a SecurityManager on the thread running the actual user code to limit file access and thread spawning. We would like to implement the first approach, maybe together with the second to make it extra secure but we decided that currently this is just a gentlemans sport. 

**Web Application**
The application is just a simple Spring Boot application running on a Tomcat server. It just serves our static files and then we render everything dynamically with ReactJS on the frontend. 

**Communication between clients and server**
We use SockJS for websocket communication between the client and the server. We have private channels for logging in and submitting code, and then we have a public channels for news about the game. We use this channel to broadcast if a user wins right now. If we had time it would be quite easy to add a chat as well as the framework is easy to work with. 

**Frontend**
Our frontend is made with ReactJS.

**Hosting**
We are hosting the application ourselves on a Raspberry Pi. The Pi is setup as an access point so you can connect to it via Wifi. 
