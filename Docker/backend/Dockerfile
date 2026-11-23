# Imagen del backend Cafefe: Debian estable + Open Liberty.
# Se construye desde la raiz del proyecto Cafefe (NetBeans):
#   docker build -f docker/backend/Dockerfile -t USUARIO/cafefe-backend:1.0 .
FROM debian:stable

# Java (la version que traiga Debian estable) y herramientas para bajar Open Liberty
RUN apt-get update \
    && apt-get install -y --no-install-recommends default-jre-headless curl unzip ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Open Liberty en /home/usuario/wlp
ENV LIBERTY_VERSION=26.0.0.6
RUN mkdir -p /home/usuario \
    && curl -fsSL -o /tmp/liberty.zip \
       https://repo1.maven.org/maven2/io/openliberty/openliberty-runtime/${LIBERTY_VERSION}/openliberty-runtime-${LIBERTY_VERSION}.zip \
    && unzip -q /tmp/liberty.zip -d /home/usuario \
    && rm /tmp/liberty.zip

# Servidor "cafefe": aqui se monta el .war en dropins/cafefe.war
RUN /home/usuario/wlp/bin/server create cafefe \
    && mkdir -p /home/usuario/wlp/usr/servers/cafefe/dropins \
                /home/usuario/wlp/usr/servers/cafefe/lib

# Driver de PostgreSQL (el mismo que usa el proyecto) y configuracion del servidor
COPY src/main/liberty/config/lib/postgresql-42.7.13.jar /home/usuario/wlp/usr/servers/cafefe/lib/postgresql-42.7.13.jar
COPY Docker/backend/server.xml /home/usuario/wlp/usr/servers/cafefe/server.xml

# Java 17+ no deja crear proxies de CDI sin esta opcion (error WELD-001524)
RUN echo "--add-opens=java.base/java.lang=ALL-UNNAMED" > /home/usuario/wlp/usr/servers/cafefe/jvm.options

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

EXPOSE 9080

CMD ["/home/usuario/wlp/bin/server", "run", "cafefe"]
