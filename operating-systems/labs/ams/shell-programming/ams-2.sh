#!/bin/bash

# 42. b)
# Write a shell script that takes pairs of parameters (a filename and a number 
# n) and outputs for each pair the name of the file, the number n and the n'th 
# word from each file.

isEven=`expr $# % 2`
nrRegex='^[0-9]+$'

if [ $isEven -eq 1 ] || [ $# -eq 0 ]; then
    echo "Usage: $0 file1 n1 file2 n2 ..."
    exit
fi

while [ $# -gt 0 ]; do

    # take 2 pairs
    if [ ! -f $1 ]; then
        echo "ERROR: $1 is not a file"
        exit
    fi
    filename="$1"
    shift
    
    if ! [[ "$1" =~ $nrRegex ]]; then
        echo "ERROR: $1 is not an integer positive number"
        exit
    fi
    n=$1
    shift
    
    # check if file has that many words
    file_total_words=`cat $filename | wc -w`
    if [ $n -gt $file_total_words ]; then
        echo "WARNING: $filename has only $file_total_words words"
        continue
    fi
    
    current_word_count=0
    previous_word_count=0
    
    while read line; do
        # keep previous
        let previous_word_count=current_word_count
        let current_word_count+=`echo $line | wc -w`
        
        # we found our line
        if [ $current_word_count -ge $n ]; then
            
            # iterate over line
            for word in $line; do
                # count each word until we reach n
                # previous_word_count has the word count until the
                # start of this line
                let previous_word_count+=1
                
                # bingo
                if [ $previous_word_count -eq $n ]; then
                    echo "$filename" $n $word
                    break
                fi
            done
            break
        fi
        
        #echo $line
        #echo $current_word_count
    done < $filename
done
