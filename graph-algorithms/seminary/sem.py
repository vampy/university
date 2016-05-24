def buildGraph():
    g = graph.DoubleDictGraph(5)


def topsort(g):
    viz = set()
    stack = []
    
    # iterate over all vertices
    for i in g.parseX():
        if (i in viz): 
            continue
        dfs(i, viz, stack, g)
        
    stack.reverse()
    
    # run once, g is a graph object
    if not cycle(stack,g): 
        return stack
    
    return None

def dfs(node, viz, stack, g):
    viz.add(node)
    for i in g.parseNout(node):
        if (i in viz): 
            continue
            
        dfs(i, viz, stack, g)
        
    stack.append(node)


def cycle(stack, g):
""" Cycle verifica daca rezultatul toposortului(stackul) are sau nu cicluri"""
    viz = set()
    for i in stack:
        if i in viz: 
            continue
            
        viz.add(i)
        for j in g.parseNin(i):
            if (j in viz): 
                continue
                
            return True
            
    return False

def findAllWalks(g):
    walks = []
    for i in g.parseX():
        walks = walks + findAllWalksFrom(g, i)
        
    return walks


def findAllWalksFrom(g, node):
    list = [[node]]
    for i in g.parseNout(node):
        for walk in findAllWalksFrom(g, i):
            list.append([node] + walk)
            
    return list

def findNRWalks(g):
    cache = {}
    nrwalks = 0
    for i in g.parseX():
        nrwalks = nrwalks + findNrWalksFrom(g, i, cache)
        
    return nrwalks


def findNrWalksFrom(g, node, cache):
    # retrieve from cache
    if node in cache:
        return cache[node]
        
    nrwalks = 1
    for i in g.parseNout(node):
        nrwalks = nrwalks + findNrWalksFrom(g, i, cache)
        
    cache[node] = nrwalks
    print "%s -> %s" % (node,nrwalks)
    
    return nrwalks


def run():
    g = DoubleDictGraph()
    list = topsort(g)
    print list
    list1 = finAllWalks(g)
    print list1
    list3 = findNrWalks(g)
    print list3
run()

		int[] topological_nodes = this.searchDFS()[2].reverse;

		// int => (predecesor, cost)
		VertexTuple[int] node_tuples;

		// all the tuples must be set to a default value for every node in the graph
		foreach(int node; topological_nodes)
		{
			node_tuples[node] = tuple(-1, 0);
		}

		printDebug(format("Node_tuple = %s\n", to!string(node_tuples)));

		// run trough all the nodes in a topological order
		foreach(int node; topological_nodes)
		{
			VertexTuple[] predecessors;
			int max_pre;

			// we must check all the predecessors
			foreach(int pre; this.getInConnections(node))
			{
				max_pre = node_tuples[pre][1]; // cost
				predecessors ~= tuple(pre, this.duration[pre]);
			}
			printDebug(format("Node = %d, predecessors = %s\n", node, to!string(predecessors)));

			int max = 0;
			VertexTuple max_tuple;
			// look for the most costly predecessor
			foreach(VertexTuple i; predecessors)
			{
				if(i[1] >= max)
				{
					max = i[1];
					max_tuple = i;
				}
			}

			// assign the maximum value to the given node in the node_tuples dictionary
			node_tuples[node] = max_tuple;
		}

		// find the critical node
		int max = 0, critical_node = -1;
		foreach(key, value; node_tuples)
		{
			if(value[1] >= max)
			{
				max = value[1];
				critical_node = key;
			}
		}

		printDebug(format("Critical node = %d", critical_node));

		printDebug(format("Node_tuple = %s\n", to!string(node_tuples)));
