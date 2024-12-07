# Use Maven for the build stage
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use Tomcat for deployment
FROM tomcat:9.0-jdk17-openjdk-slim
# Remove the default webapps to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy the WAR file to the Tomcat webapps directory
COPY --from=build /target/artgallery-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/artgallery.war
# Expose port 8080 for the Tomcat server
EXPOSE 8080
# Start Tomcat server
CMD ["catalina.sh", "run"]
