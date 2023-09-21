# DevVault - Project Management Spring Boot Application

Welcome to DevVault, your go-to project management tool powered by Spring Boot! 🚀

## Table of Contents
- [Introduction](#introduction)
- [Functionalities](#functionalities)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Security Implementation](#security-implementation)
- [Lessons Learned](#lessons-learned)
- [Contributing](#contributing)
- [License](#license)

## Introduction

DevVault is a comprehensive project management application built using Spring Boot and microservices architecture. It provides a seamless experience for project leaders and team members to collaborate effectively. With DevVault, you can register users, create projects, manage project members, create tasks, and assign tasks to team members.

## Functionalities

DevVault offers the following key functionalities:

1. **User Registration and Authentication**: Users can register and log in using JWT tokens for secure access.

2. **Project Creation**: Users can create new projects, becoming the project leader in the process.

3. **Adding Members to Projects**: Project leaders can add members to their projects and specify their roles as project admins or team members.

4. **Task Creation**: Users can create tasks within projects, enabling structured project management.

5. **Task Assignment**: Project leaders can assign tasks to specific project members.

6. **Task Progress Tracking**: Users can mark tasks as completed, keeping everyone informed about project progress.

7. **Project and Task Comments**: Users can comment on projects and tasks, fostering collaboration and communication among team members.

## Technologies Used

DevVault leverages several technologies and microservices:

| Technology             | Description                              |
|------------------------|------------------------------------------|
| Spring Boot            | The foundation for building microservices.|
| JWT Authentication     | Secure user registration and login.      |
| Feign Client           | Service communication between microservices.|
| Eureka Service Registry| For service discovery.                   |
| Netflix Zuul API Gateway| Routes requests and applies filters.   |
| Config Server          | Manages configuration properties.         |
| Shared Library         | Reusable classes for multiple services.   |

## Project Structure

DevVault is organized into microservices:

```text
devvault/
│
├── api-gateway/
│
├── service-registry/
│
├── authentication-service/
│
├── project-service/
│
├── task-service/
│
├── comment-service/
│
├── config-server/
│
└── shared-lib/
```

Each microservice is responsible for a specific aspect of the application, promoting modularity and maintainability.

## Security Implementation

Security is a top priority in DevVault. Here's how it works:

1. When a user makes a request, it goes through the API Gateway filter.

2. The API Gateway filter redirects the request to the Authentication Service.

3. Upon successful login, a JWT token is applied to the API Gateway filter, granting access.

## Lessons Learned

Through the development of DevVault, I've gained valuable insights and expertise, including:

- Proficiency in Spring Boot and microservices architecture.
- Implementation of secure user authentication with JWT.
- Effective communication between microservices using Feign Client.
- Service discovery with Eureka and routing with Zuul API Gateway.

## Contributing

Contributions to DevVault are welcome! If you have suggestions, find issues, or want to contribute, please create a GitHub issue or submit a pull request.

## License

DevVault is open-source and licensed under the [MIT License](LICENSE). Feel free to use, modify, and share it as needed.

Happy coding with DevVault! 🌟
