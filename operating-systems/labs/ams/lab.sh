#!/bin/bash

# A shell script that gets as parameters
# an operation and arbitrarly number of numbers
# ex sum 1 2 3 4
# If the operation is sum print the sum
# If operation is mul print mul
# else show invalid

echo $@
shift
echo $(sum "$*")
echo $#

if [ $1 = "sum" ]
then
    echo "Sum"
    shift
    sum=0
    for i in $@; do
        sum=`expr $sum + $i`
    done
    echo $sum
elif [ $1 = "mul" ]
then
    echo "Mul"
    shift
    mul=1
    for i in $@; do
        mul=$(($mul * $i))
    done
    echo $mul
else
    echo "Invalid"
fi
