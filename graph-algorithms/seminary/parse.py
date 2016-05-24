import graph

def dfsIter(g, s, prev, tree):
	tree[s]=[]
	q = [s]
	visited = set()
	visited.add(s)
	while len(q) > 0:
		x = q[-1]
		q = q[: -1 ]
#		print x
		for y in g.parseNout(x):
			if y not in visited:
				visited.add(y)
				q.append(y)
				prev[y] = x
				tree[x].append(y)
				tree[y]=[]
				
def bfs(g, s, prev, tree):
	tree[s]=[]
	q = [s]
	visited = set()
	visited.add(s)
	while len(q) > 0:
		x = q[0]
		q = q[1 : ]
#		print x
		for y in g.parseNout(x):
			if y not in visited:
				visited.add(y)
				q.append(y)
				prev[y] = x
				tree[x].append(y)
				tree[y]=[]
				
			
def printTree(tree, root):
	printTreeAux(tree, root, "")
	
	
def printTreeAux(tree, root, indent):
	print indent + str(root)
	children = tree[root]
	newindent = indent+"    "
	for i in children:
		printTreeAux(tree, i, newindent)
					
def dfsAux(g, x, visited, prev, tree):
#	print x
	tree[x] = []
	for y in g.parseNout(x):
		if y not in visited:
			prev[y] = x
			tree[x].append(y)
			visited.add(y)
			dfsAux(g, y, visited, prev,tree)
				
def dfs(g, s, prev,tree):
	visited = set()
	visited.add(s)
	dfsAux(g, s, visited, prev, tree)

def getPath(s, t, prev):
	list = []
	while t != s:
		list.append(t)
		t = prev[t]
		
	ret = [s]
	for i in range(len(list)):
		ret.append(list[len(list) - i - 1])
		
	return ret

g = graph.initSecondGraph(graph.DoubleDictGraph)
s = 0
t = 4

#g = graph.GoatGraph()
#s = graph.GoatStatus(0) 
#t = graph.GoatStatus(15)

prev = {}
tree = {}

#bfs(g, s, prev , tree)
dfsIter(g, s, prev, tree)
printTree(tree, s)

#sol = getPath(s, t, prev)
#for v in sol:
#	print v


#g = graph.initRandomGraph(DoubleDictGraph, 100000, 300000)
#g = graph.GoatGraph()
#for x in g.parseX():
#	print ("%s:" % x)
#	for y in g.parseNout(x):
#		print ("%s -> %s; cost=%s" % (x, y, g.cost(x,y)))

