### Abstract
The Idea is to have multiple apps running behind the API-Gateway and implement 
1. security concerns (SSO) 

in the api-gateway itself.
</br>
</br>
So, let suppose we have following three UI apps:
1. RED (Angular) (URL: red.mra.io)
2. BLUE (thymeleaf) (URL: blue.mra.io)
3. GREEN (Angular) (URL: green.mra.io)


and following microservice
1. user-service

The concept is that:
If we login from RED app and then go to the BLUE or GREEN app then it will show the same user information and vice versa.



---
### Dev 
##### Requirements
1. Java 22
2. Node 22

##### Update /etc/hosts file
Add following at the bottom of `/etc/hosts`
```text
127.0.0.1 red.mra.io
127.0.0.1 blue.mra.io
127.0.0.1 green.mra.io
```

##### Run api-gateway
1. `cd api-gateway`
2. `./mvnw spring-boot:run`

##### Run blue-app
1. `cd blue-app`
2. `./mvnw spring-boot:run`

##### Run green-app
1. `cd green-app`
2. `npm start`

##### Run red-app
1. `cd red-app`
2. `npm start`

You can access [red-app](http://red.mra.io:4000), [blue-app](http://blue.mra.io:4000) and [green-app](http://green.mra.io:4000).
Credentials (username: admin, password: Password@1)