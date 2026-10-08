# JackMart - multi-stage build for Render (Tomcat 9 + JDK 17 WAR).
# Stage 1 builds the WAR from source so the Maven target/ directory
# never needs to be committed to Git.
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY db ./db
COPY checkstyle.xml suppressions.xml spotbugs-exclude.xml ./
# NOTE: `package` stops before the `verify` phase, so the Checkstyle/SpotBugs
# executions (bound to `verify`) do not run here; CI runs `mvn -B clean verify`.
RUN mvn -B clean package -DskipTests

# Stage 2 runs the WAR on Tomcat 9 with JDK 17.
FROM tomcat:9-jdk17
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /app/target/jackmart.war /usr/local/tomcat/webapps/jackmart.war
# Ship migration files so the app applies db/migrations V1..V3 in order at startup
# (it falls back to the schema bundled inside the WAR when this directory is absent).
COPY --from=builder /app/db /usr/local/tomcat/db
# Render injects $PORT; Tomcat's default HTTP connector is 8080, so remap it at
# container start. ${PORT:-8080} keeps plain local `docker run` working unchanged.
EXPOSE 8080
CMD ["sh", "-c", "sed -i \"s/8080/${PORT:-8080}/\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]
