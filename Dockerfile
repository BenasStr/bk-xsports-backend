FROM maven:3.8.7-openjdk-18

WORKDIR /bk-xsports-backend
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run