
Quick setup:
------------

--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--

This setup uses custom pre-built Docker images available on docker hub 
(https://hub.docker.com/r/jplarochedocker) and docker-compose.

Prerequisites:
- a running local Docker environment
- a running local or remote Kafka environment

--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--

1. On your docker dev machine:

    ```sh
    git clone https://github.com/jplaroche2000/striim.git
    ```

    ```sh
    cd striim/docker
    ```

    then edit docker-compose.yml/extra_hosts section to reflect the public IP of your Kafka host.

    Ex.:  
    
    extra_hosts:
      
      `-` "zoo1:34.95.11.111"  

    **THIS ASSUMES YOU HAVE A KAFKA BROKER HOST NAMED zoo1 and advertising on port 9092**
 
 
2. Start the Docker stack

    ```sh
    docker stack deploy --compose-file docker-compose.yml striim_kafka
    ```

3. To stop the Docker stack

    ```sh
    docker stack rm striim_kafka
    ```

4. Access striim and add application

   http://<Docker host public IP>
   admin/admin
   
   In web console
   APPS > + AddApp > Import Existing App
   
   Load tql fle located at root of git project:
   striim/Test-MultiFeeds.tql
   
   Deploy App
   
   Start App
   
 5. Access database and modify records to see changes replicated to Kafka topics
 
   ```sh
   sqlplus scott/tiger@//<Public IP of oracle container>:1521/XE
   ```
   
   
   
   