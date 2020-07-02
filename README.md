## morphia-spring-boot-starter

![maven](https://img.shields.io/maven-central/v/org.openingo.starter/morphia-spring-boot-starter.svg)

#### dependency

```xml
<dependency>
    <groupId>org.openingo.starter</groupId>
    <artifactId>morphia-spring-boot-starter</artifactId>
    <version>new_version</version>
</dependency>
```

#### config

```yaml
openingo:  
  morphia:
    host: localhost
    port: 27017
    auth-database: admin
    username: username
    password: password
    database: sns
    map-package: org.openingo.morphia.starter.demo.entity
```