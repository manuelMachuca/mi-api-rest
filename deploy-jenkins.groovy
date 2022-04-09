node {
    stage('Build') {
        if (env.BRANCH_NAME == 'master') {
            echo 'I only execute on the master branch'
        } else {
            echo 'I execute elsewhere'
        }
     
        git url: 'https://github.com/manuelMachuca/mi-api-rest'

        withMaven(
        // Maven installation declared in the Jenkins "Global Tool Configuration"
        maven: 'maven-3', // (1)
        // Use `$WORKSPACE/.repository` for local repository folder to avoid shared repositories
        mavenLocalRepo: '.repository', // (2)
        // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
        // We recommend to define Maven settings.xml globally at the folder level using
        // navigating to the folder configuration in the section "Pipeline Maven Configuration / Override global Maven configuration"
        // or globally to the entire master navigating to  "Manage Jenkins / Global Tools Configuration"
        mavenSettingsConfig: '9ca6c49f-7398-46f3-b85b-d10bb70958a2' // (3)
        ) {

        // Run the maven build
        sh "mvn clean verify"

        } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe & FindBugs & SpotBugs reports...
            
    }
    stage('Run') {
       sh "java -cp target/mi-api-rest-1.0-SNAPSHOT.jar com.manuel.app.App"
    }
    stage('Scan Veracode') {
       //enviar compiplado.jar a Veracode
       //...
       echo 'Enviado a Veracode'
    }
    stage('SonarQube analysis') {
        withSonarQubeEnv(installationName: 'SonarServer') { // You can override the credential to be used
        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
        }
    }
    
}
