pipeline {
    agent any

    tools {
        maven 'maven 3.8.6'
    }

    environment {
        PROJECT_GUID = '5feedf8a-ed11-4eb7-ba5c-91bddbcaed87'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "📥 Checking out source code..."
                git branch: 'main',
                    url: 'https://github.com/Prathima-work/Java-Based-Project.git'
            }
        }

        stage('Compile') {
            steps {
                echo " Compiling project..."
                sh 'mvn compile'
            }
        }

        stage('Build Package') {
            steps {
                echo " Building package..."
                sh 'mvn clean package'
            }
        }

        stage('Locate JAR') {
            steps {
                script {
                    echo " Locating generated JAR file..."

                    env.JAR_FILE = sh(
                        script: "ls target/*.jar | head -n 1",
                        returnStdout: true
                    ).trim()

                    if (!env.JAR_FILE) {
                        error "❌ ERROR: No JAR file found in target directory"
                    }

                    echo "✅ JAR Found: ${env.JAR_FILE}"
                }
            }
        }

        stage('Sign File') {
            steps {
                sh '''
                set -e

                mkdir -p ${WORKSPACE}/signed

                echo "======================================"
                echo "🚀 STARTING SIGN PROCESS"
                echo "Input File : ${JAR_FILE}"
                echo "Project GUID : ${PROJECT_GUID}"
                echo "Output Dir : ${WORKSPACE}/signed"
                echo "======================================"

                codesign -guid ${PROJECT_GUID} \
                         -in ${JAR_FILE} \
                         -out ${WORKSPACE}/signed \
                         -sign

                echo "✅ SIGN PROCESS COMPLETED SUCCESSFULLY"
                echo "======================================"
                '''
            }
        }

        stage('Verify Signed File') {
            steps {
                sh '''
                set -e

                SIGNED_FILE=${WORKSPACE}/signed/$(basename ${JAR_FILE})

                echo "======================================"
                echo "🔎 STARTING VERIFICATION"
                echo "Signed File: $SIGNED_FILE"
                echo "======================================"

                if [ ! -f "$SIGNED_FILE" ]; then
                    echo "❌ ERROR: Signed file not found!"
                    exit 1
                fi

                codesign -guid ${PROJECT_GUID} \
                         -out $SIGNED_FILE \
                         -verify

                echo "✅ VERIFICATION COMPLETED SUCCESSFULLY"
                echo "======================================"
                '''
            }
        }

        stage('Archive Signed JAR') {
            steps {
                echo "📁 Archiving signed artifact..."
                archiveArtifacts artifacts: 'signed/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo "🎉 BUILD, SIGNING & VERIFICATION SUCCESSFUL"
        }
        failure {
            echo "❌ PIPELINE FAILED - CHECK LOGS ABOVE"
        }
    }
}
