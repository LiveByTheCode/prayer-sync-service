#!/bin/bash

# Load environment variables from .env file if it exists
if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    export $(cat .env | grep -v '^#' | xargs)
else
    echo "Warning: .env file not found. Using default values."
    echo "Run ./setup-env.sh to create one."
fi

# Run the Spring Boot application (uses default profile with env vars)
echo "Starting Prayer Sync Backend..."
./mvnw spring-boot:run