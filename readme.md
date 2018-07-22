FIFA World Cup 2018 Tickets App
----------------------------------------

This is a simple app which which allows to generate tickets to football games.
Once ticket is generated it will be pushed by Redis publisher to the queue.
When Redis subscriber discovers new ticket, it'll save it to the database (H2) 
and send to all app clients via web socket channel. 
After that, new ticket will appear on the client's web page.
Also it is possible to get all tickets stored in the DB by clicking "Load tickets from DB" button.

### Run app in Docker

* Install Docker
* Checkout source code with git or simply download .zip package and unpack it.
* Open terminal (GitBash on Windows).
* Go to the project root directory (/data-pipeline).
* Execute `./mvnw clean package -DskipTests`.
* After previous command is completed, .jar file will be generated.
* Execute `docker-compose up -d`.
* After application is up, it is available at localhost:8088. 

### UI

* Web page available at localhost:8088/ is splitted into 2 parts.
* Left part demonstrates that all available tickets can be loaded from DB.
* Right part shows real time message delivery through web socket channel.

### API

* To get all tickets from the DB: GET localhost:8088/api/tickets
* If you want to publish your own ticket: POST localhost:8088/api/ticket. Here is an example:
``` 
curl -d '{{"ticket":{"game":{"firstTeam":"France","secondTeam":"Brasil","stadium":{"city":"Saint-Denis","name":"Stade de France"},"matchDate":"1998-07-12"},"price":1000}}}' -H "Content-Type: application/json" -X POST http://localhost:8088/api/message
```
