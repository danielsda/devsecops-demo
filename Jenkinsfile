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

                withDockerRegistry([credentialsId: "docker-hub", url: ""]) {
                    sh 'echo "Docker Build"'
                    sh 'docker build -t danielsda/numeric-app:""$GIT_COMMIT"" .'
                    sh 'docker push danielsda/numeric-app:""$GIT_COMMIT""'
                }

                sh 'echo "Helm Build"'
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Java Unit Tests"'
                sh "mvn verify"
                archive 'target/*.jar'

                sh 'echo "Java Mutation Tests"'
                sh "mvn org.pitest:pitest-maven:mutationCoverage"

                sh 'echo "Docker Unit Tests"'

                sh 'echo "Helm Unit Tests"'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                    pitmutation mutationStatsFile: '**/target/pit-reports/**/mutations.xml'
                }
            }
        }

        stage('Audit') {
            steps {
                sh 'echo "Dependency Check"'
                sh 'mvn dependency-check:check'
            }
            post {
                always {
                    dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
                }
            }
        }

        stage('Quality') {
            steps {
                sh 'echo "SonarQube Analysis"'
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality-report') {
            steps {
                sh 'echo "Quality Gate"'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Dry-run') {
            steps {
                sh 'echo "Dry-Run"'
            }
        }

        stage('Deploy') {
            steps {
                withKubeConfig([credentialsId: "kubernetes"]) {
                    sh 'echo "Deploy Kubernetes"'
                    sh 'sed -i "s#replace#danielsda/numeric-app:${GIT_COMMIT}#g" k8s_deployment_service.yaml'
                    sh 'kubectl apply -f k8s_deployment_service.yaml'
                }
            }
        }
    }
}