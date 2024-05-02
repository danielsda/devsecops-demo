pipeline {
    agent any

    stages {
        stage('Lint') {
            steps {
                sh 'echo "lint_commit"'

                sh 'echo "lint_dockerfile"'

                sh 'echo "lint_dockerfile_trust"'

                sh 'echo "lint_release"'
            }
        }

        stage('Build') {
            steps {
                sh 'echo "Java Build"'
                sh 'mvn clean package -DskipTests=true'
                archive 'target/*.jar'

                sh 'echo "Docker Build"'
                sh 'docker build -t numeric-app:""$GIT_COMMIT .'

                sh 'echo "Helm Build"'
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Java Unit Tests"'
                sh "mvn test"
                archive 'target/*.jar'

                sh 'echo "Docker Unit Tests"'

                sh 'echo "Helm Unit Tests"'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }

        stage('Audit') {
            steps {
                sh 'echo "Audit"'
            }
        }

        stage('Quality') {
            steps {
                sh 'echo "Quality"'
            }
        }

        stage('Quality-report') {
            steps {
                sh 'echo "Quality"'
            }
        }

        stage('Dry-run') {
            steps {
                sh 'echo "Dry-Run"'
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "Deploy Kubernetes"'
                sh 'sed -i "s#replace#numeric-app:${$GIT_COMMIT}#g" k8s_deployment_service.yaml'
                sh 'kubectl apply -f k8s_deployment_service.yaml'
            }
        }
    }
}