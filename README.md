
### Run 

Once that you have your project created, you just need to run the compile and run command of mvn quarkus

```
./mvnw compile quarkus:dev`
```

Now you can reach the application container `indext.html` `http://localhost:8080/`

And if you want to reach your first endpoint created you can go to `http://localhost:8080/your_endpoint`
 
### Program

Here I develop a simple Serverless where thanks to Quarkus and JAX-RS we make the transport(Request/Response) layer totally agnostic.
You can see the API resource of the application as entry point [here](src/main/java/com/politrons/quarkus/resource/PolitronsQuarkusResource.java)   


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