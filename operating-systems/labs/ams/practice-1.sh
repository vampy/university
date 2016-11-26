#!/bin/bash 

if [ -z $1 ]; then
    echo "Usage: $0 dir_name"
    exit
fi

if [ ! -d $1 ]; then
    echo "$1 is not a directory"
    exit
fi

dir=$1

echo "Creating $dir.zip"
zip -r $dir.zip $dir &> /dev/null

echo "Creating $dir.tar"
tar -cvf $dir.tar $dir &> /dev/null

echo "Creating $dir.tar.gz"
tar -zcvf $dir.tar.gz $dir &> /dev/null

echo "Creating $dir.tar.bz2"
tar -jcvf $dir.tar.bz2 $dir 
