module Graph;

import std.stdio;
import std.conv;
import std.string;
import std.typecons;
import std.container;
import std.algorithm;
import std.container;

import Util;
import Vertex;

// target, cost
alias TestTuple = Tuple!(int, int[]);
alias Queue = DList;

class DiGraph(T)
{
public:
	int maxDistance = int.max;

	this()
	{
		
	}

	override string toString()
	{
		return format("vertexMap = %s \n edgesOut = %s \n edgesIn = %s", 
		              to!string(this.vertexMap),
		              to!string(this.edgesOut),
		              to!string(this.edgesIn));
	}

	bool vertexExists(T id)
	{
		if(id in this.vertexMap)
		{
			printDebug(format("vertexExists(%s) = 1", to!string(id)));
			return true;
		}

		printDebug(format("vertexExists(%s) = 0", to!string(id)));
		return false;
	}
	
	ulong getNumVertices()
	{
		return this.edgesOut.length;
	}

	// WARNING only for int
//	void setNumberVertices(int numVertices)
//	{
//		for(int i = 0; i < numVertices; i++)
//		{
//			this.addVertex(i);
//		}
//	}
	
	bool isEdge(T source, T target)
	{
		// check if in dictionary
		if(source !in this.edgesOut)
		{
			return false;
		}

		bool found = canFind(this.edgesOut[source], target);
		printDebug(format("isEdge - canFind = %d", found));

		return  found;
	}
	
	ulong getInDegree(T id)
	{
		return this.edgesIn[id].length;
	}
	
	ulong getOutDegree(T id)
	{
		return this.edgesOut[id].length;
	}
	
	T[] getOutConnections(T id)
	{
//		printDebug(format("edgesOut = %s", to!string(this.edgesOut)));
		return this.edgesOut[id];
	}
	
	T[] getInConnections(T id)
	{
//		printDebug(format("edgesIn = %s", to!string(this.edgesIn)));
		return this.edgesIn[id];
	}

	T[] getAllVertices()
	{
		return this.vertexMap.keys;
	}
	
	void setEdgeCost(T source, T target, int cost)
	{
		this.vertexMap[source].setCost(target, cost);
	}
	
	int getEdgeCost(T source, T target)
	{
		//printDebug(format("vertexMap = %s", to!string(vertexMap)));
		return this.vertexMap[source].getCost(target);
	}

	void addVertex(T id)
	{
		if(id !in this.vertexMap)
		{
			this.vertexMap[id] = new VertexEl!T(id);
		}
		if(id !in this.edgesOut)
		{
			this.edgesOut[id] = [];
		}
		if(id !in this.edgesIn)
		{
			this.edgesIn[id] = [];
		}
	}

	void addVertexDuration(T id, int duration)
	{
		Util.printDebug("addVertex");
		if(id !in this.vertexMap)
		{
			this.vertexMap[id] = new VertexEl!T(id);
			this.duration[id] = duration;
			this.earlyStart[id] = 0;
			this.earlyFinish[id] = 0;
			this.lateStart[id] = 0;
			this.lateFinish[id] = 0;
		}
		if(id !in this.edgesOut)
		{
			this.edgesOut[id] = [];
		}
		if(id !in this.edgesIn)
		{
			this.edgesIn[id] = [];
		}
	}

	void removeVertex(T id)
	{
		// TODO remove connections
		if(id in this.vertexMap)
		{
			this.vertexMap.remove(id);
		}
	}
	
	void addEdgeCost(T source, T target, int cost)
	{
		this.addVertex(source);
		this.addVertex(target);
		printDebug(format("%s to %s, cost = %d", to!string(source), to!string(target), cost));

		this.setEdgeCost(source, target, cost);
		
		// add to central dictionary
		this.edgesOut[source] ~= target;
		this.edgesIn[target] ~= source;
	}

