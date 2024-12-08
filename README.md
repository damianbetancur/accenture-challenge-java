# Accenture
*Betancur Damián* **09-12-2024**
## accenture-challenge-java






## Technologies

**Java 17** with **Spring Boot 3.4.0** framework.

- NoSQL **In-memory storage** (Simulated Database).
- API Documentation **Swagger**.

## How to use

Clone challenge-orderProcessing repository:

````
git clone https://github.com/damianbetancur/accenture-challenge-java.git
````

From IDE, you can execute 4 different modes modifying SCOPE in active profile:

-  **dev**: you will use URL production services.
-  **local**: you will use URL production services.
-  **prod**: you can test application with data.sql script using H2
-  **test**: you will be able to use application in https://localhost:8080/api/v1 by default

By default, application will execute in `LOCAL` mode.

After getting the project locally, download the Maven dependencies.

## Security

This project have JWT token authentication

In authentication, when the user successfully logs in using their credentials, a JSON Web Token will be returned. Since tokens are credentials, great care must be taken to prevent security issues. In general, you should not keep tokens longer than required.

You also should not store sensitive session data in browser storage due to lack of security.

Whenever the user wants to access a protected route or resource, the user agent should send the JWT, typically in the Authorization header using the Bearer schema. The content of the header should look like the following:

Authorization: Bearer <token>

This is the endpoint to generate the token:

### POST. No credentials required.
### .../api/v1/users/authenticate

Authenticate the user and return a Bearer Token to consume the other system services.

The JSON Web Token must be sent in the Authorization header of each endpoint

**Request body**

```yaml
{
  "userName": "user_admin1",
  "password": "12345"  
}
```
This is a user with administrator "user_admin1" permissions that can be used to test the system
This is a user with Client "user_client" permissions that can be used to test the system

**Response body**

```yaml
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyX2FkbWluMSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY0MzQzNTc1NiwiZXhwIjoxNjQzNDUzNzU2fQ.GTaBI5weTjhn8bbnZwMzFEWvlvx-KyQkv04hEC5vJHs"
}
```
## ENDPOINTS


### GET. Credentials required.
### .../api/v1/hotels

US 0001: Obtener un listado de todos los hoteles registrados

**Request Params**

**Response body**

```yaml
{
  
}
```

### GET. Credentials required.
### .../api/v1/hotels


US 0002: Obtener un listado de todos los hoteles disponibles en un determinado rango de fechas y según el destino seleccionado.

**Request Params**

    * dateFrom (LocalDate) ****required**** - *orderNumberCM must be 18 characters of the following format 1234-1234-12345678*

    * order (String) - *order must be 1 o 2*

**Response body**

```yaml
{
    "orderDate": "2021-03-17",
    "daysDelayed": 0,
    "orderNumberCM": "0001-0002-00000014",
    "orderNumberCE": "0002-00000014",
    "deliveryStatus": "P",
    "orderDetails": [
        {
            "quantity": 8,
            "partCode": "00000002",
            "accountType": "R",
            "description": "Repuesto 2"
        },
        {
            "quantity": 2,
            "partCode": "00000004",
            "accountType": "G",
            "description": "Repuesto 4"
        }
    ],
    "orderNumber": "00000014"
}
```


### GET. Credentials required.
### .../api/v1/parts/stock/{partCode}

As a user of a Subsidiary, you can check the stock of a spare part by consulting the spare part code to know the stock you have

**Request Params**

* partCode (String) ****required**** - *partCode must be 8 numeric characters*

**Response body**

```yaml
{
  "part": {
    "partCode": "00000001",
    "description": "Repuesto 1"
  },
  "quantity": 85
}
```



### POST. Credentials required.
### .../api/v1/user/signup

As a user of a subsidiary you can register a user to that subsidiary

**Request body**

```yaml
{
    "userName": "prueba@gmail.com",
    "password": "12345678",
    "isAdmin": false,
    "idSubsidiary": 1
}
```

**Response body**

```yaml
{
    "idUser": 6,
    "userName": "prueba@gmail.com",
    "password": "$2a$10$WN6YV6cNSS2dIVXju165weLmPeZh7sZu9NNBWDOUBSH0rczu8qVD2",
    "isAdmin": false,
    "subsidiary": {
        "name": "filial1",
        "address": "santa rosa 123",
        "phone": "+543453678954",
        "country": "Argentina",
        "idSubsidiary": 1
    }
}
```


## Questions

If you have questions, you can email to betancurdamian@gmail.com

### SCOPE

The suffix of each **SCOPE** is used to know which properties file to use, it is identified from the last '-' of the name of the scope.

If you want to run the application from your development IDE, you need to configure the environment variable **SCOPE=local** in the app luncher.

The properties of **application.yml** are always loaded and at the same time they are complemented with **application-<SCOPE_SUFFIX>.yml** properties. If a property is in both files, the one that is configured in **application-<SCOPE_SUFFIX>.yml** has preference over the property of **application.yml**.

For example, for the **SCOPE** 'items-loader-test' the **SCOPE_SUFFIX** would be 'test' and the loaded property files will be **application.yml** and **application-test.yml**

### Web Server

Each Spring Boot web application includes an embedded web server. For servlet stack applications, Its supports three web Servers:
* Tomcat (maven dependency: `spring-boot-starter-tomcat`)
* Jetty (maven dependency: `spring-boot-starter-jetty`)
* Undertow (maven dependency: `spring-boot-starter-undertow`)

This project is configured with Jetty, but to exchange WebServer, it is enough to configure the dependencies mentioned above in the pom.xml file.

### Main

The main class for this app is ChallengeAgenciaTurismoApplication, where Spring context is initialized and SCOPE_SUFFIX is generated.

### Error Handling

We also provide basic handling for exceptions in ApiExceptionControllerAdvice class.

## Api Documentation

This project uses Springfox to automate the generation of machine and human readable specifications for JSON APIs written using Spring. Springfox works by examining an application, once, at runtime to infer API semantics based on spring configurations, class structure and various compile time java Annotations.

You can change this configuration in SpringfoxConfig class.