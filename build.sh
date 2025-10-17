#!/bin/bash
# Build script to create executable JAR

echo "Creating executable JAR..."

# Create output directory
mkdir -p build

# Compile Java files
echo "Compiling Java files..."
javac -cp "lib/json-20220924.jar" -d build src/*.java

# Copy resources
echo "Copying resources..."
cp src/map.json build/
cp -r src/sounds build/

# Extract JSON library
echo "Extracting JSON library..."
cd build
jar xf ../lib/json-20220924.jar
rm -rf META-INF

# Create manifest
echo "Creating manifest..."
cat > MANIFEST.MF << EOF
Manifest-Version: 1.0
Main-Class: Main

EOF

# Create JAR
echo "Creating executable JAR..."
jar cfm ../smoothpacman.jar MANIFEST.MF *

cd ..
echo "Done! Run with: java -jar smoothpacman.jar"