#!/bin/bash

# Run the command, capture the error output, and display it on stdout
./gradlew :spring-boot-project:spring-boot:compileJava 2>&1 >/dev/null | tee error_temp.txt

# Check for the --skip flag
skip_flag=false
for arg in "$@"; do
  if [ "$arg" == "--skip" ]; then
    skip_flag=true
    break
  fi
done

if [ "$skip_flag" = false ]; then
  # Remove any old error_output_<UUID>.txt files
  rm -f nullaway_error_*.txt

  # Generate a new UUID for the current error output
  uuid=$(uuidgen)

  # Save the first 4 lines to a unique file
  head -n 4 error_temp.txt > "nullaway_error_${uuid}.txt"

  # Print content of nullaway_error_<UUID>.txt
  echo "First NullAway error output:"
  cat "nullaway_error_${uuid}.txt"
  else
    echo "Skip flag detected. Displaying existing NullAway error files:"
    if ls nullaway_error_*.txt 1> /dev/null 2>&1; then
      for file in nullaway_error_*.txt; do
        echo "---- $file ----"
        cat "$file"
        echo
      done
    else
      echo "No NullAway error files found."
    fi
fi

# Clean up the temporary file
rm error_temp.txt
