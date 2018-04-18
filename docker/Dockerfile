FROM java:8
VOLUME /tmp
ADD url-shortener-example-latest.jar url-shortener-app.jar
RUN bash -c 'touch /url-shortener-app.jar'
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://url-shortener-mongo/users","-Djava.security.egd=file:/dev/./urandom","-jar","/url-shortener-app.jar"]
