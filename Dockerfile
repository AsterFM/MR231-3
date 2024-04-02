FROM openjdk
COPY /out/artifacts/build/start.jar /java/start.jar
CMD ["java", "-jar", "java/start.jar"]