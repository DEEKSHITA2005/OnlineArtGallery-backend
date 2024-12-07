# Use Maven for the build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use Tomcat for deployment
FROM tomcat:9.0-jdk17-openjdk-slim
WORKDIR /usr/local/tomcat
# Remove default webapps to avoid conflicts
RUN rm -rf webapps/*
# Copy the WAR file to the Tomcat webapps directory
COPY --from=build /app/target/artgallery-0.0.1-SNAPSHOT.war webapps/ROOT.war
# Expose port 8080
EXPOSE 8080
# Start Tomcat
CMD ["catalina.sh", "run"]
