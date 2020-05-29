# online-judge
API服务。

在线编程竞赛平台分为三个服务，一个是前台服务，一个是后台API服务，最后是后台核心评测服务。
- [前台服务](https://github.com/evanlaochen/online-judge-fe)
- [API服务](https://github.com/evanlaochen/online-judge)
- [核心评测服务](https://github.com/evanlaochen/online-judge-core)。

# docker部署
本应用使用docker-compose部署。

将项目根目录docker-compose.yml文件上传至服务器，并将项目jar包和初始化数据库sql文件上传到宿主机目录中（见docker-compose.yml文件具体说明），然后docker-compose命令创建并启动容器即可访问。

启动服务：
```shell
docker-compose up -d
```
查看服务：
```shell
docker-compose ps
```
修改服务：
重新打包成jar包覆盖云服务器上的jar包，并执行：
```shell
docker-compose restart
```

# application.yml配置文件修改说明

#### mysql
mysql数据库链接修改为mysql所在服务器IP

#### redis
redis修改为redis所在服务器IP

#### kafka
kafka代理地址修改为部署的kafka集群IP

# API接口访问

使用了knife4j作为后台API服务的接口文档，服务启动后访问：
```shell
http://IP:8080/doc.html
```
现有示例：http://121.36.21.111:8080/doc.html