#!/bin/sh

echo "Running detekt check..."
OUTPUT="/tmp/detekt-$(date +%s)"
./gradlew detektAllModulesWithAutocorrect > "$OUTPUT"
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
  cat "$OUTPUT"
  rm "$OUTPUT"
  echo "***********************************************"
  echo "                 Detekt failed                 "
  echo "          Please fix the above issues          "
  echo "***********************************************"
  exit 0
fi
rm "$OUTPUT"
