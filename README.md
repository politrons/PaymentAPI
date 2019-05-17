
### Application

#### Stack

Quarkus, JAX-RS, Vavr, Mockito

### Domain

#### Stack

Vavr, Orika

### Infrastructure

#### Stack

Orika, Vavr, Orika, Cassandra, Mockito


### Run 

Once that you have your project created, you just need to run the compile and run command of mvn quarkus

```
./mvnw compile quarkus:dev`
```
 

### Health check

You can take a look how to implement health check in Quarkus [here](src/main/java/com/politrons/quarkus/resource/PolitronsHealthCheck.java)

```
http://localhost:8080/health
```

Response

```
{
    "outcome": "UP",
    "checks": [
        {
            "name": "Politrons health check",
            "state": "UP",
            "data": {
                "Oracle database": "running",
                "Cassandra database": "running"
            }
        }
    ]
}
```