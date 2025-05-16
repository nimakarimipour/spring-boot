#!/bin/bash

git add .
git commit -m "resolve"
git push
if [[ "$(uname)" == "Darwin" ]]; then
    git rev-parse --verify HEAD | pbcopy && pbpaste
else
    # just print it on output
    git rev-parse --verify HEAD
fi