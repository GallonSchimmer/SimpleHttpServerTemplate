package com.example.httpserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class RecipeService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/recipesDatabase"; // Replace 'mydatabase' with your actual database name
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private List<Recipe> recipes;

    public RecipeService() {
        this.recipes = new ArrayList<>();
        initializeRecipes();
    }

    private void initializeRecipes() {
        // Load recipes from the database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT name, description FROM recipes";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                recipes.add(new Recipe(name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
 
    }
    

    

    
    public void addRecipe(Recipe recipe) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO recipes (name, description) VALUES (?, ?)")) {
            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT name, description FROM recipes";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                recipes.add(new Recipe(name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }




    // Add other methods for CRUD operations and other recipe-related logic as needed
}





