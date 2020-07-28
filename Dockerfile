FROM openjdk:latest
EXPOSE 8082
ADD target/ubunpay-calculator.jar ubunpay-calculator.jar
ENTRYPOINT ["java","-jar","/ubunpay-calculator.jar"]
