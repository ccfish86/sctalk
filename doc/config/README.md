spring boot的包，编译出来即可以服务的形式执行。

其他启动参数的配置，可以参考[samples/]目录下的conf文件

pom文件里做以下设置：
```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <executable>true</executable>
            </configuration>
        </plugin>
    </plugins>
</build>
```