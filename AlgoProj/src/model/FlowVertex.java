package model;

import java.util.*;

public class FlowVertex {
	// name of vertex
	private String name;
	
	// table from edge name of edge object
	Hashtable<String, FlowEdge> edges;
	
	// excess flow, useful for preflow algorithm. For Ford Fulkerson type algorithm, excess = 0
	private double excess;
	
	// height, used in preflow algorithm
	private int height;
	
	private boolean visited;
	
	public FlowVertex(String name) {
		this.name = name;
		this.edges = new Hashtable<String, FlowEdge>();
	}
		
	public void addEdge(FlowEdge edge) throws Exception {
		if (edge.getOrigin().getName() != this.name) {
			// Adding edge to this vertex but in the edge origin is specified as some other vertex
			throw new Exception("Adding edge " + edge.getName() + " with origin vertex " + edge.getOrigin().getName() + " on " + this.name);
		}
		
		this.edges.put(edge.getName(), edge);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void removeEdge(FlowEdge edge) {
		this.edges.remove(edge.getName());
	}
	
	public boolean isVisited() {
		return this.visited;
	}
	
	public void markVisited() {
		this.visited = true;
	}
	
	public void resetVisited() {
		this.visited = false;
	}
	
	
	public double getExcess() {
		return this.excess;
	}
	
	public void increaseExcess(double increment) throws Exception {
		this.excess += increment;
//		if (this.excess < 0) {
//			throw new Exception("Excess " + this.excess + " less than 0 for vertex " + this.name);
//		}
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void incrementHeight() {
		this.height += 1;
	}
	
	public FlowEdge getLessHeightNeighborEdge() {		
		for (FlowEdge edge : this.edges.values()) {
			if (edge.getResidualCapacity() > 0 && edge.getDest().height < this.height) {
				return edge;
			}
		}
		
		return null;
	}
	
	public double getOutgoingFlow() {
		double flow = 0;
		for (FlowEdge edge : this.edges.values()) {
			if (!edge.isBackwardEdge()) {
				flow += edge.getFlow();
			}
		}
		
		return flow;
	}
	
	public double getOutgoingCapacity() {
		double capacity = 0;
		for (FlowEdge edge : this.edges.values()) {
			if (!edge.isBackwardEdge()) {
				capacity += edge.getCapacity();
			}
		}
		
		return capacity;
	}
	
	public FlowEdge[] getEdges() {
		FlowEdge[] edgeArray = new FlowEdge[this.edges.size()];
		this.edges.values().toArray(edgeArray);
		return edgeArray;
	}
	
	public boolean isSourceOrSink() {
		return this.name.equals("s") || this.name.equals("t");
	}
}
