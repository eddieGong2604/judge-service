FROM asia.gcr.io/tokyo-eye-313109/base-image-java:0.1.0

ARG service_name

ENV service_name ${service_name}

ADD target/${service_name}-*.jar /opt/app/${service_name}.jar

EXPOSE 9090

ENTRYPOINT  ["sh","-c","java -jar /opt/app/${service_name}.jar"]

