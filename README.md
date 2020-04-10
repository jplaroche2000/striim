
Quick setup:
------------

Objective:

Replicating Oracle database tables to a GCP Firestore datastore with Striim and Kafka

--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--

Prerequisites:
- a Docker Hub account (to pull Oracle JDK 8 image)
- a running local Docker environment with at least 15 GB of disk storage and 7.5 GB RAM ([setup instructions for GCP -Debian OS](https://docs.docker.com/install/linux/docker-ce/debian/))
- a running local or remote Kafka environment ([setup instructions for GCP - Debian OS](https://github.com/jplaroche2000/striim/blob/master/kafka/Build%20a%20Kafka%20Cluster%20on%20GCP.pdf))
- a GCP Firestore database in [Datastore mode](https://cloud.google.com/datastore/docs/quickstart)
- a GCP service account to access your GCP Firestore datastore.  To create one follow the steps descibed [here](https://cloud.google.com/iam/docs/creating-managing-service-account-keys#iam-service-account-keys-create-console), and copy the service account json file under striim/docker/java/ of the cloned git project.

--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--/--

1. On your Docker machine:

    ```sh
    git clone https://github.com/jplaroche2000/striim.git
    ```

    ```sh
    cd striim/docker
    ```

    a. Edit docker-compose.yml/extra_hosts sections to reflect the public IP of your Kafka broker(s).

    >extra_hosts:
    
    >`-` "zoo1:34.95.11.111" 
    
    >`-` "zoo2:34.95.11.112"  

    **THIS ASSUMES YOU HAVE KAFKA BROKER HOSTS NAMED zoo1 and zoo2 and advertising on port 9092**
 
    b. Edit docker-compose.yml/java-kstream/environment section to reflect the name of the service account json file, the GCP project where Firebase is activated and one of the Kafka broker hostname
    
    >environment:
    
    >GOOGLE_APPLICATION_CREDENTIALS: "/u01/oracle/XXXXXXXXXXXXXXX.json"
    
    >DS_PROJECT_ID: "my-gcp-project"
    
    >KAFKA_BROKER: "zoo1"


2. Build the custom images

    ```sh
    docker-compose build
    ```
    If the Striim image build fails, see troobleshooting section below

3. Start the Docker stack

    ```sh
    docker-compose up -d
    ```


4. Access striim and add application

    http://`<`Docker host public IP`>`
    
    admin/admin
   
    In Striim web console:
    
    APPS > + AddApp > Import Existing App
   
    
    Load tql fle located at root of git project:
    
    striim/Test-MultiFeeds.tql
   
    Deploy App
   
    Start App
   
5. Access database and modify records to see changes replicated to Kafka topics
 
    ```sh
    sqlplus scott/tiger@//<Public IP of oracle container>:1521/XE
    ```
   
6. To stop the Docker stack

    ```sh
    docker-compose down
    ```  

Troobleshooting:
---------------

The Striim pre-built docker image (striim/evalversion) has been missing from time to time.  You will need to download the binary install and configure it if the striim image build fails - https://www.striim.com/docs/en/creating-a-cluster-in-ubuntu.html

