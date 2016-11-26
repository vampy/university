#!/bin/bash 

if [ -z $1 ]; then
    echo "Usage: $0 dir_name"
    exit
fi

function explode_dir()
{
    if [ ! -d $1 ]; then
        echo "$1 is not a directory"
        return
    fi
    
    local level=0
    if [ -n "$2" ]; then
        level=$2
    fi
    
    for file in `ls $1`; do
        if [ -d $1/$file ]; then
            v=$(printf "%-${level}s" "-")
            echo -n "${v// /-}"
            echo $1/$file
            
            local temp=`expr $level + 1`
            explode_dir $1/$file $temp

        elif [ -f $1/$file ]; then
            v=$(printf "%-${level}s" "-")
            echo -n "${v// /-}"
            echo $1/$file
            
        else
            echo Something Else
            file $1/$file
        fi
    done
}


dir=$1


explode_dir $dir
