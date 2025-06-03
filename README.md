# Prayer Sync Backend Service

Spring Boot backend service for the Prayer Sync application.

## Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL database

### Configuration

This project uses environment variables for sensitive configuration. Follow these steps:

1. **Run the setup script** (recommended):
   ```bash
   ./setup-env.sh
   ```

2. **Configure your environment**:
   - Copy `.env.example` to `.env`
   - Update `.env` with your database credentials and JWT secret

3. **Environment Variables**:
   The application uses the following environment variables:
   - `DATABASE_URL` - PostgreSQL connection URL
   - `DATABASE_USERNAME` - Database username
   - `DATABASE_PASSWORD` - Database password
   - `JWT_SECRET` - Secret key for JWT token generation
   - `JWT_EXPIRATION` - Token expiration time in milliseconds (default: 86400000 = 24 hours)

### Running the Application

#### Option 1: Using IDE with .env support
Most modern IDEs (IntelliJ IDEA, VS Code) can read `.env` files automatically.

#### Option 2: Export environment variables manually
```bash
export $(cat .env | xargs)
./mvnw spring-boot:run
```

#### Option 3: Using Spring profiles
```bash
./mvnw spring-boot:run -Dspring.profiles.active=local
```

### Development Profiles

- **default**: Uses environment variables with fallback values
- **local**: Uses `application-local.properties` (not tracked in git)

### Security Notes

- Never commit `.env` or `application-local.properties` files
- Always use strong, unique JWT secrets in production
- Rotate database credentials regularly
- Use different credentials for different environments

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/auth/logout` - Logout user

### Users
- `GET /api/users/me` - Get current user profile (requires auth)

### Churches
- `GET /api/churches` - List all churches
- `GET /api/churches/{id}` - Get church details
- `POST /api/churches` - Create new church (requires auth)

### Prayer Requests
- `GET /api/prayers` - List prayer requests (requires auth)
- `POST /api/prayers` - Create prayer request (requires auth)
- `PUT /api/prayers/{id}` - Update prayer request (requires auth)
- `DELETE /api/prayers/{id}` - Delete prayer request (requires auth)