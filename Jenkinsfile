pipeline {
  agent any
  // any, none, label, node, docker, dockerfile, kubernetes
  
  tools {
    gradle 'HAT-gradle'
  }
  
  environment {
    gitName = 'Hyyena'
    gitEmail = 'sjtxm0320@gmail.com'
    githubCredential = 'github-HAT-cre'
    GIT_SSH_KEY_CREDENTIALS = 'github-HAT-SSH-key-pub'
    manifest = 'git@github.com:Hows-the-Air-Today/HAT-manifest.git'
  }
  
  stages {
    stage('Checkout Github') {
      steps {
        checkout scmGit(branches: [[name: '*/main']], extensions: [submodule(parentCredentials: true, reference: '', trackingSubmodules: true)], userRemoteConfigs: [[credentialsId: 'github-HAT-cre', url: 'https://github.com/Hows-the-Air-Today/HAT-backend-spring-boot.git']])
      }
      post {
        failure {
          echo 'Repository clone failure'
        }
        success {
          echo 'Repository clone success'
        }
      }
    }
    
    stage('Gradle Build') {
      steps {
        // 병렬 빌드 (실행 권한 필수)
        sh 'chmod +x ./gradlew'
        sh './gradlew build'
      }
      post {
        failure {
          echo 'Gradlew jar build failure'
        }
        success {
          echo 'Gradlew jar build success'
        }
      }
    }
    
    stage('Docker Image Build') {
      steps {
          sh "docker build -t 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/air-quality-app-batch:${currentBuild.number} -f air-quality-app-batch/Dockerfile ."
          sh "docker build -t 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/air-quality-app-external-api:${currentBuild.number} -f air-quality-app-external-api/Dockerfile ."
          sh "docker build -t 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/community-app-external-api:${currentBuild.number} -f community-app-external-api/Dockerfile ."
          sh "docker build -t 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/member-app-external-api:${currentBuild.number} -f member-app-external-api/Dockerfile ."
      }
      post {
        failure {
          echo 'Docker image build failure'
        }
        success {
          echo 'Docker image build success'  
        }
      }
    }
    
    stage('Login to AWS ECR') {
      steps {
        withCredentials([
          string(credentialsId: 'AWS_ACCESS_KEY_ID_hyyena', variable: 'AWS_ACCESS_KEY_ID'), string(credentialsId: 'AWS_SECRET_ACCESS_KEY_ID_hyyena', variable: 'AWS_SECRET_ACCESS_KEY')
          ]) {
            sh 'aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com'
        }
      }
    }
    
    stage('Docker Image Push to ECR') {
      steps {
        sh "docker push 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/air-quality-app-batch:${currentBuild.number}"
        sh "docker push 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/air-quality-app-external-api:${currentBuild.number}"
        sh "docker push 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/community-app-external-api:${currentBuild.number}"
        sh "docker push 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/member-app-external-api:${currentBuild.number}"
      }
      post {
        failure {
          echo 'Docker image push failure'
        }
        success {
          echo 'Docker image push success'  
        }
      }
    }
    
    stage('manifest file update') {
      steps {
        git credentialsId: 'github-HAT-cre',
            url: manifest,
            branch: 'main'
        // 이미지 태그 변경 후 메인 브랜치에 푸시
        sh "git config --global user.email ${gitEmail}"
        sh "git config --global user.name ${gitName}"
        sh "sed -i 's|image:.*air-quality-app-batch:.*|image: 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/air-quality-app-batch:${currentBuild.number}|g' ./HAT-deployment.yaml"
        sh "sed -i 's|image:.*air-quality-app-external-api:.*|image: 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/air-quality-app-external-api:${currentBuild.number}|g' ./HAT-deployment.yaml"
        sh "sed -i 's|image:.*community-app-external-api:.*|image: 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/community-app-external-api:${currentBuild.number}|g' ./HAT-deployment.yaml"
        sh "sed -i 's|image:.*member-app-external-api:.*|image: 915947332145.dkr.ecr.ap-northeast-2.amazonaws.com/member-app-external-api:${currentBuild.number}|g' ./HAT-deployment.yaml"
        sh "chmod +x ./HAT-deployment.yaml"
        sh "git add ./HAT-deployment.yaml"
        sh "git status"
        sh "git commit -m 'fix:${currentBuild.number} image versioning'"
        sh "git branch -M main"
        sh "git remote remove origin"
        sh "git remote add origin git@github.com:Hows-the-Air-Today/HAT-manifest.git"
        sh "git push -u origin main"
      }
      post {
        failure {
          echo 'Container Deploy failure'
        }
        success {
          echo 'Container Deploy success'
        }
      }
    }
  }
}
