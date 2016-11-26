#!/bin/bash

# 42. a)
# Write a shell script that reads a folder name from the keyboard and 
# computes the average number of lines from each text file inside it and its 
# subfolders.

if [ -z "$1" ]; then
    echo "Usage: $0 dirname"
    exit
fi 

dir="$1"

if [ ! -d "$dir" ]; then
    echo "$dir is not a directory"
    exit
fi

total_lines=0
nr_files=0

function calculate_lines()
{
    # iterate over each files
    for file in `ls $1`; do
        
        # found directory call recursively
        if [ -d $1/$file ]; then
            calculate_lines $1/$file
        elif [ -f $1/$file ]; then
            # add to count
            let total_lines+=`wc -l $1/$file  | awk  '{ print $1}'`
            let nr_files+=1
            #echo "Calculate file size $1/$file, $nr_files, $total_lines"
        else
            echo "WARNING: file type not known -> $1/$file"
        fi
    done
}

calculate_lines "$dir"

result=$[$total_lines/$nr_files]

#echo $total_lines $nr_files
echo "Average number of lines is $result"

