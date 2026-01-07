APP_NAME=ariseth
JAR=build/libs/*.jar

.PHONY: build run test clean

build:
	./gradlew build

run:
	./gradlew bootRun

test:
	./gradlew test

clean:
	./gradlew clean