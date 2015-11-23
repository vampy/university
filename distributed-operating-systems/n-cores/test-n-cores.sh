#!/bin/bash

BINARY="./n-cores"

if [[ ! -f $BINARY ]]; then
    echo "n-cores binary does not exist in this directory."
    echo "Compile it with: gcc n-cores.c -g -Wall -pthread -o n-cores"
    exit 1
fi



for nr_cores in $(seq 0 "$(nproc --ignore=1)"); do
    passed=0
    tested=0

    for _ in $(seq 1 25); do
        ((tested++))
        return_value=$(taskset -c "$nr_cores" $BINARY)
        if [[ $return_value -eq $nr_cores ]]; then
            ((passed++))
        fi
    done

    status="OK"
    if [[ $passed -ne $tested ]]; then
        status="FAIL"
    fi

    ((nr_cores++))
    echo "$nr_cores cores: $passed/$tested tests passed -> $status"
done
