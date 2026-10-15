FROM eclipse-temurin:21-jdk
WORKDIR /sb-ecom
COPY target/*.jar sb-ecom.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","sb-ecom.jar"]