	void addEdge(T source, T target)
	{
		// check if added
		if(source !in this.vertexMap)
		{
			//throw new Exception("Source not in vertexMap");
		}
		if(target !in this.vertexMap)
		{
			//throw new Exception("Target not in vertexMap");
		}
		
		printDebug(format("%s to %s", to!string(source), to!string(target)));

		// add to central dictionary
		this.edgesOut[source] ~= target;
		this.edgesIn[target] ~= source;
	}
	
	void addEdgeList(T source, T[] targets)
	{
		foreach(target; targets)
		{
			this.addEdge(source, target);
		}
	}

	void removeEdge(T source, T target)
	{
		int foundTarget = findAt(this.edgesOut[source], target);
		int foundSource = findAt(this.edgesIn[target], source);

		if(foundSource == -1 || foundTarget == -1)
		{
			writeln(this.edgesOut);
		    writeln(this.edgesIn);
			writeln(format("Error removing edge %s to %s", to!string(source), to!string(target)));
			return;
		}

		removeAt(this.edgesOut[source], foundTarget); // remove target
		removeAt(this.edgesIn[target], foundSource); // remove source
	}

	/*
	 * input: a map of the form child => parent
	 * return the path traversed 
	*/
	T[] traverseCameFrom(in T[T] cameFrom, in T destination)
	{
		T[] return_path = [destination]; // returned to the user
		T traverse_id = destination;
		while(traverse_id in cameFrom)
		{
			// source destination
			// printDebug(format("bfs - %s %s", to!string(cameFrom[traverse_id]), to!string(traverse_id)));
			traverse_id = cameFrom[traverse_id];
			return_path ~= traverse_id;
		}
		
		return return_path.reverse;
	}

	/*
	 * 
	 * return a tuple of the form (minDistance, path vector) 
	*/
	Tuple!(int, T[]) lowestCostDijkstra(in T root, in T destination)
	{
		// Tuple!(int, T) = Tuple(distance, id of node)
		auto qu = redBlackTree!("a < b", false, Tuple!(int, T));

		// the map of navigation node, destination => source
		T[T] cameFrom;

		// min distance map, destination => cost(distance)
		int[T] minDistance;

		// initialise distance
		foreach(neighbour; this.getAllVertices())
		{
			minDistance[neighbour] = this.maxDistance;
		}
		minDistance[root] = 0;

		qu.insert(Tuple!(int, T)(minDistance[root], root));
		printDebug(to!string(minDistance));

		while(!qu.empty())
		{
			T current = qu.front()[1]; qu.removeFront();

			// iterate over neighbours
			foreach(neighbour; this.getOutConnections(current))
			{
				//printDebug(format("dijkstra - current = %s, neighbour = %s. minDistance = %s", to!string(current), to!string(neighbour), to!string(minDistance)));
				int alt = minDistance[current] + this.getEdgeCost(current, neighbour);
				if(alt < minDistance[neighbour])
				{
					// remove current if any
					qu.removeKey(Tuple!(int, T)(minDistance[neighbour], neighbour));

					// update new distance and path
					minDistance[neighbour] = alt;
					cameFrom[neighbour] = current;

					// heapify down, update with new cost
					qu.insert(Tuple!(int, T)(minDistance[neighbour], neighbour));
				}
			}
		}

		// destination is isolated
		if(minDistance[destination] == this.maxDistance)
		{
			T[] empty;
			return tuple(this.maxDistance, empty);
		}

		return tuple(minDistance[destination], traverseCameFrom(cameFrom, destination));
	}

