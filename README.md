# Steps to run the app within docker container
1. Type these commands
```bash
# make sure you are in the directory where Dockerfile exists!!!
mvn -DskipTests package
docker build -t ebanking-app .
docker-compose up
```
2. Go to any IP@:8081 => example:
   http://127.10.0.1:8081/

**THAT's IT!🎉**