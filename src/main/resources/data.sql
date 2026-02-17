CREATE TABLE RECIPE (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(100) NOT NULL,
                vegetarian BOOLEAN,
                servings INT,
                instructions TEXT
);


CREATE TABLE recipe_ingredients (
    recipe_id INT NOT NULL,
    ingredient VARCHAR(255),
    FOREIGN KEY (recipe_id) REFERENCES recipe(id)
);



INSERT INTO recipe (title, vegetarian, servings, instructions)
VALUES ('Pasta Primavera', true, 4, 'Boil pasta. Sauté vegetables. Mix and serve.');


INSERT INTO recipe_ingredients (recipe_id, ingredient)
VALUES
    (1, 'Pasta'),
    (1, 'Bell Peppers'),
    (1, 'Olive Oil'),
    (1, 'Garlic'),
    (1, 'Parmesan');
