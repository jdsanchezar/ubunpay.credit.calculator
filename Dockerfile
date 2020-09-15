FROM openjdk:latest
EXPOSE 8082
ADD properties/qa.application.properties application.properties
ADD target/com.ubunpay.credit.calculator.jar com.ubunpay.credit.calculator.jar
ENTRYPOINT ["java","-jar","/com.ubunpay.credit.calculator.jar"]
