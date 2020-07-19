# mutants-api
Repository that has the project in charge of knowing whether a human is mutant or not based on its DNA.

## Clonning project
You should clone this project in your local machine and the PROJECT_ROOT_PATH will be: **(mutants-api/mutants)**

## Automatic compiling and deploying
There is a file `start-mutants-api.sh` that compiles and deploys, the project and the DB, into two different docker containers.<br/>
For executing this file, you need to go into the PROJECT_ROOT_PATH and execute the following command:
```
$ ./start-mutants-api.sh
```

## Environments
There are 3 different environments available for compiling and deploying mutants project:

- **dev** :arrow_right: For developing and working in local environment.
- **docker** :arrow_right: For using the Docker provided images.
- **prod** :arrow_right: For using DB hosted in AWS.

The properties of each project are defined on `application-dev.properties`, `application-docker.properties` and `application-prod.properties`

## Manual compiling
This is a Maven project and for compiling it you should proceed with the Maven wrapper provided. You should go into PROJECT_ROOT_PATH and run the following command:
```
$ ./mvnw clean install -P{DESIRED_ENVIRONMENT}
```

## Deploying
The project is dockerized and it has an external Postgres Database dockerized too. For deploying both of them you only need to run the following commands into PROJECT_ROOT_PATH.

The first one will destroy the images generated previusly and the second one will create them again.

The project will always check that the DB is already initialized for starting to deploy the application. You can check this process executing `docker-compose up` instead of `docker-compose up -d`
```
$ docker-compose down -v
$ docker-compose up -d
```

## Docker manipulation
You are available to run and stop each container separately with the following commands:
```
$ docker start/stop mutants-api
$ docker start/stop mutants-postgresql
```
You can remove each image and container as well with the following commands:
```
$ docker rm mutants-api
$ docker rm mutants-postgresql
$ docker rmi mutants-api:latest
```
For reviewing the DB from console you can type following commands:
```
$ docker exec -it mutants-postgresql bash
$ psql -U postgres;
$ \c taskmanagerdb;
```

## Generalities
The project includes:

- Java documentation into PROJECT_ROOT_PATH/doc path.
- Swagger documentation (:round_pushpin:e.g. For docker environment :arrow_right: `http://localhost:8080/swagger-ui.html`)
- i18n for responses in spanish:es: or english:us:.
- Actuator for checking health of application.

### API request
#### Headers and parameters
| header  | value | description |
| ------------- | ------------- | ------------- |
| Accept  | application/json  | Default value  |
| Accept  | application/xml  | If you want a xml response  |
| locale  | en  | Default value  |
| locale  | es  | If you want a response in spanish  |

#### Healthcheck
There is a GET endpoint for verifying health check of application: `http://localhost:8080/actuator/health`

#### Statistics
There is a GET endpoint located in `http://localhost:8080/stats` that returns the state of the system with the total number of humans consulted and the portion of this humans are mutants too.

#### Mutants
There is a POST endpoint located in `http://localhost:8080/mutants` that checks whether a human is mutant too. You need to specify the body with an object `HumanDTO` that contains the DNA to be analyzed.

Example:
```
{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGTAGG",
        "ATCCCC",
        "TCACTG"
    ]
}
```

The system will verify that the DNA is not null, contains elements and it is a square matrix.

> :warning: The system does not verify that the entered letters are capitalized or are only A, T, C, G neither. The user needs to guarantee that the input DNA contains only these capital letters. :round_pushpin:e.g. "AaAA" is not a sequence

> :warning: If the system finds a sequence with more than 4 consecutive letters, it will identify only one sequence. :round_pushpin:e.g. With 8 letters "T" :arrow_right: "TTTTTTTT" is just one sequence.

### API responses
The system checks in DB before trying to traverse the matrix and when it finds 2 sequences the algorithm stops for avoiding to do an extra effort in the calculation.

#### Healthcheck
200 OK with body:
```
{
    status: "UP"
}
```

#### Statistics
200 OK with body:
```
{
    "count_mutant_dna": 2,
    "count_human_dna": 6,
    "ratio": 0.3333333333333333
}
```
> :warning: The values `count_mutant_dna` and `count_mutant_dna` are not in camel case because of the type of response required for the test.

#### Mutants
There are 2 different type of responses:
- 403 FORBIDDEN, when is a Human:
```
{
    "errorCode": "human.is.not.mutant.exception",
    "errorMessage": "This human is not a mutant",
    "errorDetail": "Forbidden",
    "httpStatus": 403,
    "errorOriginPath": "/mutant",
    "errorOriginApp": "mutants-api"
}
```

- 200 OK, when is a Mutant:
```
{
    "dna": [
        "TTGCxA",
        "CTGTGC",
        "TGTTGT",
        "AGTTGG",
        "ATCCCA",
        "TCACTG"
    ],
    "mutantDna": true
}
```

#### Matrix errors
- 400 BAD_REQUEST, when is not a square matrix or there is not DNA information:
```
{
    "errorCode": "human.matrix.dna.size.exception",
    "errorMessage": "Check the matrix, it should be a square matrix.",
    "errorDetail": "Bad Request",
    "httpStatus": 400,
    "errorOriginPath": "/mutant",
    "errorOriginApp": "mutants-api"
}
```

- 400 BAD_REQUEST, when the DNA is null:
```
{
    "errorCode": "human.matrix.dna.size.exception",
    "errorMessage": "Check the matrix, it should be a square matrix.",
    "errorDetail": "dna null",
    "httpStatus": 400,
    "errorOriginPath": "/mutant",
    "errorOriginApp": "mutants-api"
}
```




