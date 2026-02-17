package com.id.recipes.recipes_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recipes", schema = "public")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length=100) private String title;

    public Integer getId(){ return id; }
    public String getTitle(){ return title; }


    public void setId(int i) { id = i; }
    public void setTitle(String tite){ title=tite; }
}
