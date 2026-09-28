#!/usr/bin/env bash

# Parse PORT from command line arguments (--port <num>) or environment variable $PORT
TARGET_PORT="${PORT:-8080}"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --port|-p)
      if [ -n "$2" ]; then
        TARGET_PORT="$2"
        shift 2
      else
        shift
      fi
      ;;
    *)
      shift
      ;;
  esac
done

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

echo "Starting Spring Boot Application on PORT $TARGET_PORT using $JAR_PATH..."
exec java -Xms128m -Xmx384m -Dserver.port=$TARGET_PORT -Dserver.address=0.0.0.0 -Djava.security.egd=file:/dev/./urandom -jar "$JAR_PATH"
