FROM docker.io/gradle:jdk17 as buildstage

WORKDIR /work

# Copy source files into /work/src
COPY src src/
# Copy gradle files into /work/gradle
COPY gradle gradle/
# Copy build files into /work (current WORKDIR)
COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew .

RUN gradle clean installDist

FROM docker.io/eclipse-temurin:21

COPY --from=buildstage /work/build/install/waltid-kyb-onboarding /waltid-kyb-onboarding

WORKDIR /waltid-kyb-onboarding

EXPOSE 7004

ENTRYPOINT ["/waltid-kyb-onboarding/bin/waltid-kyb-onboarding"]