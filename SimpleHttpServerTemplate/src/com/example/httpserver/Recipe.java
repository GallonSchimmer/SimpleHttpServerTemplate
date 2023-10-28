package com.example.httpserver;

public class Recipe {
    private String name;
    private String description;

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters for name and description (you can generate them in your IDE)

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// You may also want to override the toString() method to provide a custom string representation
    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
