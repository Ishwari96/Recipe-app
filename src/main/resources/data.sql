CREATE TABLE RECIPE (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(100) NOT NULL,
                vegetarian BOOLEAN NOT NULL,
                servings INT NOT NULL,
                instructions TEXT
);

INSERT INTO recipe (title, vegetarian, servings, instructions)
VALUES ('Pasta Primavera', true, 4, 'Boil pasta. Sauté vegetables. Mix and serve.');
insert into recipe (title, vegetarian, servings, instructions)
values ('Sandwich',true, 4,'Toast bread. Add Cheese. Add cucumber.');
insert into recipe (title, vegetarian, servings, instructions)
values ('Meat Sandwich',false, 4, 'Make Dough. add sauce, add meat.');
insert into recipe (title, vegetarian, servings, instructions)
values ('Meat Pizza', false, 3, 'Make Dough. add sauce, add meat. Put in Oven');



CREATE TABLE INGREDIENTS (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         recipe_id BIGINT NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         description VARCHAR(500) NOT NULL,
                         CONSTRAINT fk_ingredients_recipe
                             FOREIGN KEY (recipe_id)
                                 REFERENCES RECIPE(id)
                                 ON DELETE CASCADE
);

insert into INGREDIENTS (recipe_id, name, description) values (1,'Sauce','Tomato Sauce');
insert into INGREDIENTS (recipe_id, name, description) values (1,'Tomatoes','Tomatoes');
insert into INGREDIENTS (recipe_id, name, description) values (1,'Capsicum','Chopped Capsicum');
insert into INGREDIENTS (recipe_id, name, description) values (2,'Sauce','Tomato Sauce');
insert into INGREDIENTS (recipe_id, name, description) values (2,'bread','wheat bread');
insert into INGREDIENTS (recipe_id, name, description) values (2,'Cheese','Cow milk Cheese');
insert into INGREDIENTS (recipe_id, name, description) values (2,'Cucumber','sliced cucumber');
insert into INGREDIENTS (recipe_id, name, description) values (3,'Meat','Meat');
insert into INGREDIENTS (recipe_id, name, description) values (3,'bread','wheat bread');
insert into INGREDIENTS (recipe_id, name, description) values (4,'Meat',' Meat');
insert into INGREDIENTS (recipe_id, name, description) values (4,'dough','Wheat Dough');