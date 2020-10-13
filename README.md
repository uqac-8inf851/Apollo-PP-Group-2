# Apollo-PP-Group-2

[Project intro]

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on the production server.

### Prerequisites

Firstly, it is important that you have Java™ Development Kit (JDK) installed on your computer. It is recommended [AdoptOpenJDK](https://adoptopenjdk.net/) version 8 or version 11.

Check which version of the JDK your system is using 

```
java -version
```

If the OpenJDK is used, the results should look like

```
java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)
```

Secondly, you have ensure that your system is using [Docker](https://www.docker.com/) 

```
docker --version
```

If the Docker is installed, the result should look like

```
Docker version 19.03.8, build afacb8b
```

If not, you can choose [one of the options](https://docs.docker.com/engine/install/) for installation depending on which operational system you are running. Docker Engine is available on a variety of Linux platforms, macOS and Windows 10.

In case of you are running MacOS or Windows you have Docker Desktop as option, or simply use the static binary installation.

Ensure that the Docker is installed using the check version command listed above.

The final step to find a complete development environment on your computer is to have Node.js installed. Node.js is necessary to code the frontend using VueJS 3.0. It is possible that you have an installation of Node.js via [package manager](https://nodejs.org/en/download/package-manager/). 

### Coding

To begin the development, you have to find an appropriate IDE with support to code in Java (backend) and web languages (frontend). As suggestion, you can use [Visual Studio Code](https://code.visualstudio.com/). This tool offers a set of complements that can assist you during the development, and can be installed further.

After the installation make the clone of git repository using the following command 

```
git clone https://github.com/uqac-8inf851/Apollo-PP-Group-2.git
```

> Important: make sure you have the SSH private key to authenticate on GitHub. If you still not have asked it to admin.

Once you have done the git clone you will find the entire project on your computer (backend, database schema and frontend) according to the folders listed below

.
├── Jenkinsfile
├── README.md
├── backend
│   ├── Dockerfile
│   ├── HELP.md
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── src
│   └── target
├── dbdata
├── docker-compose.yml
└── frontend
    ├── Dockerfile
    ├── README.md
    ├── dist
    ├── index.html
    ├── node_modules
    ├── package-lock.json
    ├── package.json
    ├── src
    ├── webpack.config.js
    └── yarn.lock

The backend project was built in Java using **Spring Boot** framework with support of JPA extension to manage the connection with the **Postgresql** database. This component is able to generate the database schema automatically (dbdata folder) when you make the project building. The folder **frontend** stores the VueJS 3.0 files and the frontend files of this project.

***Please, look for the README.md files in the backend and frontend folders to read more details about implementation and building process.***

### Running locally

If you want to run locally an instance of Apollo you can easily ride it up using Docker. This project uses a Docker Compose file to build and run the frontend and backend Dockerfiles.

Once you have Docker installed and the **projects clone to your desktop** you are able to run the following command in the project folder

```
./localdocker.sh
```

This command runs three containers: (i) frontend, (ii) backend and (iii) database, connected via an internal Docker network.

***If it is the first time you are running the docker-compose command it will take a time to finish.***

Apollo will be running in three different ports: (i) 8081 for backend, 8082 for frontend and 5432 for database. Use http://localhost:8082 to access Apollo on your computer.

### Deployment

Apollo server uses Jenkins to automate the building and deployment processes on production. When you push your changes to GitHub the server receives the command to clone the repository updated and performs the building process of backend and frontend projects. 

After this procedure, Jenkins runs the docker compose file to (i) build the Docker images updated and (ii) ride containers up.

Finally, you are able to use Apollo new version using the IP server.
