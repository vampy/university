#!/bin/bash

 # safe https://sipb.mit.edu/doc/safe-shell/
 set -euf -o pipefail

function usage()
{
    echo "Usage: $0 <directory>|--help"
}

# no arguments
if [ "$#" == "0" ]; then
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
    echo "Directory $1 does not exist"
    usage
    exit 1
fi

# format
 read -r -p "Are you sure? [y/N] " response
 response=${response,,}    # tolower
 if [[ $response =~ ^(yes|y)$ ]]; then
     echo "Formating..."
     find "$dir" -regex ".*\.\(hpp\|cpp\|c\|h\)" -print0 | xargs -0 clang-format-3.6 -style=file -i
 fi
