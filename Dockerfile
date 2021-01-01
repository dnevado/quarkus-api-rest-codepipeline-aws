# BUILD STAGE 

FROM gradle:jdk11 AS build
ARG CHALLENGE_NAME=car-pooling-challenge-dnevado
COPY --chown=gradle:gradle . /home/gradle/${CHALLENGE_NAME}
WORKDIR /home/gradle/${CHALLENGE_NAME}
RUN gradle build --no-daemon
RUN cp -r  /home/gradle/${CHALLENGE_NAME} /tmp
WORKDIR /tmp/${CHALLENGE_NAME}/build
USER gradle 


# DOCKER STAGE 

FROM registry.access.redhat.com/ubi8/ubi-minimal:8.3


ARG JAVA_PACKAGE=java-11-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'


RUN microdnf install curl ca-certificates ${JAVA_PACKAGE} \
    && microdnf update \
    && microdnf clean all \
    && mkdir /deployments \
    && chown 1001 /deployments \
    && chmod "g+rwX" /deployments \
    && chown 1001:root /deployments \
    && curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o /deployments/run-java.sh \
    && chown 1001 /deployments/run-java.sh \
    && chmod 540 /deployments/run-java.sh \
    && echo $JAVA_HOME > /deployments/java_home_path.txt \
    && echo "securerandom.source=file:/dev/urandom" >> /etc/alternatives/jre/lib/security/java.security  

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Dquarkus.http.port=9091"     

RUN ls -ltr  /deployments

COPY --from=build  /tmp/car-pooling-challenge-dnevado/build/lib/* /deployments/lib/
COPY --from=build  /tmp/car-pooling-challenge-dnevado/build/*-runner.jar /deployments/app.jar
RUN ls -ltr /deployments/
	
EXPOSE 9091
USER 1001

ENTRYPOINT [ "/deployments/run-java.sh" ]
	
	