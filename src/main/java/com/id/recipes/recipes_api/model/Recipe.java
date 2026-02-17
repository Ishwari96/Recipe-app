package com.id.recipes.recipes_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "recipe", schema = "public")
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length=100) private String title;

    private boolean vegetarian;
    private int servings;
    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;
    @Column(columnDefinition = "TEXT")
    private String instructions;
}
