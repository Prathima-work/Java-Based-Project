pipeline {
    agent any
    tools {
        maven 'maven 3.8.6'
    }
    parameters {
        string(
            name: 'PROJECT_GUID',
            defaultValue: '5feedf8a-ed11-4eb7-ba5c-91bddbcaed87',
            description: 'Project GUID for signing'
            )
       }
    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Prathima-work/Java-Based-Project.git'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Build Package') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Locate JAR') {
            steps {
                script {
                    env.JAR_FILE = sh(
                        script: "ls target/*.jar | head -n 1",
                        returnStdout: true
                    ).trim()

                    if (!env.JAR_FILE) {
                        error "❌ No JAR file found in target directory"
                    }

                    echo "Jar found: ${env.JAR_FILE}"
                }
            }
        }

        stage('Sign JAR') {
            steps {
                sh '''
                mkdir -p ${WORKSPACE}/signed

                echo " STARTING SIGN PROCESS"

                codesign -guid ${PROJECT_GUID} \
                         -in ${JAR_FILE} \
                         -out ${WORKSPACE}/signed \
                         -sign

                echo "✅ SIGN PROCESS COMPLETED"
                '''
            }
        }

        stage('Verify Signed JAR') {
            steps {
                sh '''
                SIGNED_FILE=${WORKSPACE}/signed/$(basename ${JAR_FILE})

                if [ ! -f "$SIGNED_FILE" ]; then
                    echo " Signed file not found"
                    exit 1
                fi

                echo "🔍 STARTING VERIFICATION"

                codesign -guid ${PROJECT_GUID} \
                         -out $SIGNED_FILE \
                         -verify

                echo "✅ VERIFICATION COMPLETED"
                '''
            }
        }
    }
 
    post {
        success {
            echo " BUILD, SIGNING & VERIFICATION SUCCESSFUL"
        }
        failure {
            echo " PIPELINE FAILED"
        }
    }
}
