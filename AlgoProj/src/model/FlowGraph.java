package model;

import java.util.*;
import graph.*;

public class FlowGraph {
	private Hashtable<String, FlowVertex> vertices;
	private Hashtable<String, FlowEdge> edges;
	
	public FlowGraph(SimpleGraph graph) throws Exception {
		this.vertices = new Hashtable<String, FlowVertex>();
		this.edges = new Hashtable<String, FlowEdge>();
		
		Iterator vertexIterator = graph.vertices();
		while (vertexIterator.hasNext()) {
			Vertex vertex = (Vertex)vertexIterator.next();
			FlowVertex flowVertex = new FlowVertex((String)vertex.getName());
			this.addVertex(flowVertex);
		}
		
		Iterator edgeIterator = graph.edges();
		while (edgeIterator.hasNext()) {
			Edge edge = (Edge)edgeIterator.next();
			
			FlowVertex origin = this.vertices.get(edge.getFirstEndpoint().getName());
			FlowVertex dest = this.vertices.get(edge.getSecondEndpoint().getName());
			double capacity = (double)edge.getData();
			
			this.addEdge(origin, dest, capacity);
		}
	}
	
	public Collection<FlowVertex> getVertices() {
		return this.vertices.values();
	}
	
	public void addVertex(FlowVertex vertex) {
		this.vertices.put(vertex.getName(), vertex);
	}
	
	public void addEdge(FlowVertex origin, FlowVertex dest, double capacity) throws Exception {
		FlowEdge edge = new FlowEdge(origin, dest, capacity);
		origin.addEdge(edge);
		this.edges.put(edge.getName(), edge);
	}
	
	public FlowVertex getSource() {
		return this.getVertex("s");
	}
	
	public FlowVertex getSink() {
		return this.getVertex("t");
	}
	
	public FlowVertex getVertex(String name) {
		return this.vertices.get(name);
	}
	
	public void resetVisited() {
		for (FlowVertex vertex : this.vertices.values()) {
			vertex.resetVisited();
		}
	}
	
	public int numberOfVertices() {
		return this.vertices.size();
	}
	
	public int numberOfEdges() {
		return this.edges.size();
	}
}
