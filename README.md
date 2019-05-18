
## Architecture

For the design of PaymentAPI we use DDD + CQRS + Event Sourcing, we will explain a little bit why the use of this three architectures patters.

### DDD

Domain Driven Design it's a design pattern for dealing with highly complex domains that is based on making the domain itself the main focus of the project.

Also it helps to decouple the different layers of your program(Application, Domain, Infrastructure) making it more extensible and easy to port each of those layers into another program if it's need it.

In PaymentAPI we implement three modules described like:

* **Application**: Layer which normally is the entry point of your application. It has dependency with domain and infrastructure modules.
* **Domain**: Layer where you implement the business logic of your application it does not have any dependency.
* **Infrastructure**: Layer where you implement the communication with the backends(Database, services, Brokers). It has dependency with domain

![My image](img/ddd.png)

### CQRS

Command Query Responsibility Segregation it's another Design pattern, mostly divulged by **Greg Young** which segregate the model of Commands and Queries
in your architecture to do Writes and Reads separate. It can give you the chance to make the Queries more efficient since normally in a Cluster the 90% of the traffic
it's for Queries.

In PaymentAPI we implement Handler for Commands and Service to deal with Queries.

* **Handler**: Receive commands and transform the Command into Domain model, to pass the responsibility to the domain, and finally into the infrastructure.
* **Service**: Receive Queries and invoke directly to the DAO in the infrastructure layer.

### Event Sourcing

Event sourcing it's design patter where you work with Events. The main idea Event sourcing is to keep the state of your program, 
persist every action that happens in your program, and all that without mutate previous state.

That means that with Event sourcing we are not deleting any data in our system, giving us the possibility to have an historical about the different state of one Payment.

In PaymentAPI we implement together with CQRS this patter to allow rehydrate of event and historical of data.

### API

The design of the API it's based on Restful design, so for Queries we use **GET**, for create **POST**, update **PUT** and delete **DELETE**

Since the root of the endpoints its `/v1/payment` many of the endpoints does not require any extra word, only arguments as `Query param`

```.java
@Path("/v1/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @GET
    @Path("/{paymentId}")
    public CompletionStage<PaymentResponse<?>> fetchPaymentById(@PathParam("paymentId") String id)
    
    
    @POST
    @Path("/")
    public CompletionStage<PaymentResponse<String>> addPayment(AddPaymentCommand addPaymentCommand)     
}
```

## Testing

![My image](img/testPyramid.png)

To go fast you have to go well` this quote of Robert C. Martin express perfectly what TDD and BDD is. You should first think in all corner cases of your program and then implement 
one by one committing every scenario to have a quick feedback about your program.
 
In our application we invest around 70% of the time implementing the test framework, from the most concrete type of testing(Unit) to higher level of Integration.

* **Unit**: We use JUnit5 together with Mockito to Mock external resources of your class
* **Integration**: We use Quarkus server, which include a very nice Test framework to do BDD and run the application and test all layers of your application.
Just to be clear, the IT test are just a prove that our Unit test are well designed and the Mock behaves as we expect. None IT test it should fail ever. And if it does, 
you have to reproduce it in Unit test.

## Technology Stack

As Http Server we use Quarkus, a new Serverless framework implemented by RedHat team, as a new Power Vertx framework. Ineed a good choice to create Reactive systems.

For the API we use JAX-RS standard.

To have a reactive system, functional programing it's a powerful tool. Even Java it's functional since Java 8 it's not Scala. And a good library for functional 
programing that make you feel that you're in Scala realm again, it's Vavr.

To make transformation of models between layer doing DDD we use Orika.

Finally as Connector and Database we choose Cassandra since it's a good choice for Event sourcing.

* **Application**: Quarkus, JAX-RS, Vavr, Mockito

* **Domain**: Vavr, Orika

* **Infrastructure**: Orika, Vavr, Orika, Cassandra, Mockito.


## Use

To run the Unit and Integration test, just go into the root program and run the command:

```
mvn clean install
```

Once that you have your project tested, you just need to run the application. Go to the application module and run the command:

```
./mvnw compile quarkus:dev`
```
 
