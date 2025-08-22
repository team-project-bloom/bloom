# Bloom 🍷

**Bloom** is a wine shop application where users can explore wines, search dynamically, add favorites,
and add items to the shopping cart for future orders through a simple REST API.

---

### How to Launch a Spring Boot Application with Maven or Docker Compose

Before running the application, ensure the following tools are installed and available:

- Java (17 or compatible)
```
java -version
```

- Maven (3.8+ recommended)
```
mvn -version
```

- Docker & Docker Compose
```
docker --version
docker compose version
```
Open the Terminal
- Open a terminal or command prompt on your computer

Navigate to the Project Folder
- Use the cd command to move into the folder that contains your Spring Boot project (the folder with the pom.xml file):
```
cd path/to/your/project
```
*Example:*
```
cd ~/Documents/bloom
```

Before running the project, create .env a file in the root directory with the required credentials

*Example:*
```

JWT_SECRET=your_data 
MYSQLDB_USER=your_data
MYSQLDB_ROOT_PASSWORD=your_data
MYSQLDB_DATABASE=your_data
MYSQLDB_LOCAL_PORT=your_data
MYSQLDB_DOCKER_PORT=your_data
SPRING_LOCAL_PORT=your_data
SPRING_DOCKER_PORT=your_data
DEBUG_PORT=your_data
```

Run the Application Using Maven
- Use the following command to launch the Spring Boot application:
```
mvn spring-boot:run
```

**Or Use Docker Compose to run app**

- Create .jar file
```
mvn clean package
```

- Run the app with Docker Compose
```
docker compose up --build
```

Verify the Application is Running
- If successful, you will see logs ending with something like:
```
Started BloomApplication in X.XXX seconds (process running for X.XXX)
```

#### Use Swagger 

[http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)
