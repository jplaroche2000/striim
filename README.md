# striim

Run Oracle and Striim Docker images
==================================

Oracle
------

sudo rm -rf /home/oracle/oradata
sudo mkdir /home/oracle/oradata
sudo chown oracle:oracle /home/oracle/oradata
sudo chmod a+rwx /home/oracle/oradata

Direct run:
docker run --name oracle-database-11.2.0.2-xe --shm-size=1g -p 1521:1521 -p 8080:8080 -e ORACLE_PWD=tiger -v /home/oracle/scripts/setup:/u01/app/oracle/scripts/setup -v /home/oracle/scripts/startup:/u01/app/oracle/scripts/startup -v /home/oracle/oradata:/u01/app/oracle/oradata -d oracle/database:11.2.0.2-xe


Striim
------

Direct run:
docker run --name striim-3.7.4 -d -p 80:9080 -e "STRIIM_ACCEPT_EULA=Y"  striim/evalversion

deploy jdb driver:
docker exec -ti striim-3.7.4 
cd lib
curl https://s3.amazonaws.com/jpl.s3bucket/ojdbc7.jar -o ojdbc7-12.1.0.2.jar

docker restart striim-3.7.4

adjust /etc/hosts:
cat >> /etc/hosts
34.95.11.111    kafka_1
35.225.220.10   zoo1
34.66.94.46     zoo2


Swarm:
------

Custom images must be used:

cd ~/git-projects/striim/docker
docker build -t custom-striim -f Dockerfile.striim .
docker build -t custom-oracle -f Dockerfile.oracle .

docker stack deploy --compose-file docker-compose.yml striim_kafka
