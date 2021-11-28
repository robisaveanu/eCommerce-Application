pipeline {
    agent any
    stages {
        stage ('Compile Stage') {
            steps {
                withMaven(maven : 'Maven3') {
                    sh 'mvn clean compile'
                }
            }
        }
        stage ('Testing Stage') {

            steps {
                withMaven(maven : 'Maven3') {
                    sh 'mvn test'
                }
            }
        }
        stage ('Install Stage') {
            steps {
                withMaven(maven : 'Maven3') {
                    sh 'mvn install'
                }
            }
        }
    }
}