package com.example.httpserver;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;


import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;


import com.sun.net.httpserver.HttpContext;


import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;





public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8004;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
     // Create a static file context for serving CSS, JS, etc.
        HttpContext staticFileContext = server.createContext("/static");
        staticFileContext.setHandler(SimpleHttpServer::handleStaticFileRequest);

        server.createContext("/", new MyHandler());
        server.setExecutor(null); // Use the default executor

        server.start();

        System.out.println("Server is listening on port " + port);
        //netstat -ano | find "8004"

    }
    
    // Handler for serving static files
    private static void handleStaticFileRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String filePath = "resources/static" + path; // Assuming static files are in the resources folder

        File file = new File(filePath);
        if (file.exists()) {
            byte[] fileData = readBytesFromFile(file);

            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", getContentType(filePath));
            headers.set("Content-Length", String.valueOf(fileData.length));

            exchange.sendResponseHeaders(200, fileData.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(fileData);
            outputStream.close();
        } else {
            // File not found, return a 404 response
            String response = "File not found.";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }
    
 // Helper method to determine the content type based on the file extension
    private static String getContentType(String filePath) {
        if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        }
        // Add more content types as needed

        return "application/octet-stream"; // Default content type for unknown file types
    }
    
 // Helper method to read bytes from a file
    private static byte[] readBytesFromFile(File file) throws IOException {
        byte[] fileData = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileData);
        }
        return fileData;
    }
    
    
    
    
    
    
    
    static class MyHandler implements HttpHandler {
    	
        private final RecipeService recipeService; // Inject the RecipeService here

        
        
        public MyHandler() {
            // Initialize the RecipeService (and other required services) here
            this.recipeService = new RecipeService();
        }

        
        
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            
        	// Enable CORS
        	Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "http://localhost:8001"); // Replace "http://localhost:8001" with the actual frontend URL
            headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            headers.add("Access-Control-Max-Age", "86400");
        	
        	String path = exchange.getRequestURI().getPath();
            String response;
            int statusCode = 200;
            
            
            
            //in powershell instead of browser    
            //Invoke-WebRequest -Uri http://localhost:8001
            
            // Handle the root path "/" to serve index.html
            if (path.equals("/")) {
                serveIndexPage(exchange);
                return;
            }

            if (path.equals("/recipes")) {
                List<Recipe> recipes = recipeService.getAllRecipes();
                JsonArray recipesJsonArray = convertRecipesToJsonArray(recipes);
                sendJsonResponse(exchange, recipesJsonArray, 200); // Send the JSON response
                return;
            } else if (path.equals("/addRecipe") && exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                String requestBody = getRequestData(exchange);
                Recipe newRecipe = parseRecipeFromJson(requestBody);
                recipeService.addRecipe(newRecipe);
                response = "Recipe added successfully!";
            } else if (path.equals("/favicon.ico")) {
                // Return an empty response for favicon requests
                response = ""; 
            
            }else {
                // Return a "Not Found" response for other invalid paths
                response = "Not Found";
                statusCode = 404;
            }
            
         // Debugging: Log the response content length and body
            System.out.println("Response Content Length: " + response.getBytes().length);
            System.out.println("Response Body: " + response);
 
         // Calculate the content length
            byte[] responseBodyBytes = response.getBytes(StandardCharsets.UTF_8);
            int contentLength = responseBodyBytes.length;

            // Set the Content-Length header
            headers.add("Content-Length", Integer.toString(contentLength));


            // Send the response
            sendResponse(exchange, response, statusCode);
        }
        
        
        
        
        
        private JsonArray convertRecipesToJsonArray(List<Recipe> recipes) {
            // Convert the list of Recipe objects to a JSON array
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Recipe recipe : recipes) {
                JsonObject jsonObject = Json.createObjectBuilder()
                        .add("name", recipe.getName())
                        .add("description", recipe.getDescription())
                        .build();
                jsonArrayBuilder.add(jsonObject);
            }
            return jsonArrayBuilder.build();
        }

        
     

        
        
        
        
     // Helper method to send JSON response
        private static void sendJsonResponse(HttpExchange exchange, JsonArray jsonArray, int statusCode) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream responseBody = exchange.getResponseBody();
                 JsonWriter writer = Json.createWriter(responseBody)) {
                writer.writeArray(jsonArray);
            }
        }
        
        
        
        
        
        private void serveIndexPage(HttpExchange exchange) throws IOException {
            // Set the Content-Type header to "text/html"
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "text/html");

            // Read the index.html file and send it as the response
            File file = new File("resources/index.html"); // Update the path to match the location of index.html
            byte[] fileData = readBytesFromFile(file);

            // Set the Content-Length header to the length of the file data
            headers.set("Content-Length", String.valueOf(fileData.length));

            // Send the response with status code 200 (OK)
            exchange.sendResponseHeaders(200, fileData.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(fileData);
            outputStream.close();
        }
        
        
        
        
        private byte[] readBytesFromFile(File file) throws IOException {
            byte[] fileData = new byte[(int) file.length()];
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.read(fileData);
            }
            return fileData;
        }

        
        
        
        private String getRequestData(HttpExchange exchange) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                return requestBody.toString();
            }
        }

        
        
        
        private Recipe parseRecipeFromJson(String json) {
            try (JsonReader reader = Json.createReader(new StringReader(json))) {
                JsonObject jsonObject = reader.readObject();

                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");

                return new Recipe(name, description);
            } catch (JsonException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
    
    
    
    
    
    
    
    
 
    
    
    private static void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
	


/*
 * https://stackoverflow.com/questions/13155734/eclipse-cant-recognize-com-sun-net-httpserver-httpserver-package
 * */
