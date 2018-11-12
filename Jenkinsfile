def HTTP_PORT="8090"
node{
 stage('Initialize'){
        def mavenHome  = tool 'M3'
        env.PATH = "${mavenHome}/bin:${env.PATH}"
    }

    stage('Checkout') {
        checkout scm
    }
    stage('Build') {
 try {
         sh 'mvn clean install -DskipTests'
} finally {
           // step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])

}
     }


  stage('Sonar'){
          try {
                withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'TOKEN')]) {
                               sh "mvn sonar:sonar -Dsonar.login=$TOKEN"
                }

          } catch(error){
              echo "The sonar server could not be reached ${error}"
          }
       }




   }
