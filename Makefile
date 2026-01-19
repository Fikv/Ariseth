APP_NAME=ariseth
JAR=build/libs/*.jar

help:
	@echo "Available commands:"
	@echo "  make setup       - create .env from example"
	@echo "  make run         - run app with local env"
	@echo "  make test        - run tests with env"
	@echo "  make env-show    - show current env"

setup:
	@if [ ! -f .env ]; then cp .env.example .env; fi

run: setup
	export $$(cat .env | xargs) && ./gradlew bootRun

test: setup
	export $$(cat .env | xargs) && ./gradlew test

env-show:
	export $$(cat .env | xargs) && env | grep -E 'SECURITY_|DB_|APP_'