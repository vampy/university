module Vertex;

import std.conv;

class VertexEl(T)
{
public:
	this(T id)
	{
		this.id = id;
	}
	
	void setId(T id)
	{
		this.id = id;
	}
	
	T getId()
	{
		return this.id;
	}
	
	int getCost(T target)
	{
		if(target !in this.costs)
		{
			throw new Exception("Target not in list");
		}
		
		return this.costs[target];
	}
	
	void setCost(T target, int cost)
	{
		this.costs[target] = cost;
	}
	
	override string toString()
	{
		return to!string(this.costs);
	}
	
protected:
	T id;
	
	// target => cost
	int[T] costs;
}
