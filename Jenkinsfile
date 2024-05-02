pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'echo "Java Build"'
                sh 'mvn clean package -DskipTests=true'
                archive 'target/*.jar'

                sh 'echo "Docker Build"'
                sh 'docker build -t numeric-app:""$GIT_COMMIT .'
            }
        }

        stage('Test') {
            steps {
                sh "mvn test"
                archive 'target/*.jar'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }
    }
}