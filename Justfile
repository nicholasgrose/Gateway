build:
    ./gradlew build

test:
    ./gradlew test

server platform:
    ./gradlew :minecraft:platform:{{platform}}:runServer

docs:
    uv run zensical serve

docs-build:
    uv run zensical build