	T[] lowestLengthPath(in T root, in T destination)
	{
		auto qu = Queue!T();

		// visited vector
		T[] visited;

		// the map of navigation node, destination => source
		T[T] cameFrom;

		qu.insertBack(root);
		visited ~= root;
		while(!qu.empty())
		{
			T current = qu.front(); qu.removeFront();
//			printDebug(format("bfs - current = %s, visited = %s, queue = %s,", to!string(current), to!string(visited), to!string(qu)));

			// found the path
			if(current == destination)
			{
				return this.traverseCameFrom(cameFrom, destination);
			}

			// iterate over neighbours
			foreach(neighbour; this.getOutConnections(current))
			{
//				printDebug(format("bfs - current = %s, neighbour = %s. visited = %s", to!string(current), to!string(neighbour), to!string(visited)));
				// not visited add to queue
				if(!canFind(visited, neighbour))
				{
					visited ~= neighbour;
					cameFrom[neighbour] = current;
					qu.insertBack(neighbour);
				}
			}
		}

		return [];
	}

	// Also checks if not DAG
	T[] topSortDFS()
	{
		// set mark, -1 unmarked, 0 marked temporary, 1 marked
		int[T] visited;
		T[] stack;
		foreach(vertex; this.getAllVertices())
		{
			visited[vertex] = -1;
		}
		
		void dfs(T vertex)
		{
			// if temporary not good
			if(visited[vertex] == 0)
			{
				throw new Exception("Graph is not a DAG");
			}
			
			// not visited
			if(visited[vertex] == -1)
			{
				visited[vertex] = 0;
				foreach(neighbour; this.getOutConnections(vertex))
				{
					dfs(neighbour);
				}
				visited[vertex] = 1;
				stack ~= vertex;
			}
		}
		
		while(true)
		{
			bool unmarkedNodes = true;
			foreach(T vertex, int marked; visited)
			{
				// node is unmarked
				if(marked == -1)
				{
					unmarkedNodes = false;
					dfs(vertex);
				}
			}
			
			if(unmarkedNodes)
			{
				break;
			}
		}
		
		printDebug(to!string(stack.reverse));
		return stack.reverse;
	}

	/*
	 * Perform a DFS on the graph
	 * @return came_from, pre_order, post_order 
	*/
	Tuple!(T[T], T[], T[]) searchDFS()
	{
		int[T] visited; 		// List for marking visited and non-visited nodes
		T[T] spanning_tree; // Spanning tree aka cameFrom can use traverseCameFrom
		T[] pre; 		  		// Graph's preordering
		T[] post;       		// Graph's postordering
		
		void dfs(T node)
		{
			// Depth-first search subfunction
			visited[node] = 1;
			pre ~= node;
			
			// Explore recursively the connected component
			foreach(each; this.getOutConnections(node))
			{
				if(each !in visited)
				{
					spanning_tree[each] = node;
					dfs(each);
				}
			}
			post ~= node;
		}
		
		// Algorithm loop
		foreach(each; this.getAllVertices())
		{
			// Select a non-visited node
			if(each !in visited)
			{
				// Explore node's connected component
				dfs(each);
			}
		}
		
		return tuple(spanning_tree, pre, post);
	}

	/*
	 * @return all the strongly connected components 
	*/
	T[][] tarjanStronglyConnected()
	{
		// return to the user
		T[][] return_components;

		int[T] index;
		int[T] lowlink;
		T[] stack;
		int index_value = 0;
		
		void strongconnect(T vertex)
		{
			// Set the depth index for v to the smallest unused index
			index[vertex] = index_value;
			lowlink[vertex] = index_value;
			index_value++;
			stack ~= vertex;
			
			// Consider successors of v
			foreach(neighbour; this.getOutConnections(vertex))
			{
				if(neighbour !in index)
				{
					// Successor neighbour has not yet been visited; recurse on it
					strongconnect(neighbour);
					lowlink[vertex] = min(lowlink[vertex], lowlink[neighbour]);
				}
				else if(canFind(stack, neighbour))
				{
					// Successor neighbour is in stack S and hence in the current SCC
					lowlink[vertex] = min(lowlink[vertex], index[neighbour]);
				}
			}
			
			// If vertex is a root node, pop the stack and generate an SCC
			if(lowlink[vertex] == index[vertex])
			{
				T[] component;
				T temp_node;
				do
				{
					ulong i = stack.length - 1;
					temp_node = stack[i]; removeAt(stack, i);
					component ~= temp_node;
				} while(temp_node != vertex);
				return_components ~= component;
			}
		}

		// iterate over all nodes
		foreach(vertex; this.getAllVertices())
		{
			if(vertex !in index)
			{
				strongconnect(vertex);
			}
		}

		return return_components;
	}

