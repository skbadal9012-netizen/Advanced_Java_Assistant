#!/usr/bin/env bash
# Script to compile and run Advanced Java Assistant on Linux/macOS

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

BIN_DIR="out"
mkdir -p "$BIN_DIR"

echo "Compiling Java Assistant source files..."
find src -name "*.java" > sources.txt
javac -d "$BIN_DIR" @sources.txt
rm sources.txt

echo "Compilation successful!"
echo "Starting Advanced Java Assistant..."
java -cp "$BIN_DIR" com.assistant.Main
