FROM openjdk:27-ea-trixie
ADD target/Dibs.jar Dibs.jar
ENTRYPOINT ["java","-jar","/Dibs.jar"]
