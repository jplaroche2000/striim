#!/usr/bin/env sh

#!/bin/sh -e -x -u

trap "echo TRAPed signal" HUP INT QUIT KILL TERM

CMD="${JAVA_HOME}/bin/java -cp ${CLASSPATH}"./* gcp.poc.kafka.streams.KafkaToFirestoreStream"

echo "Starting Java Kafka streaming using ${CMD}"

exec ${CMD}
