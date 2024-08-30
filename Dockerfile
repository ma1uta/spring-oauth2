FROM docker.io/bellsoft/liberica-native-image-kit-container:jdk-21-nik-23.1-glibc as build

WORKDIR /build
COPY . .

RUN ls
RUN ls gradle
RUN ls gradle/wrapper
RUN ./gradlew :jibDockerBuild
