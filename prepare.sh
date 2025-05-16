#!/bin/bash

# Step 1: Make the directory
mkdir -p annotator-out/0

# Get the absolute path to annotator-out/0
abs_path="$(cd annotator-out/ && pwd)"

# Step 2: Write nullaway.xml
cat > "$abs_path/nullaway.xml" <<EOF
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<serialization>
    <suggest active="true" enclosing="true" />
    <fieldInitInfo active="true" />
    <path>$abs_path/0</path>
    <uuid>1a714bd6-3896-4a58-9706-379a0d966a9e</uuid>
</serialization>
EOF

# Step 3: Write scanner.xml
cat > "$abs_path/scanner.xml" <<EOF
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<scanner>
    <serialization active="true" />
    <uuid>80d60ff3-bbd9-48a2-95e3-9d329ee53ac2</uuid>
    <path>$abs_path/0</path>
    <processor>
        <LOMBOK active="true" />
    </processor>
    <annotations />
    <annotatedPackages />
</scanner>
EOF


project_root="$(pwd)"
echo -e "$abs_path/nullaway.xml\t$abs_path/scanner.xml" >> "$project_root/paths.tsv"