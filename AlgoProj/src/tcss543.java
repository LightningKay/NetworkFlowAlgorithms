import algorithms.*;
import graph.*;

public class tcss543 {

	public static void main(String[] args) throws Exception {
		SimpleGraph graph = new SimpleGraph();
		GraphInput.LoadSimpleGraph(graph, args[0]);
		
		
		System.out.println("Vertices: " + graph.numVertices());
		System.out.println("Edges: " + graph.numEdges());
		
		System.out.println("Preflow Max flow: " + new PreFlowPush().findMaxFlow(graph));
		System.out.println("SFF Max flow: " + new ScallingFordFulkerson().findMaxFlow(graph));
		System.out.println("FF Max flow: " + new FordFulkerson().findMaxFlow(graph));
	}

}
