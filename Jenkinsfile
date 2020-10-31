pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    environment {
        pom = readMavenPom file: 'backend/pom.xml'
        version_backend = "${pom.version}"
        file = "backend-${pom.version}"
    }

    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building'
                
                sh '''
                cd backend
                mvn -Dmaven.test.failure.ignore=true clean package
                '''
            }
        }
        stage('Test') {
            steps {
                echo 'Testing'
                sh '''
                cd backend
                mvn test
                '''                
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying'
                sh 'docker-compose down --remove-orphans'
                sh 'docker-compose build'
                sh 'docker-compose up -d'
            }
        }
    }
}