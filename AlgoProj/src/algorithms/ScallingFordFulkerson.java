package algorithms;

import java.util.*;

import graph.SimpleGraph;
import model.*;

public class ScallingFordFulkerson implements FlowAlgorithm {

	@Override
	public double findMaxFlow(SimpleGraph simpleGraph) throws Exception {
		FlowGraph graph = new FlowGraph(simpleGraph);
		FlowVertex source = graph.getSource();
		
		double sourceOutgoingCapacity = source.getOutgoingCapacity();
		
		// Get starting min residual capacity
		double minResidualCapacity = 1;
		while (minResidualCapacity * 2 < sourceOutgoingCapacity) {
			minResidualCapacity *= 2;
		}
		
		while (minResidualCapacity >= 1) {
			this.findMaxFlowWithMinResidualCapacity(graph, minResidualCapacity);
			minResidualCapacity = minResidualCapacity / 2;
		}
		
		return source.getOutgoingFlow();
	}
	
	private void findMaxFlowWithMinResidualCapacity(FlowGraph graph, double minResidualCapacity) throws Exception {
		while (true) {
			LinkedList<FlowEdge> path = this.getPathToSinkWithMinResidualCapacity(graph, graph.getSource(), minResidualCapacity);
			graph.resetVisited();
			if (path == null) {
				// No s-t path in residual graph
				break;
			}
			
			double bottleneck = this.getBottleneck(path);
			for (FlowEdge edge : path) {
				edge.increaseFlow(bottleneck);
			}
		}
	}
	
	private double getBottleneck(LinkedList<FlowEdge> path) {
		double bottleneck = Double.MAX_VALUE;
		for (FlowEdge edge : path) {
			if (edge.getResidualCapacity() < bottleneck) {
				bottleneck = edge.getResidualCapacity();
			}
		}
		
		return bottleneck;
	}
	
	private LinkedList<FlowEdge> getPathToSinkWithMinResidualCapacity(
														FlowGraph graph,
														FlowVertex origin,
														double minResidualCapacity) {
		// Mark origin as visited
		origin.markVisited();
		
		for (FlowEdge edge : origin.getEdges()) {
			if (edge.getResidualCapacity() >= minResidualCapacity) {
				if (edge.getDest().getName().equals(graph.getSink().getName())) {
					// Reached sink using this edge, return path as just this edge
					LinkedList<FlowEdge> path = new LinkedList<FlowEdge>();
					path.add(edge);
					return path;
				} else if (!edge.getDest().isVisited()) {
					LinkedList<FlowEdge> path = this.getPathToSinkWithMinResidualCapacity(graph, edge.getDest(), minResidualCapacity);
					if (path != null) {
						// Dest of edge reached the sink, add this edge and return
						path.addFirst(edge);
						return path;
					}
				}
			}
		}
		
		// No path found
		return null;
	}
}
