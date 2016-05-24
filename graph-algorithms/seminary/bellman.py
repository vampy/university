import graph


def computeIsWalk(graph, start, maxLen):
    initSet = set()
    initSet.add(start)
    isWalk = [initSet]

    for k in range(1, maxLen + 1):
        prevSet = isWalk[k - 1]
        currSet = set()
        for y in prevSet:
            for x in graph.parseNout(y):
                currSet.add(x)

        isWalk.append(currSet)

    return isWalk


def getExistingWalk(graph, isWalk, start, target, length):
    walk = []
    currVertex = target
    currLength = length
    while currLength > 0:
        walk.insert(0, currVertex)
        for prevVertex in graph.parseNin(currVertex):
            if prevVertex in isWalk[currLength - 1]:
                currVertex = prevVertex
                break

        currLength = currLength - 1

    walk.insert(0, start)
    return walk


def loverProblemSolution(graph, start1, start2):
    nrVertices = len(graph.parseX())
    maxLen = nrVertices * (nrVertices - 1)
    isWalk1 = computeIsWalk(graph, start1, maxLen)
    isWalk2 = computeIsWalk(graph, start2, maxLen)

    for i in range(maxLen):
        for x in isWalk1[i]:
            if x in isWalk2[i]:
                return (i, getExistingWalk(graph, isWalk1, start1, x, i),
                        getExistingWalk(graph, isWalk2, start2, x, i))

    return None


def bellman(graph, start, maxLen):
    initMap = {start: 0}
    dist = [initMap]

    for k in range(1, maxLen + 1):
        prevMap = dist[k - 1]
        currMap = {}
        for y in prevMap:
            for x in graph.parseNout(y):
                if x not in currMap or currMap[x] > prevMap[y] + graph.cost(y, x):
                    currMap[x] = prevMap[y] + graph.cost(y, x)
        dist.append(currMap)

    return dist


def getMinCostWalk(graph, dist, start, target, length):
    walk = []
    currVertex = target
    currLength = length
    while currLength > 0:
        walk.insert(0, currVertex)
        for prevVertex in graph.parseNin(currVertex):
            if prevVertex in dist[currLength - 1] and \
                    (dist[currLength - 1][prevVertex] + graph.cost(prevVertex, currVertex) == dist[currLength][
                        currVertex]):
                currVertex = prevVertex
                break

        currLength = currLength - 1

    walk.insert(0, start)
    return walk


def run():
    g = graph.initLoverProblemGraph(graph.DoubleDictGraph)
    print(loverProblemSolution(g, 0, 2))


def run1():
    g = graph.initSecondGraph(graph.DoubleDictGraph)
    s = 0
    t = 5
    maxLen = 3 * len(g.parseX())

    # get dist
    dist = bellman(g, s, maxLen)

    for i in range(maxLen + 1):
        if t in dist[i]:
            print(getMinCostWalk(g, dist, s, t, i), dist[i][t])


g = graph.initMyGraph(graph.MatrGraph)
print(bellman(g, 0, len(g.parseX())))
