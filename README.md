# Simple Camel Application for Testing

Note: we're using java 11

## Build
mvnw clean package dependency:copy-dependencies

## Build Docker Image
docker build -t imaniprima/simple-camel:0.0.1 .

## Run on Docker
docker run -it --rm --name simple-camel -e TZ=Asia/Jakarta imaniprima/simple-camel:0.0.1 

## Run on Windows
java -cp target/simple-camel-0.0.1-SNAPSHOT.jar;target/dependency/* id.co.imaniprima.test.SimpleCamel

## Run on Linux
java -cp 'target/simple-camel-0.0.1-SNAPSHOT.jar:target/dependency/*' id.co.imaniprima.test.SimpleCamel

## Stop
ctrl+c on running console
