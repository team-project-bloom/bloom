### How to Launch a Spring Boot Application in Docker

Before running the application, ensure the following tools are installed and available:

- Maven (3.8+ recommended)
```
mvn -version
```

- Docker
```
docker --version
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
JWT_SECRET=your_data //1q2w3e4r5t6y7u8i9o0pqawsedrftgyhujikolpazsxdcfvgbhnjmklqwertyuiop
```

- Create .jar file
```
mvn clean package
```

#### Run the app with Docker

- Create Docker image
```
docker build -t my-bloom .
```

- Run docker container
```
 docker run --env-file .env -p 8080:8080 my-bloom
```

**Or Use Docker Compose to run app**

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
