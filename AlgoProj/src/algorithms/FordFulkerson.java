package algorithms;
import java.util.LinkedList;
import graph.SimpleGraph;
import model.*;

/**
 * For a SimpleGraph, Calculate its MaxFlow path
 * Public Member - findMaxFlow(SimpleGraph smp)
 * Exception class - Exception
 */
public class FordFulkerson implements FlowAlgorithm {

	@Override
	public double findMaxFlow(SimpleGraph simpleGraph) throws Exception {
		FlowGraph graph = new FlowGraph(simpleGraph);
		FlowVertex source = graph.getSource();
		
		while (true) {
			LinkedList<FlowEdge> path = this.getPathToSink(graph, source);
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
		
		return source.getOutgoingFlow();
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
	
	private LinkedList<FlowEdge> getPathToSink(FlowGraph graph, FlowVertex origin) {
		// Mark origin as visited
		origin.markVisited();
		
		for (FlowEdge edge : origin.getEdges()) {
			if (edge.getResidualCapacity() > 0) {
				if (edge.getDest().getName().equals(graph.getSink().getName())) {
					// Reached sink using this edge, return path as just this edge
					LinkedList<FlowEdge> path = new LinkedList<FlowEdge>();
					path.add(edge);
					return path;
				} else if (!edge.getDest().isVisited()) {
					LinkedList<FlowEdge> path = this.getPathToSink(graph, edge.getDest());
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
