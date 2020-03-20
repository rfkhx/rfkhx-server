FROM java:8
VOLUME /tmp
ADD target/mishuserver-0.0.1-SNAPSHOT.jar mishu.jar
RUN bash -c 'touch /mishu.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mishu.jar"]
