# SimpleHttpServerTemplate

This Java project, created using Eclipse, implements a simple HTTP server that serves static files and handles various HTTP requests. It includes functionalities for serving static files, handling JSON data, and interacting with a MySQL database to manage recipes.

## Usage

This application serves as a basic HTTP server, providing functionalities for serving static files, handling GET and POST requests, and interacting with a MySQL database. It demonstrates how to create a simple HTTP server in Java and handle various types of HTTP requests.

## Requirements

- Java
- Eclipse IDE
- MySQL
  
![image](https://github.com/GallonSchimmer/SimpleHttpServerTemplate/assets/26065891/7f8c3c7b-25fc-43df-8bdb-56ecc3732068)

## Getting Started

To get started with this project, you can follow these steps:

1. Clone the repository.
2. Open the project in Eclipse.
3. Configure the MySQL database with the necessary tables for managing recipes.
4. Run the `SimpleHttpServer` class to start the HTTP server.
5. Use a web browser or a tool like `curl` to send HTTP requests to the server.



## Classes

### SimpleHttpServer

The `SimpleHttpServer` class serves as the main entry point for the HTTP server. It initializes the server, handles static file requests, and delegates other requests to the `MyHandler` class.

### MyHandler

The `MyHandler` class implements the `HttpHandler` interface and handles various types of HTTP requests. It manages GET and POST requests for serving recipes and adding new recipes to the database.

### RecipeService

The `RecipeService` class interacts with the MySQL database to manage recipes. It includes methods for adding new recipes, retrieving all recipes, and initializing recipes from the database.

## Frontend

The project includes a simple HTML frontend for displaying and adding recipes. The `index.html` file fetches and displays recipes from the server and allows users to add new recipes.

Feel free to explore this project and experiment with the HTTP server and recipe management functionalities. You can enhance the project by adding more features, improving error handling, and implementing additional CRUD operations for recipes.

# MySQL for Testing
CREATE DATABASE recipesDatabase; 
USE recipesDatabase;

CREATE TABLE recipes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);

show tables;
select * from recipes;

INSERT INTO recipes (name, description)
VALUES ('German Potato Pancakes', 'These hearty, crispy fried potato pancakes are simple to make and delicious to eat! They can be served with a sweet, apple sauce, a savory garlic sauce, or a classic creamy quark!');

## Html
- WebPage example:
![image](https://github.com/GallonSchimmer/SimpleHttpServerTemplate/assets/26065891/d8a4007c-5d5e-43ab-b381-8f5245b60809)

## URL
http://localhost:8004/

## Contributors

- https://github.com/GallonSchimmer - Initial work

Feel free to update this README as you make changes or add more features to your SimpleHttpServer project.

Make sure to update the README as you continue to develop the project and add more functionalities or components.