	/*
	 * @return the nodes in topologica order 
	 * @throws Exception on not DAG
	*/
	T[] topSort()
	{
		T[] topological_nodes;

		foreach(component; this.tarjanStronglyConnected().reverse)
		{
			// not a DAG graph
			if(component.length != 1)
			{
				throw new Exception("Graph is not a DAG");
			}

			topological_nodes ~= component[0];
		}

		return topological_nodes;
	}

	void calculateTimings(T[] topological_nodes)
	{
		// run in topological order, calculate earlyStart and earlyFinish
		foreach(node; topological_nodes)
		{
			int max_prev_early_finish = 0;
			
			// we must check all the predecessors, max early finish
			foreach(pre; this.getInConnections(node))
			{
				if(this.earlyFinish[pre] > max_prev_early_finish)
				{
					max_prev_early_finish = this.earlyFinish[pre];
				}
			}
			this.earlyStart[node] = max_prev_early_finish;
			this.earlyFinish[node] = max_prev_early_finish + this.duration[node];
			
			printDebug(format("node = %s, earlyStart = %s, earlyFinish = %s\n", 
			                  to!string(node), 
			                  to!string(this.earlyStart[node]), 
			                  to!string(this.earlyFinish[node]))
			           );
		}
		
		// calculate total time by the max early finish, no need for virtual nodes
		foreach(k, v; this.earlyFinish)
		{
			if(v > this.totalTime)
			{
				this.totalTime = v;
			}
		}
		
		// run in inverse topological order, calculate lateStart and lateFinish
		foreach(node; topological_nodes.reverse)
		{
			int min_next_late_start = this.totalTime;
			
			// check all the next nodes, min late start
			foreach(next; this.getOutConnections(node))
			{
				if(this.lateStart[next] < min_next_late_start)
				{
					min_next_late_start = this.lateStart[next];
				}
			}
			this.lateFinish[node] = min_next_late_start;
			this.lateStart[node] = min_next_late_start - this.duration[node];
			printDebug(format("node = %s, lateStart = %s, lateFinish = %s\n", 
			                  to!string(node), 
			                  to!string(this.lateStart[node]), 
			                  to!string(this.lateFinish[node]))
			           );
		}
	}

	T[] criticalPath()
	{
		T[] topological_nodes = this.searchDFS()[2].reverse;
		T[] critical_path;
		
		this.calculateTimings(topological_nodes);

		// find critical activities
		// TODO modularize
		foreach(k, v; this.earlyStart)
		{
			if(this.earlyStart[k] == this.lateStart[k] && this.earlyFinish[k] == this.lateFinish[k])
			{
				critical_path ~= k;
			}
		}

		return critical_path;
	}

	int[T] getEarlyStart()
	{
		return this.earlyStart;
	}

	int[T] getEarlyFinish()
	{
		return this.earlyFinish;
	}

	int[T] getLateStart()
	{
		return this.lateStart;
	}

	int[T] getLateFinish()
	{
		return this.lateFinish;
	}

	int getTotalTime()
	{
		return this.totalTime;
	}

protected:
	// T(aka id) => Vertex
	VertexEl!T[T] vertexMap;

	// info
	int[T] duration;
	int[T] earlyStart;
	int[T] earlyFinish;
	int[T] lateStart;
	int[T] lateFinish;
	
	int totalTime = 0;
	
	// keep the id of the edges
	// id => [array of ids]
	T[][T] edgesIn;
	T[][T] edgesOut;
}
