pipeline {
    triggers { pollSCM('') }
    agent {
        kubernetes {
            yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: jnlp
            image: jenkins/inbound-agent:latest
          - name: java
            resources:
              requests:
                ephemeral-storage: 1Gi
              limits:
              ephemeral-storage: 5Gi
            image: openjdk:21-slim
            command:
            - cat
            tty: true
          - name: docker
            image: docker:latest
            command:
            - cat
            tty: true
            volumeMounts:
             - mountPath: /var/run/docker.sock
               name: docker-sock
          - name: kubectl
            image: bitnami/kubectl:1.32.0
            command:
            - cat
            tty: true
            securityContext:
              runAsUser: 1000
          - name: curl
            image: curlimages/curl
            command:
            - cat
            tty: true
            securityContext:
              runAsUser: 1000
          volumes:
          - name: docker-sock
            hostPath:
              path: /var/run/host-docker.sock
        '''
        }
    }

    environment {
        GIT_REPO = ' git@github.com:mangefoo/thread-pilot-draft.git'
        DOCKER_IMAGE = 'thread-pilot'
        GIT_BRANCH = 'master'
    }

    stages {
        stage('Checkout') {
            environment {
                GIT_SSH_COMMAND = 'ssh -o StrictHostKeyChecking=no'
            }
            steps {
                sshagent (credentials: ['github']) {
                    script {
                        sh '''
                            [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                            ssh-keyscan -H github.com > ~/.ssh/known_hosts
                        '''
                    }
                    git branch: "${GIT_BRANCH}",
                        credentialsId: "github",
                        url: "${GIT_REPO}"
                }
                script {
                    TIMESTAMP = sh(script: 'date "+%y%m%d%H%M%S"', returnStdout: true).trim()
                    GIT_SHORT_HASH = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    BUILD_VERSION = "${TIMESTAMP}-${GIT_SHORT_HASH}"
                }
            }
        }

        stage('Build and test') {
            steps {
                container('java') {
                    sh '''
                    ./gradlew build test
                    '''
                }
            }
        }

        stage('Build Docker Images') {
          parallel {
            stage('insurance-service') {
              steps {
                container('docker') {
                    sh """
                    cd insurance-service && docker build -f Dockerfile -t ${DOCKER_REGISTRY}/insurance-service:${BUILD_VERSION} .
                """
                }
              }
            }
             stage('vehicle-service') {
               steps {
                 container('docker') {
                     sh """
                     cd vehicle-service && docker build -f Dockerfile -t ${DOCKER_REGISTRY}/vehicle-service:${BUILD_VERSION} .
                 """
                 }
               }
             }
          }
        }

        stage('Push to Registry') {
          parallel {
            stage('insurance-service') {
              steps {
                container('docker') {
                  sh """
                    docker push ${DOCKER_REGISTRY}/insurance-service:${BUILD_VERSION}
                  """
                }
              }
            }
            stage('vehicle-service') {
              steps {
                container('docker') {
                  sh """
                    docker push ${DOCKER_REGISTRY}/vehicle-service:${BUILD_VERSION}
                  """
                }
              }
            }
          }
        }

        stage('Deploy to test') {
          parallel {
            stage('insurance-service') {
              steps {
                container('kubectl') {
                  script {
                    withKubeConfig([credentialsId: 'kubernetes-blackbox']) {
                      sh "DEPLOYMENT_ENV=test DOCKER_IMAGE=$DOCKER_REGISTRY/insurance-service:$BUILD_VERSION envsubst < insurance-service/k8s-deploy.yaml | kubectl apply -f -"
                      sh "sleep 10" // Allow things to settle
                      sh "kubectl wait -n thread-pilot-test deployment/insurance-service --for=condition=Available=True --timeout=90s"
                    }
                  }
                }
              }
            }
            stage('vehicle-service') {
              steps {
                container('kubectl') {
                  script {
                    withKubeConfig([credentialsId: 'kubernetes-blackbox']) {
                      sh "DEPLOYMENT_ENV=test DOCKER_IMAGE=$DOCKER_REGISTRY/vehicle-service:$BUILD_VERSION envsubst < vehicle-service/k8s-deploy.yaml | kubectl apply -f -"
                      sh "sleep 10" // Allow things to settle
                      sh "kubectl wait -n thread-pilot-test deployment/vehicle-service --for=condition=Available=True --timeout=90s"
                    }
                  }
                }
              }
            }
          }
        }

        stage('Run E2E Tests') {
          steps {
            container('curl') {
              sh '''
                /bin/sh e2e/e2e.sh
              '''
            }
          }
        }

        stage('Approve deployment') {
          input {
            message "Deploy to production?"
          }
          steps {
            echo 'Deployment approved. Proceeding...'
          }
        }

        stage('Deploy to prod') {
          parallel {
            stage('insurance-service') {
              steps {
                container('kubectl') {
                  script {
                    withKubeConfig([credentialsId: 'kubernetes-blackbox']) {
                      sh "DEPLOYMENT_ENV=prod DOCKER_IMAGE=$DOCKER_REGISTRY/insurance-service:$BUILD_VERSION envsubst < insurance-service/k8s-deploy.yaml | kubectl apply -f -"
                      sh "sleep 10" // Allow things to settle
                      sh "kubectl wait -n thread-pilot-prod deployment/insurance-service --for=condition=Available=True --timeout=90s"
                    }
                  }
                }
              }
            }
            stage('vehicle-service') {
              steps {
                container('kubectl') {
                  script {
                    withKubeConfig([credentialsId: 'kubernetes-blackbox']) {
                      sh "DEPLOYMENT_ENV=prod DOCKER_IMAGE=$DOCKER_REGISTRY/vehicle-service:$BUILD_VERSION envsubst < vehicle-service/k8s-deploy.yaml | kubectl apply -f -"
                      sh "sleep 10" // Allow things to settle
                      sh "kubectl wait -n thread-pilot-prod deployment/vehicle-service --for=condition=Available=True --timeout=90s"
                    }
                  }
                }
              }
            }
          }
        }
    }

    post {
        always {
            cleanWs()
        }

        success {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: "✅ Pipeline Succeeded: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'\n" +
                        "Check details at: ${env.BUILD_URL}"
            )
        }

        failure {
            slackSend(
                color: 'danger',
                message: "❌ Pipeline Failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'\n" +
                        "Check console output at: ${env.BUILD_URL}"
            )
        }
    }
}
