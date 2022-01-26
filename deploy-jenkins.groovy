node {
    stage('Example') {
        if (env.BRANCH_NAME == 'master') {
            echo 'I only execute on the master branch'
        } else {
            echo 'I execute elsewhere'
        }
     
	    def mvnHome = "/opt/apache/maven/3.8.4" 
		steps.sh "'${mvnHome}/bin/mvn' compile test-compile"
        
    }
}