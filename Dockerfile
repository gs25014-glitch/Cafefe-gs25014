FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

RUN mvn dependency:get \
    -Dartifact=io.openliberty:openliberty-runtime:26.0.0.6:zip \
    -Dtransitive=false

RUN mkdir -p /openliberty \
    && mvn dependency:unpack \
    -Dartifact=io.openliberty:openliberty-runtime:26.0.0.6:zip \
    -DoutputDirectory=/openliberty \
    -Doverwrite=true


FROM eclipse-temurin:21-jre

COPY --from=build /openliberty/wlp /opt/ol/wlp

COPY --from=build /app/target/Cafefe-1.0-SNAPSHOT.war \
    /opt/ol/wlp/usr/servers/defaultServer/dropins/Cafefe-1.0-SNAPSHOT.war

COPY src/main/liberty/config/server-docker.xml \
    /opt/ol/wlp/usr/servers/defaultServer/server.xml

COPY src/main/liberty/config/lib/postgresql-42.7.13.jar \
    /opt/ol/wlp/usr/servers/defaultServer/lib/postgresql-42.7.13.jar

EXPOSE 9080

CMD ["/opt/ol/wlp/bin/server", "run", "defaultServer"]