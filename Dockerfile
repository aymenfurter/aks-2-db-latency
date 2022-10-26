ARG MVN_VERSION=3.8.6
ARG JDK_VERSION=11

FROM maven:${MVN_VERSION}-jdk-${JDK_VERSION} as MAVEN_TOOL_CHAIN_CACHE
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY ./pom.xml /tmp/
COPY ./src /tmp/src/
WORKDIR /tmp/
RUN mvn clean package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:17-mariner

RUN mkdir -p /opt/app
COPY --from=MAVEN_TOOL_CHAIN_CACHE /tmp/target/dbtest-0.0.1-SNAPSHOT.jar /opt/app/dbtest-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/opt/app/japp.jar"]