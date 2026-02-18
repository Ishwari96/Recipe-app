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
                ' {
                  "title": "string",
                  "vegetarian": true,
                  "numberOfPerson": 50,
                  "cookingInstruction": "string",
                  "ingredients": [
                    {
                      "id": 0,
                      "name": "string",
                      "description": "string",
                      "recipeId": 0
                    }
                  ]
                }' 
            
	
```
- Update Recipe
    - Update recipe service is used to update existing recipe. Following is the api url and sample json. It is PUT request which accept JSON body for recipe alongwith ID as path parameter.

```
		PUT 
		'Content-Type: application/json' 
		'/api/v1/recipe/{id}'
		
		'Accept: application/json' -d 
                '{
                  "title": "string",
                  "vegetarian": true,
                  "numberOfPerson": 5,
                  "cookingInstruction": "string",
                  "ingredients": [
                    {
                      "id": 1,
                      "name": "string",
                      "description": "string",
                      "recipeId": 1
                    }
                  ]
                }'	 
		

```

- Get All Recipes
    - Get all recipe service is used to get list of all the recipes. Following is the api url. It is GET request.

```
		GET 
		'Accept: application/json' 
		'/api/v1/recipes/'

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
		'/api/v1/{}'

```

- Search Recipe By Criteria
    - This method searches for the recipe that fits into the given criteria. User can search by any field of the Recipe. Following are the fields of the recipe.
        - name
        - servings
        - cookingInstruction
        - vegetarian
    - Search criteria needs field to search for with operator to apply on field.
        - vegetarian:true = here : is used to check equality. This criteria mean all the recipes which are vegetarian.
        - servings = This is self-explanatory. It will bring all the recipes which can be served to more than 5 person.
    
    - Search criteria can also be put together with comma separated values.
        - vegetarian:true,servings>4 = This will query the database with both the criteria "AND" operator.
        - vegetarian:true,servings>=4 = This will query the database with both the criteria "OR" operator.

```
	http://localhost:8444/api/v1/filteredRecipes?vegetarian=true&servings=4&include=Sauce&exclude=Capsicum
```

## Test Scripts

Postman test script is provided in postman/ folder. It can be used by importing script and environment to postman. Don't forget to select environment and whitelist host.

## Clarifications
As per the requirement given, User should be able to search in the recipes. Instead of creating different method. It is more feasible and reusable to create advance search functionality. I have not completed whole functionality currently, but it is reusable.

All the validations are imaginary, it is only for the demo purpose.

Code coverage is not considered for model, repository and dto classes.

## Next steps

Database Configuration
This project uses two different database setups depending on the environment:
H2 Database (Demo & Initial Development)

Used for quick setup, local debugging, and early functional testing.
Allows the application to run without requiring an external database installation.

PostgreSQL (Production)

Intended database for production to ensure reliable data persistence and performance.
The application configuration can be easily switched to PostgreSQL using environment‑specific properties.


🍽️ Domain Model Adjustments
Ingredients Converted to Set
During debugging, the ingredients field was changed from List to Set to ensure uniqueness and improve data consistency.
This also aligns with business logic where duplicate ingredients do not make sense.
Separate Request and Entity Models
Different models are kept for:

API Requests (DTOs)
Persistence Layer (Entities)

This design supports future flexibility, such as:

Different naming conventions across UIs/frontend apps
Backend refactoring without breaking clients
Mapping customizations if request formats evolve


🔍 Future Enhancements (Search & Functional Requirements)
Further functional discussions are planned around:

Search behavior and expected filtering logic
Enhancing domain validation and business rules
Adjusting entities, DTOs, or repository queries based on finalized requirements

Once functional expectations are finalized, the codebase can be updated accordingly.

🧪 Testing Notes
Due to time constraints, only limited Postman integration test scripts could be created.
Once deployed to the development environment, more detailed Postman tests will be added, including:

Regression scenarios
Error handling validations
Negative test flows


📨 Postman Improvements
To make the API collection production-ready:

A separate collection and environment have been created.
DTAP-ready URLs (Dev, Test, Acceptance, Production) are managed using variable-based configurations.
This enables easier switching between environments during CI/CD pipelines or manual testing.


🔐 Security & Quality Integration
Before merging into master, the application must pass the standard enterprise security and quality checks:

SonarQube – Code quality & static analysis
Nexus IQ – Dependency vulnerability scanning
Fortify – Security scanning for code vulnerabilities

These integrations ensure the application meets production readiness standards.

🐳 Docker Image Submission
To containerize and deliver the application:
Dockerfile Creation

A Dockerfile is included to package the application into a runnable container image.

Docker Compose

Docker Compose can be used for multi-container setups
(e.g., application + PostgreSQL database).

Image Submission

The final Docker image can be pushed to any registry such as Docker Hub, AWS ECR, Azure Container Registry, etc.