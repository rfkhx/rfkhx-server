# 密书·服务器端

> 本项目是[密书](https://code.aliyun.com/yqmailsend/rfkhx)的服务器端程序，用于提供密书的版本发布、数据同步等功能。

[TOC]

## 技术栈

- 前端：`Bootstrap`、`jQuery`
- 模板引擎：`Thymeleaf`
- 后端：`springboot`、`Spring Security`
- 持久化层：`Spring Data Jpa`
- 数据库：`MariaDB`
- 部署：`Docker`、`Nginx`
- 版本控制：`Git`

## 项目结构

- `config` 站点配置信息
- `controller`控制器
- `dto`实体类（与数据库表对应）
- `init`项目启动配置
- `repositories`
- `utils`静态工具类
- `vo`实体类（用于不同层之间交换数据或与json对应）

## 功能介绍

目前为了简单，多数页面只做了功能，没有设计界面。

### 系统设置

除`Spring`本身的项目设置，该项目的系统设置都存储在数据库中，在启动时自动载入。

| 配置         | 存储的名字 | 用途                                                         |
| ------------ | ---------- | ------------------------------------------------------------ |
| 备案信息     | beian      | 根据我国法律要求，在网站主页底部添加备案信息                 |
| 文件上传路径 | uppath     | 指定上传的文件的存储位置                                     |
| 站点网址     | url        | 用于拼接文件的下载路径等。站点网址包括前面的`http`或`https`，不包括最后的斜线。 |

### 版本发布

用于发布软件版本，发布的版本会显示在主页和用于软件的自动升级。

## 接口

### 自动升级信息接口

- 请求地址` /update/{platform}`
  - `platform` 版本对应的平台。
    - Android 安卓（`arm`）平台。
- 请求参数
  - `versionCode`当前用户已经安装的版本号。非必须。用于比较版本，确定应用是否需要升级。
  - `appKey` 应用名。非必须。该字段暂时无用。

## 编译部署

1. 使用`maven`编译。编译时配置文件中指定的数据库应该能正常访问。`mvn install`
2. 将编译出的`jar`上传到服务器上。
3. 创建一个`application.properties`，内容大致如下

```properties
#使用HTML5
spring.thymeleaf.mode=html

#数据库连接。注意要使用宿主机的ip地址。
spring.datasource.url=jdbc:mysql://172.18.0.1:3306/mishu?useUnicode=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=YeVwQAFNQP5u57
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
#create模式关闭时会清除数据
spring.jpa.properties.hibernate.hbm2ddl.auto=update

#日志级别配置
#Spring框架中的日志级别
logging.level.org.springframework.web=INFO
logging.level.edu.upc.mishuserver=DEBUG

spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=1000MB

server.port = 8080
```

4. 创建一个`Dockerfile`，内容大致为

```dockerfile
FROM java:8
VOLUME /tmp
ADD mishuserver-0.0.1-SNAPSHOT.jar mishu.jar
ADD application.properties application.properties
RUN bash -c 'touch /mishu.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mishu.jar"]
```

5. 构建镜像

```bash
sudo docker build -t mishu .
```

6. 启动。注意记录此处映射的路径，应在站点启动后加入配置文件。

```bash
docker run --name mishu -d -p 8080:8080 -v /data/upload:/upload  mishu
```

7. 修改nginx配置文件

在`/etc/nginx/sites-available`下添加配置文件`mishu.conf`

```
server{
  listen  80;
  server_name  upccaishu.top;
  index  index.php index.html index.htm;

  location / {
     proxy_pass  http://127.0.0.1:8080;
  }
}
```

建立符号链接

```bash
sudo ln -s /etc/nginx/sites-available/mishu.conf /etc/nginx/sites-enabled/mishu.conf
```

重启`nginx`

```bash
sudo nginx -c /etc/nginx/nginx.conf -s reload
```

8. 访问后台管理界面，修改站点配置信息。

访问类似[http://upccaishu.top/admin/admin.html](http://upccaishu.top/admin/admin.html)的地址，修改配置信息。