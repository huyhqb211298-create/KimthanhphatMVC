# Dùng image Java 17 (hoặc 21 nếu bạn dùng Java 21)
FROM openjdk:17-jdk-slim

# Copy toàn bộ source code vào container
COPY . .

# Build ứng dụng bằng Maven Wrapper
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Copy file JAR đã build vào vị trí chạy chính
ARG JAR_FILE=target/KimThanhPhatMVC-0.0.1.jar
COPY ${JAR_FILE} app.jar

# Khai báo cổng
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java","-jar","/app.jar"]
