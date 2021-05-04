#!/bin/bash

if [[ -z $1 ]]; then
  echo """
HOW TO RUN:

  ./deploy.sh <service_name> <project>

Example:

./deploy.sh iam-service fhs_product
  """

  exit 1
fi

service_name=$1
project_name=$2
cluster_name=$(gcloud container clusters list --project ${project_name} | tail -1 | awk '{print $1}')
cluster_location=$(gcloud container clusters list --project ${project_name} | tail -1 | awk '{print $2}')

#Build docker image
echo "------------BUILD------------"
./builddocker.sh ${service_name} ${project_name} ..

#Deploy
echo "------------DEPLOY-----------"
echo "Cluster: ${cluster_name} - ${cluster_location} - ${project_name}"
gcloud container clusters get-credentials ${cluster_name} --region ${cluster_location} --project ${project_name}
kubectl delete deployments test-service
kubectl apply -f ../deployment/deployment.yaml
