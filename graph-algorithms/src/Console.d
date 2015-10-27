module Console;

import std.stdio;
import std.conv;
import std.string;
import std.typecons;

import Graph;
import Util;

class Console
{
public:
	this(int graph, string headerMessage="")
	{
		this.headerMessage = headerMessage;
		this.graph = graph;
	}
	
	string getMenu()
	{
		string menu = "\t1. Get number of vertices\n";
		menu ~= "\t2. Check edge\n";
		menu ~= "\t3. In/Out Degree\n";
		menu ~= "\t4. Get In vertices\n";
		menu ~= "\t5. Get Out vertices\n";
		menu ~= "\t6. Modify edge cost\n";
		menu ~= "\t7. Get edge cost\n";
		menu ~= "\n";
		menu ~= "\t10. Find a lowest length path\n";
		menu ~= "\n";
		menu ~= "\t11. Dijkstra lowest cost walk\n";
		menu ~= "\n";
		menu ~= "\t99. Print this help menu\n";
		menu ~= "\t0. Quit\n";
		return menu;
	}
	
	bool readInt(out int command, string message="")
	{
		try
		{
			stdout.write(message);
			command = to!int(stdin.readln().strip());
		}
		catch(ConvException exc)
		{
			stdout.writeln("Input is not a number, please try again");
			return false;
		}
		
		return true;
	}

	void run()
	{
		int command;
		stdout.writeln(headerMessage);
		stdout.writeln(this.getMenu);


		printDebug(to!string(this.graph));
		//printDebug(to!string(this.graph.lowestLengthPath(0, 4)));
		//Tuple!(int, int[]) temp_tuple = graph.dijkstra(0, 0);
		while(true)
		{
			if(!this.readInt(command, ">>> "))
			{
				continue;
			}
			
			if(command == 0)
			{
				stdout.writeln("Quiting");
				break;
			}
			else if(command == 99)
			{
				stdout.writeln(this.getMenu);
			}
			else if(command == 1) // Get number of vertices
			{
				stdout.writeln("Number of vertices: ", this.graph.getNumVertices());
			}
			else if(command == 2) // Check edge
			{
				int source, target;
				
				// validate
				if(!this.readInt(source, "Source id: ") || !this.readInt(target, "Target id: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(source))
				{
					stderr.writefln("The vertex with id = %d does not exist", source);
					continue;
				}
				if(!this.graph.vertexExists(target))
				{
					stderr.writefln("The vertex with id = %d does not exist", target);
					continue;
				}
				
				
				// view
				if(this.graph.isEdge(source, target))
				{
					stdout.writeln("Edge exists");
				}
				else
				{
					stdout.writeln("Edget does NOT exist");
				}
			}
			else if(command == 3) // In/Out Degree
			{
				int id;
				
				// validate
				if(!this.readInt(id, "Vertex id: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(id))
				{
					stderr.writefln("The vertex with id = %d does not exist", id);
					continue;
				}
				
				// view
				stdout.writefln("In degree: %d, Out degree: %d", 
				                this.graph.getInDegree(id), 
				                this.graph.getOutDegree(id));
			}
			else if(command == 4) // Get In vertices
			{
				int id;
				
				// validate
				if(!this.readInt(id, "Vertex id: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(id))
				{
					stderr.writefln("The vertex with id = %d does not exist", id);
					continue;
				}
				stdout.writeln(this.graph.getInConnections(id));
			}
			else if(command == 5) // Get Out vertices
			{
				int id;
				
				// validate
				if(!this.readInt(id, "Vertex id: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(id))
				{
					stderr.writefln("The vertex with id = %d does not exist", id);
					continue;
				}
				stdout.writeln(this.graph.getOutConnections(id));
			}
			else if(command == 6) // Modify edge cost
			{
				int source, target, readCost;
				
				// validate
				if(!this.readInt(source, "Source id: ") 
				   || !this.readInt(target, "Target id: ") 
				   || !this.readInt(readCost, "New cost: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(source))
				{
					stderr.writefln("The vertex with id = %d does not exist", source);
					continue;
				}
				if(!this.graph.vertexExists(target))
				{
					stderr.writefln("The vertex with id = %d does not exist", target);
					continue;
				}
				if(!this.graph.isEdge(source, target))
				{
					stderr.writefln("There is no edge between %d and %d", source, target);
					continue;
				}
				
				this.graph.setEdgeCost(source, target, readCost);
				stdout.writeln("Cost updated");
			}
			else if(command == 7) // Get edge cost
			{
				int source, target;
				
				// validate
				if(!this.readInt(source, "Source id: ") || !this.readInt(target, "Target id: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(source))
				{
					stderr.writefln("The vertex with id = %d does not exist", source);
					continue;
				}
				if(!this.graph.vertexExists(target))
				{
					stderr.writefln("The vertex with id = %d does not exist", target);
					continue;
				}
				if(!this.graph.isEdge(source, target))
				{
					stderr.writefln("There is no edge between %d and %d", source, target);
					continue;
				}
				
				stdout.writefln("The cost from %d to %d is: %d", 
				                source, 
				                target, 
				                this.graph.getEdgeCost(source, target));
			}
			else if(command == 10) // lowest length path
			{
				int source, target;

				// validate
				if(!this.readInt(source, "Path root: ") || !this.readInt(target, "Path destination: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(source))
				{
					stderr.writefln("The vertex with id = %d does not exist", source);
					continue;
				}
				if(!this.graph.vertexExists(target))
				{
					stderr.writefln("The vertex with id = %d does not exist", target);
					continue;
				}
				int[] path = this.graph.lowestLengthPath(source, target);

				if(path.length)
				{
					ulong print_length =  path.length - 1;
					// exception  case
					if(path.length == 1)
					{
						print_length = 1;
					}

					stdout.writefln("Found path with length %d", print_length);
					stdout.writefln("Traversed nodes: %s", to!string(path));
				}
				else
				{
					stdout.writeln("There is no path");
				}
			}
			else if(command == 11) // lowest cost path Djkstra
			{
				int source, target;
				
				// validate
				if(!this.readInt(source, "Path root: ") || !this.readInt(target, "Path destination: "))
				{
					continue;
				}
				if(!this.graph.vertexExists(source))
				{
					stderr.writefln("The vertex with id = %d does not exist", source);
					continue;
				}
				if(!this.graph.vertexExists(target))
				{
					stderr.writefln("The vertex with id = %d does not exist", target);
					continue;
				}

				Tuple!(int, int[]) temp_tuple = this.graph.lowestCostDijkstra(source, target);

				printDebug(to!string(temp_tuple[0]));
				printDebug(to!string(temp_tuple[1]));
				if(temp_tuple[0] == this.graph.maxDistance)
				{
					stdout.writefln("There is no minimum distance(ther vertex %d is isolated or there is no path from %d to %d)", 
					                target, source, target);
					continue;
				}

				if(temp_tuple[1].length)
				{
					stdout.writefln("Found path with minimum distance %d", temp_tuple[0]);
					stdout.writefln("Traversed nodes: %s", to!string(temp_tuple[1]));
				}
				else
				{
					stdout.writeln("There is no path");
				}
			}
			else 
			{
				stdout.writeln("Command does not exist, please try again");
			}
		} 
	}
	
protected:
	string headerMessage;
	int graph; 

}
