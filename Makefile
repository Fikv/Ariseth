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
	set -a && . ./.env && set +a && ./gradlew bootRun

test: setup
	set -a && . ./.env && set +a && ./gradlew test

env-show: setup
	set -a && . ./.env && set +a && env | grep -E 'SECURITY_|DB_|APP_'