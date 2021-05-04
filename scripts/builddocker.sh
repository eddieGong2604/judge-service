#!/bin/sh

if [[ -z $1 ]] || [[ -z $2 ]]; then
  echo """
HOW TO RUN:

  ./builddocker.sh <service_name> <project> <path_to_pom_file>

Example:

  ./builddocker.sh iam-service fhs-product ..
  """
    
  exit 1
fi

service_name=$1
project=$2
path_to_pom=$3
image_name="asia.gcr.io/${project}/${service_name}"

cd ${path_to_pom}
mvn clean install -Dmaven.test.skip=true
docker build --build-arg service_name=test-service -t asia.gcr.io/aqueous-tube-251208/test-service:0.1.0 ..
docker push asia.gcr.io/aqueous-tube-251208/test-service:0.1.0
