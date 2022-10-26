# aks-2-db-latency
A sample application to measure latency between AKS and SQL Server
 
# Local Build (Requires local docker)
```
mvn package 
```

## Package 
```
docker build . -t youracr.azurecr.io/dbtest:latest
docker push youracr.azurecr.io/dbtest:latest
```
