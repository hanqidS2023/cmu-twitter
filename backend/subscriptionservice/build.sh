#!/bin/bash

# This script builds Docker images for multiple platforms and pushes them to a local registry.
set -e

IMAGE_NAME="raystudyhardworkhard/subscription-service"
IMAGE_TAG="latest"

# Build and push to local registry
docker buildx build --platform linux/amd64,linux/arm64 \
  -f Dockerfile \
  -t $IMAGE_NAME:$IMAGE_TAG . \
  --push

echo "Docker image for multiple platforms pushed to local registry."
