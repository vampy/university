#!/bin/bash

 # safe https://sipb.mit.edu/doc/safe-shell/
 set -euf -o pipefail

function usage()
{
    echo "Usage: $0 <directory> | --help"
}

# Try different clang-format commands...
if type "clang-format" > /dev/null 2>&1; then
    CLANG_FORMAT="clang-format"
elif type "clang-format-3.8" > /dev/null 2>&1; then
    CLANG_FORMAT="clang-format-3.8"
elif type "clang-format-3.7" > /dev/null 2>&1; then
    CLANG_FORMAT="clang-format-3.7"
else
    CLANG_FORMAT=""
fi

if [ -z "$CLANG_FORMAT" ]; then
    echo "No clang-format command can be found"
    exit 1
fi
echo "Using: $("$CLANG_FORMAT" --version)"

# no arguments
if [ $# -eq 0 ]; then
    usage
    exit 1
fi

dir="$1"
# display help
if [ "$dir" = "--help" ]; then
    usage
    exit 0
fi

# path does not exist
if ! [ -d "$dir" ]; then
    if [ -f "$dir" ]; then
        echo "Can not format a file, use: $CLANG_FORMAT -style=file -i $dir"
    else
        echo "Directory $1 does not exist"
    fi
    usage
    exit 1
fi

# format
read -r -p "Are you sure? [y/N] " response
response=${response,,}    # tolower
if [[ $response =~ ^(yes|y)$ ]]; then
    echo "Formating..."
    find "$dir" -regex ".*\.\(hpp\|cpp\|c\|h\)" -print0 | xargs -0 "$CLANG_FORMAT" -style=file -i
fi
