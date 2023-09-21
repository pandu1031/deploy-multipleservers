//Declarative pipeline
pipeline{
    agent {label 'slave1'}
    parameters {
        string(name: 'SERVERIP', defaultValue: '', description: 'Enter server ip')
        string(name: 'BUILDNUM', defaultValue: '', description: 'Enter Build number')
        string(name: 'BRANCH', defaultValue: '', description: 'Enter branch name')
}
stages{
    stage("Deploy in Multiple servers"){
        steps{

            sh '''
            aws s3 cp s3://mamuu/pandu/${BRANCH}/${BUILDNUM}/hello-${BUILDNUM}.war .
            ls -l 
            whoami
            IFS=',' read -ra storevalue <<< "${SERVERIP}"
            for ip in ${storevalue[@]}
            do
            echo "$ip"
            scp -o StrictHostKeyChecking=no -i /tmp/mamu1031.pem hello-${BUILDNUM}.war ec2-user@$ip:/var/lib/tomcat/webapps
            ssh -o StrictHostKeyChecking=no -i /tmp/mamu1031.pem ec2-user@$ip "hostname"
            done
            '''
        }
      }
    }
  }