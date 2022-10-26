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
COPY --from=MAVEN_TOOL_CHAIN_CACHE /tmp/target/dbtest-0.0.1-SNAPSHOT.jar /opt/app/app.jar

ARG APPINSIGHTS_FILE=applicationinsights-agent-3.4.2.jar
ARG APPINSIGHTS_URL_PREFIX=https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.4.2
RUN curl -L "${APPINSIGHTS_URL_PREFIX}/${APPINSIGHTS_FILE}" --output /applicationinsights-agent.jar

EXPOSE 8080

ENTRYPOINT ["java","-javaagent:/applicationinsights-agent.jar", "-jar","/opt/app/app.jar"]