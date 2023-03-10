# EC2 인스턴스에서 SPARK 설치하기

# 1. 사전 환경설정

```powershell
# 비밀번호 설정
sudo passwd
# 루트 모드로
su
# 루트 비밀번호 설정
passwd
#설정 후 나가기
exit
```

# 2. 패키지 및 jre scala 설치하기

```powershell
# 전체 패키지 정보 업데이트
sudo apt-get update
# python3의 pip 설치하기
sudo apt install python3-pip
# jre 설치
sudo apt-get install default-jre
# scala 설치
sudo apt-get install scala

pip3 install py4j
```

# 3. spark 설치하기

[Index of /dist/spark (apache.org)](http://archive.apache.org/dist/spark/) 사이트에서 적당한 스파크 파일 링크를 이용

```powershell
# 다운받기, 적당한 링크 : http://archive.apache.org/dist/spark/spark-2.4.8/spark-2.4.8-bin-hadoop2.7.tgz
# 최신은 http://archive.apache.org/dist/spark/spark-3.3.2/spark-3.3.2-bin-hadoop3.tgz
wget ${link}

# 다운받은 파일 압축풀기
sudo tar -zxvf ${filename}

# findspark 설치
pip3 install findspark
```

```bash
# 디렉토리 만들기
mkdir certs

# 디렉토리로 이동하기
cd certs

# 키 생성(모두 엔터 눌러서 생략하기)
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout mycert.pem -out mycert.pem

#뒤로 가기
cd ..
```

- 새 argon hash

    'argon2:$argon2id$v=19$m=10240,t=10,p=8$6jGItQO7Uwpw5cT9htzAfQ$YYjqrFfn/FObcvJ7b0wI0BMymTv+iOaMizLb0+I8EQA’


## 새로운 ec2 ssh 접속 명령어

- 새 ec2 ssh 접속 명령어

    ```bash
    ssh -i "myspark.pem" -L 8888:localhost:8888 ubuntu@13.125.99.200
    ```



## jupyter notebook config

```docker
# move to directory
cd .jupyter/

# edit py
vi jupyter_notebook_config.py
```

### jupyter_notebook_config.py

- 코드 보기

    ```python
    c = get_config()  #noqa
    c.NotebookApp.certfile=u'/home/ubuntu/certs/mycert.pem'
    # 모든 ip에서 실행하도록 함
    c.NotebookApp.ip='*'
    c.NotebookApp.port=8888
    # 위에서 새로 설정받은 argon hash값 넣기
    conf.NotebookApp.password = u'<argon2:$argon2id$v=19$m=10240,t=10,p=8$6jGItQO7Uwpw5cT9htzAfQ$YYjqrFfn/FObcvJ7b0wI0BMymTv+iOaMizLb0+I8EQA>'
    ```


# 4. Spark 실행하고 설정하기



## EC2에서 spark 실행하기

##