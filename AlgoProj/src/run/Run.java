package run;

import java.awt.Color;
import java.io.*;
import java.util.*;
import algorithms.*;
import graph.*;
import graph.generation.*;

public class Run {

	public static void main(String[] args) throws Exception {
		// rows columns max-capacity filename
		LinkedList<String[]> generatorArgs = new LinkedList<String[]>();
		generatorArgs.add(new String[] { "5", "10", "100", "Mesh1.txt" });
		generatorArgs.add(new String[] { "8", "20", "100", "Mesh2.txt" });
		
		int numRuns = 5;
		
		String separator = "\t";
		String header = "Rows" + separator
							+ "Columns" + separator
							+ "Max-Capacity" + separator
							+ "Vertices" + separator
							+ "Edges" + separator
							+ "Max flow" + separator
							+ "Preflow Time" + separator
							+ "Scaling Ford Fulkerson Time" + separator
							+ "Ford Fulkerson Time";
		System.out.println(header);

		for (String[] generatorArg : generatorArgs) {
			String fileName = generatorArg[3];
			File graphFile = new File(fileName);
			if (!graphFile.exists()) {
				System.out.println("Creating graph " + fileName);
				new MeshGenerator(generatorArg).generate();
			}
			
			
			SimpleGraph graph = new SimpleGraph();

			PrintStream original = System.out;
			System.setOut(new PrintStream(new OutputStream() {
				@Override
				public void write(int arg0) {
					// Do nothing
				}
			}));
			GraphInput.LoadSimpleGraph(graph, fileName);
			System.setOut(original);
			
			String rows = generatorArg[0];
			String columns = generatorArg[1];
			String maxCapacity = generatorArg[2];
			int vertices = graph.numVertices();
			int edges = graph.numEdges();
			
			double maxFlow = Double.NaN;
			
			FlowAlgorithm[] algorithms = new FlowAlgorithm[] { new PreFlowPush(), new ScallingFordFulkerson(), new FordFulkerson() };
			LinkedList<Long> durations = new LinkedList<Long>();
			
			for (FlowAlgorithm algorithm : algorithms) {
				
				long startTime = System.currentTimeMillis();
				for (int i = 0; i < numRuns; i++) {
					double result = algorithm.findMaxFlow(graph);
					
					if (Double.isNaN(maxFlow)) {
						maxFlow = result;
					} else if (maxFlow != result) {
						throw new Exception("Diff result " + result + " from " + maxFlow + " in " + algorithm.getClass().getSimpleName());
					}
				}
				
				long endTime = System.currentTimeMillis();
				long duration = (endTime - startTime) / numRuns;
				durations.addLast(duration);
			}
			
			String line = rows + separator
							+ columns + separator
							+ maxCapacity + separator
							+ vertices + separator
							+ edges + separator
							+ maxFlow + separator
							+ durations.get(0) + separator
							+ durations.get(1) + separator
							+ durations.get(2);
			System.out.println(line);
		}
	}
}
