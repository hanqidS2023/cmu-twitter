#!/bin/bash


while ! nc -z mysql 3306; do
    echo "Waiting for MySQL..."
    sleep 1
done

# Start the application
java -jar target/postservice-0.0.1-SNAPSHOT.jar