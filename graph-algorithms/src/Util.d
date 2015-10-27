module Util;

import std.stdio;
import std.conv;
import std.string;
import std.algorithm;

void printDebug(const string output)
{
	debug stdout.writeln("DEBUG: " ~ output);
}

void printDebug(const char[] output)
{
	debug stdout.write("DEBUG: ");
	debug stdout.writeln(output);
}

static void removeAt(T)(ref T[] arr, size_t index)
{
	foreach (i, ref item; arr[index .. $ - 1])
		item = arr[i + 1];
	arr = arr[0 .. $ - 1];
	arr.assumeSafeAppend();
}

static int findAt(T)(ref T[] arr, T needle)
{
	for(int i = 0; i < arr.length; i++)
	{
		if(arr[i] == needle)
		{
			return i;
		}
	}

	return -1;
}

static int[] intersection(T)(T[] A, T[] B)
{
	int[] intersection;
	foreach(i; A)
	{
		if(canFind(B, i))
		{
			intersection ~= i;
		}
	}

	return intersection;
}

// Version using just arrays (it assumes the input to contain distinct items): 
T[][] powerSet(T)(in T[] s) pure nothrow @safe 
{
	auto r = new typeof(return)(1, 0);
	foreach (e; s) 
	{
		typeof(return) rs;
		foreach (x; r)
			rs ~= x ~ [e];
		r ~= rs;
	}
	return r;
}