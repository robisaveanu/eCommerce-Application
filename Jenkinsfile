pipeline {
    agent any
    stages {
        stage ('Compile Stage') {
            steps {
                withMaven(maven : 'maven_3_8_0') {
                    sh 'mvn clean compile'
                }
            }
        }
        stage ('Testing Stage') {

            steps {
                withMaven(maven : 'maven_3_8_0') {
                    sh 'mvn test'
                }
            }
        }
        stage ('Install Stage') {
            steps {
                withMaven(maven : 'maven_3_8_0') {
                    sh 'mvn install'
                }
            }
        }
    }
}