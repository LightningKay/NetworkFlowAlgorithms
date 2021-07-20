package algorithms;
import java.util.*;
import java.io.*;

import graph.*;
import model.*;

public class PreFlowPush implements FlowAlgorithm {
	
	@Override
	public double findMaxFlow(SimpleGraph simpleGraph) throws Exception {
		FlowGraph graph = new FlowGraph(simpleGraph);
		FlowVertex source = graph.getSource();
		FlowVertex sink = graph.getSink();
				
		LinkedList<FlowVertex> positiveExcessVertices = new LinkedList<FlowVertex>();
		HashSet<String> verticesInQueue = new HashSet<String>();
		
		// Initial conditions
		source.setHeight(graph.numberOfVertices());
		for (FlowEdge edge : source.getEdges()) {
			edge.increaseFlow(edge.getResidualCapacity());
			this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, edge.getDest());
		}

		while (!positiveExcessVertices.isEmpty()) {
			FlowVertex vertex = this.remove(positiveExcessVertices, verticesInQueue);
			FlowEdge edge = vertex.getLessHeightNeighborEdge();
			if (edge == null) {
				// No neighbor with less height, relabel
				vertex.incrementHeight();
				
				// Add vertex back
				this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, vertex);
			} else {
				double increment = Math.min(edge.getResidualCapacity(), vertex.getExcess());
				edge.increaseFlow(increment);

				// Add origin if there is still excess
				if (edge.getOrigin().getExcess() > 0) {
					this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, edge.getOrigin());
				}

				// Add dest if excess > 0
				if (edge.getDest().getExcess() > 0) {
					this.addVertexIfNotPresent(positiveExcessVertices, verticesInQueue, edge.getDest());
				}
			}
		}
		
		return source.getOutgoingFlow();
	}
	
	private void addVertexIfNotPresent(
							LinkedList<FlowVertex> positiveExcessVertices,
							HashSet<String> verticesInQueue,
							FlowVertex vertex) {

		if (vertex.isSourceOrSink()) {
			return;
		}
		
		if (verticesInQueue.add(vertex.getName())) {
			positiveExcessVertices.addLast(vertex);
		}
	}
	
	private FlowVertex remove(LinkedList<FlowVertex> positiveExcessVertices, HashSet<String> verticesInQueue) {
		FlowVertex vertex = positiveExcessVertices.remove();
		verticesInQueue.remove(vertex.getName());
		return vertex;
	}
	

	

}
