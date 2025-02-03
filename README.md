# PostgresTree

This repository is about a system that saves and retrieves integer trees from a PostgreSQL database with jOOQ.

Instead of Spring Initializr this app was initially created with Bootify.io.

## Development

The database needs to be started before the repository can be built due to the jOOQ codegen. For that run

```
docker compose up -d
``` 

in the root directory.
[Docker](https://www.docker.com/get-started/) must be available on the current system.

The application can be run by starting
the [PostgresTreeApplication](src/main/kotlin/at/rspiegl/postgres_tree/PostgresTreeApplication.kt)or started with

```
gradlew bootRun
```

After starting the application it is accessible under `localhost:8080`.

Swagger UI is accessible under [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## How to test

I suggest using either the [endpoints file](endpoints.http) in IntelliJ or
the [postman collection](postman_collection.json), although with postman my requests timed out earlier than in IntelliJ.

Play around for exceptions with creation and deletion.
Then delete all edges.
Run the tree generation with your desired amount of nodes. This will generate an unbalanced (i.t. randomly 1 to 4 child
nodes) tree with nodeIds starting from 0 to the specified number. Even though the inserts are batched, larger values
than 1 million could take a while and the HTTP request might run into a timeout.
Then call the tree retrieval endpoint with your desired nodeId.

## Performance:

From my limited profiling checks with a tree with 1 million nodes the optimal endpoint didn't go above 200MB RAM even
though in total it has used more RAM/s than the non optimal endpoint. The non optimal endpoint had a max RAM of 400MB
and more CPU spikes. With 10 million nodes it is quit different, RAM usage increases until the GC runs regularly and
then stays the same. The non-optimal endpoint doesn't return any data and crashes after some time.

Note: the application doesn't warm up the database connection, so keep in mind that the first request will be slower
than subsequent ones.
