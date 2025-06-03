#!/bin/bash

echo "Setting up Prayer Sync Backend environment..."

# Check if .env file exists
if [ ! -f .env ]; then
    echo "Creating .env file from .env.example..."
    cp .env.example .env
    echo "✓ .env file created. Please update it with your database credentials."
else
    echo "✓ .env file already exists."
fi

# Note: application-local.properties is no longer needed
# All configuration is done through environment variables

echo ""
echo "Setup complete! Next steps:"
echo "1. Edit .env file with your database credentials"
echo "2. Run the application with: ./mvnw spring-boot:run"
echo ""
echo "To use environment variables from .env file, you can:"
echo "  - Use an IDE that supports .env files (IntelliJ IDEA, VS Code)"
echo "  - Or export them manually: export \$(cat .env | xargs)"
echo "  - Or use dotenv-cli: dotenv -- ./mvnw spring-boot:run"