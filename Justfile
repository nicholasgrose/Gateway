build:
    ./gradlew build

test:
    ./gradlew test

lint:
    ./gradlew detekt

scan:
    ./gradlew qodana

server platform:
    ./gradlew :minecraft:platform:{{platform}}:runServer

docs:
    uv run zensical serve

docs-build:
    uv run zensical build
