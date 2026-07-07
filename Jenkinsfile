pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'simongaorp/ems-back'
        EC2_HOST = '18.225.163.162'
        EC2_USER = 'ec2-user'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'feature-cicd-test',
                    url: 'https://github.com/Ruipu/SpringBootProject1.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package'
            }
        }

        stage('SonarCloud Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        ./mvnw sonar:sonar \
                          -Dsonar.host.url=https://sonarcloud.io \
                          -Dsonar.organization=simongao \
                          -Dsonar.projectKey=SpringBootProject1 \
                          -Dsonar.token=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    withCredentials([
                        string(credentialsId: 'google-client-secret', variable: 'GOOGLE_CLIENT_SECRET'),
                        string(credentialsId: 'db-password', variable: 'DB_PASSWORD')
                    ]) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                                sudo docker pull ${DOCKER_IMAGE}:latest &&
                                sudo docker stop ems-back || true &&
                                sudo docker rm ems-back || true &&
                                sudo docker run -d --name ems-back --network host \
                                  -e GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET} \
                                  -e DB_PASSWORD=${DB_PASSWORD} \
                                  -e SPRING_PROFILES_ACTIVE=prod \
                                  ${DOCKER_IMAGE}:latest
                            '
                        """
                    }
                }
            }
        }