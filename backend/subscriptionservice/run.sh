#!/bin/bash

IMAGE_NAME="subscription-service"
IMAGE_TAG="latest"

CONTAINER_NAME="SubscriptionServiceContainer"
PORT_MAPPING="-p 8082:8082"

# Run the Docker image as a container
docker run -d --name $CONTAINER_NAME $PORT_MAPPING $IMAGE_NAME:$IMAGE_TAG

# Optional: Show a message that the container is running
echo "Docker container $CONTAINER_NAME is running"
