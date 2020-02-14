
Quick setup:
------------

--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--

This setup uses custom pre-built Docker images available on docker hub 
(https://hub.docker.com/r/jplarochedocker) and docker-compose.

Prerequisites:
- a running local Docker environment ([setup instructions for GCP -Debian OS](https://docs.docker.com/install/linux/docker-ce/debian/))
- a running local or remote Kafka environment ([setup instructions for GCP - Debian OS](https://github.com/jplaroche2000/striim/blob/master/kafka/Build%20a%20Kafka%20Cluster%20on%20GCP.pdf))

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
      
      `-` "zoo2:34.95.11.112"  

    **THIS ASSUMES YOU HAVE KAFKA BROKER HOSTS NAMED zoo1 and zoo2 and advertising on port 9092**
 
 
2. Start the Docker stack

    ```sh
    docker stack deploy --compose-file docker-compose.yml striim_kafka
    ```


3. Access striim and add application

    http://`<`Docker host public IP`>`
    
    admin/admin
   
    In web console:
    
    APPS > + AddApp > Import Existing App
   
    Load tql fle located at root of git project:
    
    striim/Test-MultiFeeds.tql
   
    Deploy App
   
    Start App
   
4. Access database and modify records to see changes replicated to Kafka topics
 
    ```sh
    sqlplus scott/tiger@//<Public IP of oracle container>:1521/XE
    ```
   
5. To stop the Docker stack

    ```sh
    docker stack rm striim_kafka
    ```  
   
   
