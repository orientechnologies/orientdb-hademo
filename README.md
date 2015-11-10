



## install javascript dependencies


`cd src/main/webapp`

`bower install`

## launch application


`mvn spring-boot:run -Drun.arguments="--port=8080,--db=remote:localhost/demo"`


To run another app 

`mvn spring-boot:run -Drun.arguments="--port=8081,--db=remote:localhost:2425/demo"`
