package com.example.amit.bakingapp;

import java.util.ArrayList;

class Ingredients {
     int       quantity;
     String    measure;
     String    ingredient;
}

class Steps {
    int       id;
    String    shortDescription;
    String    description;
    String    videoURL;
    String    thumbnailURL;
}

public class RecipeInfo {

     String    name;
     int       id;
     int       servings;
     String    image;
     ArrayList<Ingredients> ingredients = new ArrayList<>();
     ArrayList<Steps> steps = new ArrayList<>();

     public RecipeInfo()
     {

     }

}
