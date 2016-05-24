import graph

class PriorityQueue:
	def __init__(self):
		self.__values = {}
		
	def isEmpty(self):
		return len(self.__values) == 0

	def pop(self):
		topPriority = None
		topObject = None
		for obj in self.__values:
			objPriority = self.__values[obj]
			if topPriority is None or topPriority > objPriority:
				topPriority = objPriority
				topObject = obj
		del self.__values[topObject]
		return topObject

	def add(self, obj, priority):
		self.__values[obj] = priority

	def contains(self, val):
		return val in self.__values

def getChildren(x, prev):
	list = []
	for i in prev:
		if prev[i] == x:
			list.append(i)
            
	return list
			
def printDijkstraTree(s, q, d, prev, indent):
	if q.contains(s):
		star = ''
	else:
		star = '*'
        
	print "%s%s [%s]%s" % (indent, s, d[s], star)
	for x in getChildren(s,prev):
		printDijkstraTree(x, q, d, prev, indent + '    ')
				
def printDijkstraStep(s, x, q, d, prev):
	print '----'
	if x is not None:
		print 'x=%s [%s]' % (x, d[x])
        
	printDijkstraTree(s, q, d, prev, '')

def dijkstra(g, s):
	prev = {}
	q = PriorityQueue()
	q.add(s, 0)
	d = {}
	d[s] = 0
	visited = set()
	visited.add(s)
	printDijkstraStep(s, None, q, d, prev)
	while not q.isEmpty():
		x = q.pop()
		for y in g.parseNout(x):
			if y not in visited or d[y] > d[x] + g.cost(x, y):
				d[y] = d[x] + g.cost(x, y)
				visited.add(y)
				q.add(y, d[y])
				prev[y] = x
        
		printDijkstraStep(s, x, q, d, prev)
				
				
	return (d, prev)
				
def printTree(tree, root):
	printTreeAux(tree, root, "")
	
def printTreeAux(tree,root, indent):
	print indent + str(root)
    
	children = tree[root]
	newindent = indent+"    "
	for i in children:
		printTreeAux(tree, i, newindent)
					

def getPath(s, t, prev):
    # build path
	list = []
	while t != s:
		list.append(t)
		t = prev[t]
        
    # reverse
	ret = [s]
	for i in range(len(list)):
		ret.append(list[len(list) - i - 1])
		
	return ret

def bellman(g, s):
	weight = [{s : 0}]
	path = [{s : (0,)}]
	print "k = %s" % 0
	print "weight = %s" % weight[0]
	print "path = %s" % path[0]
	for k in range(1, len(g.parseX())):
		weight.append({})
		path.append({})
        
		for y in weight[k - 1]:
			for x in g.parseNout(y):
				if (x not in weight[k]) or (weight[k][x] > weight[k - 1][y] + g.cost(x, y)):
					weight[k][x] = weight[k - 1][y] + g.cost(y, x)
					path[k][x] = path[k - 1][y] + (x,)
                    
		print "k = %s" % k
		print "weight = %s" % weight[k]
		print "path = %s" % path[k]
    
	return weight, path
#g = graph.initSecondGraph(graph.DoubleDictGraph)
#s = 0
#t = 4

#dijkstra(g, s)
#d, prev = dijkstra(g, s)
#print d
#print prev

g = graph.initMyGraph(graph.MatrGraph)
print bellman(g, 1);

