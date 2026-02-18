# Recipe-app
Recipe API
-----
## Running the application

Clone the repository to your local

```
https://github.com/Ishwari96/Recipe-app.git

```

Go to root directory of the code. Run following command to run the application.

```
mvn clean install

```

Now the application is build to run. Use following command to run spring boot application on command prompt.

```
mvn spring-boot:run

```

In case, If you want to run the application directly from IDE. Import the project to your favorite IDE as 'Existing maven project'. Select project from the IDE and run it as java application. “Don't forget to update maven ;)” I have configured the port to 8882 in properties. So the application can be accessed by `http://localhost:8882`

Application is using H2 inmemory database. Following is the details of connection.
- `http://localhost:8444`
- spring.datasource.url=jdbc:h2:mem:testdb
- spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
- spring.datasource.driverClassName = org.h2.Driver
- spring.datasource.platform=h2
- spring.datasource.username = sa
- spring.datasource.password =

Swagger is integrated for easy access of API. It can be accessed via `http://localhost:8444/swagger-ui/index.html#/`

## API details

- Create Recipe
    - Create recipe service is used to create new recipe. Following is the api url and sample json. It is POST request which accepts JSON body for recipe.

```
		POST 
		'Content-Type: application/json'
		'/api/v1/recipe/'
		
		'Accept: application/json' -d 
		'{ \ 
		   "cookingInstruction": "string", \ 
		   "ingredients": [ \ 
		     { \ 
		       "ingredientDesciption": "string", \ 
		       "ingredientName": "string" \ 
		     } \ 
		   ], \ 
		   "name": "string", \ 
		   "numberOfPerson": 0, \ 
		   "vegetarian": true \ 
		}' 
		
	
```
- Update Recipe
    - Update recipe service is used to update existing recipe. Following is the api url and sample json. It is PUT request which accept JSON body for recipe alongwith ID as path parameter.

```
		PUT 
		'Content-Type: application/json' 
		'/api/v1/recipe/{id}'
		
		'Accept: application/json' -d 
		'{ \ 
		   "id": 5, \ 
		   "name": "string test", \ 
		   "createDate": "2020-10-26 13:26", \ 
		   "numberOfPerson": 0, \ 
		   "cookingInstruction": "string", \ 
		   "ingredients": [ \ 
		     { \ 
		       "id": 10, \ 
		       "ingredientName": "string", \ 
		       "ingredientDesciption": "string" \ 
		     } \ 
		   ], \ 
		   "vegetarian": true \ 
		}'	 
		

```

- Get All Recipes
    - Get all recipe service is used to get list of all the recipes. Following is the api url. It is GET request.

```
		GET 
		'Accept: application/json' 
		'/api/v1/recipe/'

```

- Find Recipe By Id
    - Find the recipe by id can be used to fetch particular Recipe. Following is the api url. It is GET request which accept ID as path parameter.

```	
		GET 
		'Accept: application/json' 
		'/api/v1/recipe/{id}'

```

- Delete Recipe By Id
    - Delete the recipe by id can be used to delete particular Recipe. Following is the api url. It is DELETE request which accept ID as path parameter.

```	
		GET 
		'Accept: application/json' 
		'/api/v1/recipe/{id}'

```

- Search Recipe By Criteria
    - This method searches for the recipe that fits into the given criteria. User can search by any field of the Recipe. Following are the fields of the recipe.
        - name
        - numberOfPerson
        - cookingInstruction
        - vegetarian
    - Search criteria needs field to search for with operator to apply on field.
        - isVegetarian:true = here : is used to check equality. This criteria mean all the recipes which are vegetarian.
        - numberOfPerson>5 = This is self-explanatory. It will bring all the recipes which can be served to more than 5 person.
    - For now, I have implemented 3 operators, "<, >, :(==)"
    - Search criteria can also be put together with comma separated values.
        - isVegetarian:true,numberOfPerson>5 = This will query the database with both the criteria "AND" operator.
        - isVegetarian:true,'numberOfPerson>5 = This will query the database with both the criteria "OR" operator.

```
	GET 
	'Accept: application/json' 
	'/api/v1/recipe/searchByCriteria/isVegetarian%3Afalse'
```

## Test Scripts

Postman test script is provided in postman/ folder. It can be used by importing script and environment to postman. Don't forget to select environment and whitelist host.

## Clarifications
As per the requirement given, User should be able to search by all 5 fields of the recipe. Instead of creating different method. It is more feasible and reusable to create advance search functionality. I have not completed whole functionality currently, but it is reusable.

All the validations are imaginary, it is only for the demo purpose.

httpSecurity.csrf().disabled() is only used for demo purpose. It needs to be enabled for actual API. This is only to make it available to postman for testing.

Code coverage is not considered for model, repository and dto classes.
