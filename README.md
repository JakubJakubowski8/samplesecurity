# Sample Security

This project was developed using Java 15 and Spring-Boot 2.4.2 and MySQL.  
This app is sample security with JWT for authenticating.  
It's fully dockerized, you can pull it from [Docker HUB](https://hub.docker.com/r/jakubjakubowski/samplesecurity)

### To start App:
1. Open terminal in main catalog
2. Pull docker image from docker hub: `docker-compose pull`
3. Start app by: `docker-compose up -d`
4. App will work on https://localhost:8080.

### To stop service
1. Open terminal and use this command:
2. `docker-compose down`  use `-v` if you want to remove volume.

### Log In
To use app you need to have authentications. To get them you have to send `POST` request to 
endpoint 
`/api/auth/login` with user credentials (f.e. using curl or postman).
In app exist a predefined `admin` user with all existing permissions. 

You can use below request to log in:

```
curl --location --request POST 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"admin",
    "password":"password"
}'
```
The request will return the response body with `TOKEN` which will be necessary to request other endpoints. Token expires after 30 minutes.

### Create Role 
You can create roles for your new users. There are 4 predefined [rights](src/main/java/com/jakub/samplesecurity/model/RightName.java) and you can assign them to your 
new role.   
For example, using below curl you will create USER_ROLE which will give access to 
create new users and list existing ones. 

```
curl --location --request POST 'http://localhost:8080/api/role/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer TOKEN' \
--data-raw '{
    "roleName":"ROLE_USER",
    "rights": ["ROLE_CREATE_USER", "ROLE_LIST_USER"]
}'
```

The only users with an admin role has permission to create new roles.

### Create User
You can create a new User. To do that you have to log in with user with right `ROLE_CREATE_USER` 
and send `POST` request with user's data.  
Example request: 
```
curl --location --request POST 'http://localhost:8080/api/user/create' \
--header 'Authorization: Bearer TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"user",
    "password":"password",
    "roles":["ROLE_USER"]
}'
```
### More Info About The Project

In this project, I used the newest versions of some tools like Java 15, Spring-Boot 2.4.2, 
Docker-Compose 3.8, MySQL, and more. The main target was to create a secured app with ability to 
log in, reset password, create users, create roles, and give permissions.
To secure app I used Spring Security and JWT. 

This service is Dockerized. To simplify I used Spring-Boot BuildPacks to create Docker Image and
because of that DockerFile is unnecessary. To create a new Docker Image and push it to [Docker Hub](https://hub.docker.com/r/jakubjakubowski/samplesecurity)
just start below script:  
```sh create_docker_image.sh```

To start app you can use `docker run` or using `docker-compose` which is more suitable.

Versions which I used:

* OpenJDK 15.0.1
* Docker 20.10.1
* Docker-compose 1.27.4

#### In case of any problems, please contact me.
