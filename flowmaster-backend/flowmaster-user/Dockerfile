# FlowMaster User Service Dockerfile
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 设置环境变量
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseStringDeduplication"
ENV SPRING_PROFILES_ACTIVE=docker

# 安装Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 复制 pom.xml 文件
COPY flowmaster-backend/pom.xml .
COPY flowmaster-backend/flowmaster-user/pom.xml flowmaster-user/

# 下载依赖
RUN mvn dependency:go-offline -B -f flowmaster-user/pom.xml

# 复制源代码
COPY flowmaster-backend/flowmaster-user/src flowmaster-user/src

# 构建应用
RUN mvn clean package -DskipTests -f flowmaster-user/pom.xml

# 创建非 root 用户
RUN addgroup --system spring && adduser --system spring --ingroup spring

# 复制构建的 jar 文件
RUN cp flowmaster-user/target/*.jar app.jar

# 更改文件所有者
RUN chown spring:spring app.jar

# 切换到非 root 用户
USER spring:spring

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
