#!/usr/bin/env bash

if [ -d "$(pwd)/.jdk" ]; then
    export JAVA_HOME="$(pwd)/.jdk"
    export PATH="$JAVA_HOME/bin:$PATH"
fi

if [ -f "target/portfolio-1.0.0.jar" ]; then
    JAR_PATH="target/portfolio-1.0.0.jar"
elif [ -f "portfolio/target/portfolio-1.0.0.jar" ]; then
    JAR_PATH="portfolio/target/portfolio-1.0.0.jar"
else
    JAR_PATH=$(find . -name "portfolio-*.jar" | head -n 1)
fi

echo "Starting Spring Boot Application using $JAR_PATH..."
exec java -Dserver.port=${PORT:-8080} -jar "$JAR_PATH"
