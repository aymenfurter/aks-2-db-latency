# aks-2-db-latency ⌛☕
A java sample application to measure latency between AKS and SQL Server in a hello world scenario.


## Local Build (Requires local docker)
```
mvn package 
```

## Package 
```
docker build . -t youracr.azurecr.io/dbtest:latest
docker push youracr.azurecr.io/dbtest:latest
```

## Run
The manifest folder contains the sample deployment file how to run the image. You likely want to configure `SPRING_DATASOURCE_URL` and `APPLICATIONINSIGHTS_CONNECTION_STRING` to point to a MSSQL / Application Insight instance.

## Application Insight Integration
Application Insight is preconfigured, you can investigate the flow from the microservice to the database:

![image](https://user-images.githubusercontent.com/20464460/198011355-248ee7f7-670e-4c44-a8d3-27b9dc724d98.png)
