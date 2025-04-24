FROM docker.io/gradle:jdk17 as buildstage

WORKDIR /work

COPY src/ /src
COPY gradle/ /gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew /


RUN gradle clean installDist

FROM docker.io/eclipse-temurin:21

COPY --from=buildstage /work/build/install/ /

#COPY config /waltid-kyb-onboarding/config
WORKDIR /waltid-kyb-onboarding



EXPOSE 7004

ENTRYPOINT ["/waltid-kyb-onboarding/bin/waltid-kyb-onboarding"]