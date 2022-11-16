11번가 techtalk 발표 "java로 Kubernetes Operator를 만들 수 있다고?"에서 사용한 코드 레파지토리입니다.



## kubernetes 리소스

```json
kubernetes
├── cluster-role-binding.yml # exposedapps의 cluster role binding
├── cluster-role.yml # exposedapps의 cluster role 설정
└── cr.yml # exposedapps의 custom resource 파일
```



## 실행 방법

### 공통

minikube를 통해 로컬에 Kubernetes 클러스터를 띄워놓은 상태에서 진행합니다.

1. 코드 컴파일을 통한 CRD yaml 파일 생성
2. 생성된 yaml 파일을 통해 CRD 생성
   ```shell
   kubectl apply -f build/classes/java/main/META-INF/fabric8/elevenstapps.11st.example.com-v1.yml
   ```

3. Operator 실행

4. Operator 실행 후 Custom Resource 실행
   ```shell
   kubectl apply -f kubernetes/cr.yml
   ```

   

### IntelliJ를 통한 Operator 실행

IntelliJ 내에서 애플리케이션을 실행



### minikube Kubernetes 클러스터에 배포를 통한 Operator 실행

1. CLI에서 미니큐브 내의 도커 엔진을 사용하도록 설정
   ```shell
   eval $(minikube -p minikube docker-env)
   ```

2. 도커 이미지 빌드
   ```shell
   docker build -t java-operator-sample  -f ./Dockerfile .
   ```

3. ClusterRole, ClusterRoleBinding 설정
   ```shell
   kubectl apply -f kubernetes/cluster-role.yml
   kubectl apply -f kubernetes/cluster-role-binding.yml
   ```

4. pod 실행

   ```shell
   kubectl run java-operator-sample --image=java-operator-sample --image-pull-policy=Never --restart=Never
   ```

