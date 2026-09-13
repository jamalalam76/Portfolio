#!/usr/bin/env bash
set -e

echo "=== Checking Java environment ==="

if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
    echo "Using existing JAVA_HOME: $JAVA_HOME"
else
    JAVAC_SYS=$(which javac 2>/dev/null || find /usr/lib/jvm /opt -name javac 2>/dev/null | head -n 1 || true)
    if [ -n "$JAVAC_SYS" ]; then
        export JAVA_HOME=$(dirname $(dirname "$JAVAC_SYS"))
        echo "Found system JAVA_HOME: $JAVA_HOME"
    else
        echo "Java JDK not found in container. Downloading portable OpenJDK 17..."
        mkdir -p .jdk
        if [ ! -x .jdk/bin/javac ]; then
            curl -sL "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.10%2B7/OpenJDK17U-jdk_x64_linux_hotspot_17.0.10_7.tar.gz" | tar -xz -C .jdk --strip-components=1
        fi
        export JAVA_HOME="$(pwd)/.jdk"
        echo "Portable JDK 17 installed at $JAVA_HOME"
    fi
fi

export PATH="$JAVA_HOME/bin:$PATH"

echo "=== Verifying Java & Compiler ==="
java -version
javac -version

chmod +x ./mvnw
echo "=== Building Spring Boot Jar with Maven ==="
./mvnw clean package -DskipTests

echo "=== Build Completed Successfully ==="
