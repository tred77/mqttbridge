## MQTT Bridge
This application will bridge MQTT messages to and from the MQTT broker.

### Application Structure
The application is structured as follows:
```
mqtt_bridge
├── controller: Service HTTP endpoints 
├── service: A layer to manage any business logic and orchestrate the data flow to right layer
├── repository: A layer to manage the data access
├── model: A directory to manage data models like POJOs
├── agent: A layer to manage the MQTT client

```

### Build application
To build the application, you can use the following command:
```
./gradlew clean build
```

### Run application
To run the application, you can use the following command from the root directory:
```
java -jar build/libs/mqttbridge-0.0.1-SNAPSHOT.jar
```
In addition to that, you can run the application using docker. To do that, after building the application, a container with the name `mqttbridge` will be created (version is equal to the project version). You can run the container using the following command:
```
docker run -p 8081:8081 mqttbridge:0.0.1-SNAPSHOT
```
