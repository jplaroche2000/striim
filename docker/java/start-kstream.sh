#!/usr/bin/env sh

#!/bin/sh -e -x -u

trap "echo TRAPed signal" HUP INT QUIT KILL TERM

CMD="${JAVA_HOME}/bin/java -jar gcp.poc-exec.jar"

echo "Starting Java Kafka streaming using ${CMD}"

exec ${CMD}
