#!/usr/bin/env rdmd
module main;

import std.stdio;
import std.file;
import std.conv;
import std.string;
import std.exception;
import std.algorithm;

import Graph;
import UGraph;
import Vertex;
//import Console;
import Util;

//void readFileIntoGraph(int graph, const string filename)
//{
//	// check
//	assert(exists(filename) == true, "File NOT found. Quiting");
//	
//	auto fileGraph = File(filename);
//	
//	// read first line
//	string[] firstLineArray = fileGraph.readln.strip.split;
//	
//	assert(firstLineArray.length == 2, "First line does not have 2 numbers");
//	
//	int nr_vertices = to!int(firstLineArray[0]), nr_edges = to!int(firstLineArray[1]);
//	printDebug(format("Vertices = %d, Edges = %d", nr_vertices, nr_edges));
//
//	graph.setNumberVertices(nr_vertices);
//	// read the edges
//	int actuallyRead = 0;
//	foreach(string tempLine; lines(fileGraph))
//	{
//		tempLine = tempLine.strip();
//		printDebug(tempLine);
//		string[] tempLineArray = tempLine.split;
//		assert(tempLineArray.length == 3, "Edge definition is wrong in file");
//		int sourceVertex = to!int(tempLineArray[0]), targetVertex = to!int(tempLineArray[1]),
//			edgeCost = to!int(tempLineArray[2]);
//		
//		graph.addEdge(sourceVertex, targetVertex, edgeCost);
//
//		actuallyRead++;
//	}
//	assert(actuallyRead == nr_edges, "Number of edges differs from the header.");
//}

void readFileActivitiesIntoGraph(DiGraph!string graph, const string filename)
{
	// check
	assert(exists(filename) == true, "File NOT found. Quiting");

	auto fileActivities = File(filename);

	foreach(string line; lines(fileActivities))
	{
		string[] line_split = line.strip().split(" ");
		if(line_split.length <= 1)
		{
			printDebug("Ignoring line");
			continue;
		}

		string node_id = line_split[0];
		int node_duration = to!int(line_split[1]);

		graph.addVertexDuration(node_id, node_duration);

		// add prerequisites
		if(line_split.length > 2)
		{
			for(int i = 2; i < line_split.length; i++)
			{
				string pre_id = line_split[i];

				graph.addEdge(pre_id, node_id);
				//writeln("From " ~ pre_id ~ " to " ~ node_id);
			}
		}
	}
}

void readFileUndirectedIntoGraph(UGraph graph, const string filename)
{
	// check
	assert(exists(filename) == true, "File NOT found. Quiting");
	
	auto fileActivities = File(filename);

	bool firstLine = true;
	foreach(string line; lines(fileActivities))
	{
		string[] line_split = line.strip().split(" ");
		if(line_split.length == 0 || firstLine)
		{
			printDebug("Ignoring line");
			firstLine = false;
			continue;
		}

		int node_id = to!int(line_split[0]);
		for(int i = 1; i < line_split.length; i++)
		{
			int neighbour = to!int(line_split[i]);

			graph.addEdge(node_id, neighbour);
		}
	}
}

int main(string[] args)
{
	// TODO add file dynamicly
	string filename = "graph.dat";

	// use other file from the command line
	if(args.length >= 2)
	{
		filename = args[1];
	}

	try
	{
		auto graph = new DiGraph!int;
		// laburi 1-3 nu functioneza ca am trecut pe template
		//readFileIntoGraph(graph, filename);
		//auto ui = new Console(graph, "Graph Program");
		//ui.run();


		// work 4
//		auto graphdata = new DiGraph!string;
//		readFileActivitiesIntoGraph(graphdata, "activities.dat");
//		try
//		{
//			//graphdata.topSortDFS();
//
//			writefln("Topological order: %s\n", to!string(graphdata.topSort()));
//
//
//			string[] critical_activities = graphdata.criticalPath();
//			int[string] earlyStart = graphdata.getEarlyStart;
//			int[string] earlyFinish = graphdata.getEarlyFinish;
//			int[string] lateStart = graphdata.getLateStart;
//			int[string] lateFinish = graphdata.getLateFinish;
//			int totalTime = graphdata.getTotalTime;
//
//			foreach(v, n; earlyStart)
//			{
//				writefln("Node = %s, ES = %d, EF = %d, LS = %d, LF = %d", v, earlyStart[v], earlyFinish[v], lateStart[v], lateFinish[v]);
//			}
//
//			writefln("\nTotal time = %d", totalTime);
//
//			writefln("\nCritical activites = %s", to!string(critical_activities));
//
//		}
//		catch(Exception e)
//		{
//			writeln("ERROR: " ~ e.msg);
//		}


		// work 5
		auto ugraph = new UGraph();
		readFileUndirectedIntoGraph(ugraph, "undirected.dat");
		writefln("Maximum size clique: %s", to!string(ugraph.maxClique()));
//
//		// wikipedia graph
//		graphdata.addVertex(7, 1);
//		graphdata.addVertex(5, 2);
//		graphdata.addVertex(3, 5);
//		graphdata.addVertex(11, 4);
//		graphdata.addVertex(8, 2);
//		graphdata.addVertex(2, 10);
//		graphdata.addVertex(9, 1);
//		graphdata.addVertex(10, 3);
//
//		graphdata.addEdgeList(7, [11, 8]);
//		graphdata.addEdgeList(5, [11]);
//		graphdata.addEdgeList(3, [8, 10]);
//		graphdata.addEdgeList(11, [2, 9, 10]);
//		graphdata.addEdgeList(8, [9]);
//
//		// video graph
//		graphdata.addVertexDuration("A", 2); // A
//		graphdata.addVertexDuration("B", 1); // B
//		graphdata.addVertexDuration("C", 6); // C
//		graphdata.addVertexDuration("D", 5); // D
//		graphdata.addVertexDuration("E", 4); // E
//		graphdata.addVertexDuration("F", 2); // F
//
//		graphdata.addEdge("A", "B");
//		graphdata.addEdge("B", "E");
//		graphdata.addEdgeList("C", ["E", "F"]);
//		graphdata.addEdgeList("D", ["B", "C"]);
//

////		ugraph.addEdgeList(1, [2, 3, 4]);
////		ugraph.addEdgeList(2, [1, 3, 4]);
////		ugraph.addEdgeList(3, [1, 2, 4]);
////		ugraph.addEdgeList(4, [1, 2, 3, 5, 8]);
////		ugraph.addEdgeList(5, [4, 6, 7]);
////		ugraph.addEdgeList(6, [5, 7]);
////		ugraph.addEdgeList(7, [5, 6]);
////		ugraph.addEdgeList(8, [4, 9, 10]);
////		ugraph.addEdgeList(9, [8, 10]);
////		ugraph.addEdgeList(10, [8, 9]);
	}
	catch(Error err)
	{
		stdout.writeln("An error occured reading the file:  ", err.msg);
	}


	// Lets the user press <Return> before program returns
//	stdin.readln();

	return 0;
}
