# Collabflow

Hey everyone! 👋 This is Collabflow, a little side project I've been working on. It's basically a microservices-based task manager (think a very simplified Jira or Trello clone). I built it to get more hands-on experience with Spring Boot, API Gateways, and WebSockets.

## What's under the hood?

I decided to split the app into a few different services to keep things decoupled:

*   **API Gateway**: Built with Spring Cloud Gateway. It's the main entry point and handles all the JWT token validation so the other services don't have to worry about it.
*   **User Service**: Handles signups, logins, and issues those JWTs.
*   **Task Service**: The meat of the app. It handles creating projects and tasks. I threw Redis in here to cache the task lists so it's super snappy.
*   **Notification Service**: A neat little STOMP WebSocket server. It listens to a Redis Pub/Sub channel and broadcasts task updates to the frontend in real-time.

**Tech stack:** Java 21, Spring Boot 3.5, PostgreSQL, Redis, and WebSockets.

## How to run it locally

You'll need Docker and Java 21 installed.

1.  **Spin up the databases**
    Instead of installing Postgres and Redis locally, I set up a `docker-compose.yml` that handles the infrastructure. Just run:
    ```bash
    docker-compose up -d
    ```
    This spins up two Postgres databases (one for users, one for tasks) and a Redis instance, and maps them to your localhost ports.

2.  **Build the code**
    ```bash
    mvn clean install -DskipTests
    ```

3.  **Boot up the services**
    You can run these straight from your IDE or terminal. You'll want to start them in this order:
    -   `user-service` (runs on 8081)
    -   `task-service` (runs on 8082)
    -   `notification-service` (runs on 8083)
    -   `gateway` (runs on 8080)

## Playing around with the API

Since there's no frontend yet, you can test the flow using something like Postman, Insomnia, or just good ol' `curl`. Everything goes through the Gateway on port `8080`.

**1. Register & Login:**
```bash
# Register
curl -X POST http://localhost:8080/auth/register \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com", "name":"Alice", "password":"password123"}'

# Login (if you already have an account)
curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com", "password":"password123"}'
```
*(Both endpoints return your user ID and a JWT token)*

**2. Manage Projects:**
Copy the JWT token from the registration/login step and use it here:
```bash
# Create a project
curl -X POST http://localhost:8080/projects \
     -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{"name":"My First Project", "description":"Building collabflow"}'

# Get a project
curl -X GET http://localhost:8080/projects/<PROJECT_ID> \
     -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

**3. Manage Tasks (The cached data!):**
```bash
# Create a task
curl -X POST http://localhost:8080/tasks \
     -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{"projectId":"<PROJECT_ID>", "title":"Write the README", "description":"Make it look human"}'

# Update a task (Triggers a WebSocket broadcast and evicts cache)
curl -X PATCH "http://localhost:8080/tasks/<TASK_ID>?projectId=<PROJECT_ID>" \
     -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{"status":"IN_PROGRESS"}'

# Fetch all tasks for a project (This hits the Redis Cache!)
curl -X GET http://localhost:8080/projects/<PROJECT_ID>/tasks \
     -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

**4. Real-time Pub/Sub (WebSockets):**
Because Spring Boot uses STOMP over WebSockets (which requires specific framing and null characters), testing raw WebSockets in generic API tools can be frustrating. 

To make testing seamless, this project includes a built-in test client:
1. Open the `websocket-tester.html` file (located in the root directory) directly in your web browser.
2. Enter your `<PROJECT_ID>` and click **Connect & Subscribe**.
3. Fire the `PATCH` or `POST` task curl commands from Step 3 in your terminal.
4. Watch the real-time JSON updates appear instantly in your browser!

## Roadmap (v1.1.0)

This project is currently a functional MVP (v1.0.0). The following improvements are planned for the upcoming v1.1.0 release:

* **Global Exception Handling**: Graceful error handling and unified HTTP responses using `@ControllerAdvice`.
* **Data Validation**: Implementing `@Valid` annotations on incoming DTOs (e.g., ensuring valid emails and passwords).
* **Unit Testing**: Adding robust test coverage for core business logic in the microservices using JUnit and Mockito.

## License
MIT
