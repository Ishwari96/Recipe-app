package com.id.recipes.recipes_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipe", schema = "public")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false, length=100) private String title;

    private boolean vegetarian;
    private int servings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "ingredients")
    @JoinColumn(name = "recipe_id", nullable = false)
    private Set<Ingredient> ingredients = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String instructions;


}
