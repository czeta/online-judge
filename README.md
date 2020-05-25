# online-judge
acm在线评测平台

# docker部署
本应用使用docker-compose部署。

将项目根目录docker-compose.yml文件上传至服务器，根据yml文件中的描述创建好宿主机的目录，并上传对应的文件（jar包，sql文件），然后docker-compose命令创建并启动容器即可访问。

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

# API接口访问

使用了knife4j作为后台API服务的接口文档，服务启动后访问：
```shell
http://IP:8080/doc.html
```
现有示例：http://121.36.21.111:8080/doc.html