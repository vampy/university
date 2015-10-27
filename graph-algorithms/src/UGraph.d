module UGraph;

import std.stdio;
import std.conv;
import std.string;
import std.typecons;
import std.container;
import std.algorithm;
import std.container;

import Util;

class UGraph
{
public:
	this()
	{
		
	}
	
	ulong getInDegree(int id)
	{
		return this.edges[id].length;
	}
	
	ulong getOutDegree(int id)
	{
		return this.edges[id].length;
	}
	
	int[] getConnections(int id)
	{
		return this.edges[id];
	}
	
	int[] getAllVertices()
	{
		return this.edges.keys;
	}
	
	bool isEdge(int source, int target)
	{
		// check if in dictionary
		if(source !in this.edges)
		{
			return false;
		}
		
		return canFind(this.edges[source], target);
	}
	
	void addEdge(int source, int target)
	{
		printDebug(format("%d to %d", source, target));
		
		if(source !in this.edges)
		{
			this.edges[source] = [];
		}
		if(target !in this.edges)
		{
			this.edges[target] = [];
		}
		
		if(!canFind(this.edges[source], target))
		{
			this.edges[source] ~= target;
		}
		if(!canFind(this.edges[target], source))
		{
			this.edges[target] ~= source;
		}	
	}
	
	void addEdgeList(int source, int[] targets)
	{
		foreach(int target; targets)
		{
			this.addEdge(source, target);
		}
	}
	
	int[] maxClique()
	{
		int[][] candidates = this.powerGraph();

		int[][] keepEm;
		foreach(int[] candidate; candidates)
		{
			if(this.allConnected(candidate))
			{
				keepEm ~= candidate;
			}
		}
		
		ulong bestLength = 0;
		int[] bestSolution;
		foreach(int[] test; keepEm)
		{
			if(test.length > bestLength)
			{
				bestLength = test.length;
				bestSolution = test;
			}
		}
		
		return bestSolution.sort;
	}
	
protected:
	int[][int] edges;
	
	int[][] powerGraph()
	{
		return powerSet(this.getAllVertices);
	}
	
	bool allConnected(int[] candidate)
	{
		foreach(int n; candidate)
		{
			foreach(int m; candidate)
			{
				if(n != m)
				{
					if(!canFind(this.getConnections(m), n))
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
