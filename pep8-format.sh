#!/bin/bash

# safe https://sipb.mit.edu/doc/safe-shell/
set -euf -o pipefail

function usage()
{
    echo "Usage: $0 <directory>|--help"
}

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
    echo "Directory $1 does not exist"
    usage
    exit 1
fi

autopep8 --verbose --aggressive --aggressive --in-place --max-line-length 120 -r "$dir"
