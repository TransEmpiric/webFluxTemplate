# Spring 5 Webflux Template

This code can be used to build the next generation of applications using micro-services, reactive programing, webs-sockets, rest, mongoDB, JWT (JSON Web Tokens) and Netty.

## What is this again?

This is a complete template example for Spring 5 Webflux.
The application shows how to use Spring 5 Webflux with Spring Security, web-sockets, @RestContorler, @Controller, Reactive MongoDB, and JWT.

Spring 5 is still new.  There are very few examples of how to use Spring Security with the Webflux stack.
Reactive Oauth2 support is not ready yet.  This application roles it's own Oauth2 like api with JWT tokens.
This app has integrated web-sockets and rest services into the Reactive Spring Security implementation.

#### MongoDB
MongoDB is used for this example, so you'll need this up and running on your local machine, if you are on OSX, you can easily install
MongoDB using Homebrew.
The MongoDB config is defined in application.yml.

## Running locally
```
	git clone https://github.com/TransEmpiric/webFluxTemplate.git
	cd webFluxTemplate
	./gradlew bootRun
```

If everything goes well you can go to the Secure Web-socket Example page [http://localhost:8443/test/ws](http://localhost:8443/test/ws)

#### Get a JWT for authentication
Endpoint:<br/>
http://localhost:8443/auth/token

Method:<br/>
POST:<br/>

Request headers:
```
content-type: application/json
```
Body:
```
{"username" : "jdev", "password":"jdev"}
```
Response:
```
{
"token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZGV2IiwiYXVkIjoidW5rbm93biIsImV4cCI6MjEyMTcwNzgyMiwiaWF0IjoxNTE2OTA3ODIyfQ.TVwbWsz-BDfMQmgUDnB_GloXklEdS_ABMiF9iGzHrBNA1f4yOQb3day7vcFLNxcLefkQjZDlVlpU91AtkzQqLg",
"username": "jdev"
}
```

#### Use JWT on a secure rest endpoint
Endpoint:<br/>
http://localhost:8443/api/rest/user/list:<br/>

Method:<br/>
GET:<br/>

Request headers:
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZGV2IiwiYXVkIjoidW5rbm93biIsImV4cCI6MjEyMTcwNzgyMiwiaWF0IjoxNTE2OTA3ODIyfQ.TVwbWsz-BDfMQmgUDnB_GloXklEdS_ABMiF9iGzHrBNA1f4yOQb3day7vcFLNxcLefkQjZDlVlpU91AtkzQqLg
```
Response:
```
[
   {
      "id":"5a6a2ef094bf49a0b8319a30",
      "username":"jdev",
      "firstname":"Joe",
      "lastname":"Developer",
      "email":"dev@transempiric.com",
      "roles":[
         "ROLE_ADMIN"
      ],
      "enabled":true,
      "lastPasswordResetDate":1516908272107,
      "accountNonExpired":true,
      "accountNonLocked":true,
      "credentialsNonExpired":true,
      "authorities":[
         {
            "authority":"ROLE_ADMIN"
         }
      ]
   }
]
```

#### Use JWT on a secure web-socket endpoint
Go to [http://localhost:8443/test/ws](http://localhost:8443/test/ws) in a browser.
JWT is hard coded in the JS within templates/websocket.ftl
You need to replace with a new token if the hard coded token has expired
```
    <script type="application/javascript">
        // Use wss:// for HTTPS
        var socket = new WebSocket("ws://" + location.host + "/api/ws/echotest?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZGV2IiwiYXVkIjoidW5rbm93biIsImV4cCI6MjEyMTYwODkyMywiaWF0IjoxNTE2ODA4OTIzfQ.QdSkOuGb8tp1QKjRKzUPlUqobNzF0PuDNk4Y7qAXqrVdbVaKiNJPalxUYapDoeQxE_Dz9WqhdrpdLpGQnlgkkw");

        addEvent("keypress", document.getElementById('input'), function(event) {
            socket.send(event.key);
        });
        var output = document.getElementById("output");
        socket.onmessage = function(e) {
            output.innerHTML = output.innerHTML + e.data;
        }
    </script>
```

#### Properties
```
Properties are in application.yml
You can set to HTTPS (test cert works) if you want, but make sure to update the call to web-socket endpoints with "wss://"
```
#### WILLDO
```
Verify password at auth end point with CustomPasswordEncoder
Make a UI for example token retrieval and a UI to use it
Finish Refresh Token code
Add React and/or Angular Front-end example to repo
```