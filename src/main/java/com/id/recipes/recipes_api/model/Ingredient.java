package com.id.recipes.recipes_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "ingredients")
@Getter
@Setter
public class Ingredient {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        long id;

        @Column(name = "name")
        String name;


        @Column(name = "description")
        String description;


        @Column(name = "recipe_id", nullable = false, insertable = false, updatable = false)
        private Long recipeId;

}
