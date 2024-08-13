# Computational Graph Project
## Shoham Galili ID: 208010785

Welcome to the Computational Graph project repository. This project demonstrates the implementation of a computational graph using a publisher/subscriber architecture in Java. The project includes components for managing agents, configurations, and an HTTP server with servlets for handling various request types.

## Overview

This project aims to provide an understanding of how complex libraries function by creating a simplified version that highlights their core principles. We have implemented various design patterns and architectural components, including a generic server and diverse client-side elements.

The system employs a publisher/subscriber architecture to create a computational graph designed for complex calculations. This graph consists of computational nodes operating concurrently, where the output of one node serves as the input for another. For example, one node reads a signal from a video camera and publishes a compressed image. Another node subscribes to this image, processes it using a neural network to detect humans, and publishes an array of their "skeletons." A third node subscribes to this data and attempts to recognize the visual gesture made by the person in the image.

## Terminology

- **Message**: A data packet containing relevant information.
- **Topic**: A subject to which one can subscribe to receive messages or publish messages for others.
- **Agent**: A software component that can subscribe to Topics, process received messages, and publish results to other Topics. Each Agent can subscribe to multiple Topics and publish messages to various Topics.

## Project Layers

The project is divided into several programming exercises:

1. **Model Layer**: Infrastructure for creating and computing a computational graph.
2. **Controller Layer**: A library for a generic server that supports a RESTful API.
3. **View Layer**: A web (and mobile) application that allows loading and operating computational graphs remotely through a browser.

## Project Structure

The repository is organized into several directories:

- **configs.configs**: Contains configurations (Agent, MathExampleConfig, etc.) for initializing agents based on external data.
- **graph**: Implements agent management (Agent, Message) and communication (TopicManagerSingleton) within the computational graph.
- **server**: Contains the HTTP server implementation (HTTPServer, MyHTTPServer) and request handling (RequestParser) logic.
- **servlets**: Provides servlet implementations (CalculateServlet, CalculatorServlet, etc.) for processing specific HTTP requests.

## Features

- **Agent Management**: Uses an agent-based architecture where agents perform specific operations based on configurations loaded from files.
- **HTTP Server**: Implements a multithreaded HTTP server (MyHTTPServer) capable of handling GET, POST, and DELETE requests concurrently using servlets.
- **Servlets**: Includes servlet implementations for handling various types of requests (CalculateServlet, CalculatorServlet, etc.) and generating appropriate responses.

## Getting Started

To begin using the Computational Graph project:

1. **Clone the Repository**: Clone this repository to your local machine:

   ```sh
   git clone https://github.com/your-username/your-project-repo.git
   cd your-project-repo
   ```

2. **Build and Run**: Ensure you have the Java Development Kit (JDK) installed. Compile and run the project using your IDE or command line tools.

3. **Explore the Code**: Review the code in each directory (configs.configs, graph, server, servlets) to understand how agents, configurations, and the HTTP server are implemented.

4. **Test the Server**: Use HTTP clients like Postman or curl to test the server with various HTTP requests (GET, POST, DELETE) against different servlet endpoints.

5. **Contribute**: Enhance existing functionality, add new features, improve documentation, or fix bugs. Submit pull requests for review and collaboration.

## Usage Examples

### Example 1: Running the HTTP Server

To run the HTTP server (MyHTTPServer), instantiate it with a port number and number of threads, add servlets for handling requests, and start the server:

```java
MyHTTPServer server = new MyHTTPServer(8080, 20); // Port 8080, 20 threads
server.addServlet("GET", "/calculate", new CalculateServlet());
server.addServlet("POST", "/calculator", new CalculatorServlet());
server.start();
```

### Example 2: Handling GET Request with CalculateServlet

Send a GET request to `http://localhost:8080/calculate?a=15&b=2&op=add` to get the result of adding numbers 10 and 5:

```
Result: 17
```

## Dependencies

- **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed to compile and run the project.
- **External Libraries**: No external libraries are required beyond the standard JDK for basic functionality.

## Conclusion

This project illustrates the implementation of a computational graph using a publisher/subscriber architecture in Java. It includes agent management, a multithreaded HTTP server, and various servlet implementations to handle HTTP requests, showcasing how complex systems can be built using straightforward yet effective design patterns.

## Project Demo link:
https://youtu.be/vvcCjH5Rdwg

Hope you enjoy! 
Shoham & Hadas
