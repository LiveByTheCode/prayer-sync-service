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

# Check if application-local.properties exists
if [ ! -f src/main/resources/application-local.properties ]; then
    echo "Creating application-local.properties..."
    
    # Create the file with basic template
    cat > src/main/resources/application-local.properties << 'EOF'
# Local development configuration
# This file is ignored by git - add your local settings here

# You can override any property from application.properties
# For example:
# spring.datasource.url=jdbc:postgresql://localhost:5432/prayersync_dev
# spring.jpa.show-sql=true
EOF
    
    echo "✓ application-local.properties created."
else
    echo "✓ application-local.properties already exists."
fi

echo ""
echo "Setup complete! Next steps:"
echo "1. Edit .env file with your database credentials"
echo "2. Run the application with: ./mvnw spring-boot:run"
echo ""
echo "To use environment variables from .env file, you can:"
echo "  - Use an IDE that supports .env files (IntelliJ IDEA, VS Code)"
echo "  - Or export them manually: export \$(cat .env | xargs)"
echo "  - Or use dotenv-cli: dotenv -- ./mvnw spring-boot:run"