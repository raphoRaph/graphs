import java.util.Arrays;
import java.util.List;

import m1graphs2025.Graph;
import m1graphs2025.Node;
import m1graphs2025.UndirectedGraph;
import m1maxflow2025.FlowNetwork;
import m1maxflow2025.FordFulkerson;
import m1maxflow2025.PathFinder;

/**
 * Demonstration class for the m1graphs2025 API.
 *
 * Shows how to build, explore, and export/import graphs.
 * This serves as a quick test of the Graph, Node, and Edge classes.
 *
 * Authors: Name1, Name2
 * Date: November 2025
 */
public class Main {
	public static void mainGraph(String[] args) {
		System.out.println("\n==================================================");
		System.out.println(" Creating DIRECTED graph demonstration");
		System.out.println("==================================================");

		// Create simple directed graph
		System.out.println("Creating graph from successor array: (2,3,0,3,4,0)");
		Graph g = new Graph(2, 3, 0, 3, 4, 0);
		System.out.println("Graph created:\n" + g);

		System.out.println("\nNumber of nodes (4): " + g.nbNodes());
		System.out.println("Number of edges (4): " + g.nbEdges());

		// Add nodes
		System.out.println("\n▶ Adding extra nodes and edges...");
		g.addEdge(4, 5); // Not mandator to add node before add edge (5)
		g.addEdge(5, 2);
		g.addEdge(5, 5); // Self loop
		System.out.println("Updated graph:\n" + g);

		// Properties
		System.out.println("\nGraph properties:");
		System.out.println("- Contains self-loops (true)? " + g.hasSelfLoops());
		System.out.println("- Is multigraph (false)? " + g.isMultiGraph());
		System.out.println("- Node 3 out-degree (0): " + g.outDegree(3));
		System.out.println("- Node 2 in-degree (2): " + g.inDegree(2));
		System.out.println("- Are 3 and 4 adjacent (false)? " + g.adjacent(3, 4));

		// Traversal
		System.out.print("\nDepth-First Search (DFS) [1, 2, 3, 4, 5]: ");
		List<Node> dfsOrder = g.getDFS();
		for (Node n : dfsOrder)
			System.out.print(n + " ");
		System.out.println();

		System.out.print("\nBreadth-First Search (BFS) [1, 2, 3, 4, 5]: ");
		List<Node> bfsOrder = g.getBFS();
		for (Node n : bfsOrder)
			System.out.print(n + " ");
		System.out.println();

		// Export
		System.out.println("\nExporting graph to DOT file: 'demoGraph.gv'");
		g.toDotFile("demoGraph");
		System.out.println("DOT export done. Check file 'demoGraph.gv'.");

		// Import
		System.out.println("\nImporting graph from 'demoGraph.gv'");
		Graph imported = Graph.fromDotFile("demoGraph");
		if (imported != null) {
			System.out.println("Graph imported successfully:\n" + imported);
			System.out.println("Imported graph edges (7): " + imported.nbEdges());
		} else {
			System.out.println("Failed to import the DOT file.");
		}

		// Transitive closure
		System.out.println("\nGenerating derived graphs:");
		Graph reversed = g.getReverse();
		Graph closure = g.getTransitiveClosure();
		System.out.println("Reversed graph:\n" + reversed);
		System.out.println("Transitive closure:\n" + closure);

		System.out.println("\n==================================================");
		System.out.println(" Creating UNDIRECTED graph demonstration");
		System.out.println("==================================================");

		UndirectedGraph ug = new UndirectedGraph();
		// Add edges
		System.out.println("\nAdding nodes and undirected edges...");
		ug.addEdge(1, 2);
		ug.addEdge(2, 3);
		ug.addEdge(3, 1);

		System.out.println("Undirected graph created:\n" + ug);

		// Properties
		System.out.println("\nGraph properties:");
		System.out.println("- Number of nodes (3): " + ug.nbNodes());
		System.out.println("- Number of edges (3): " + ug.nbEdges());
		System.out.println("- Degree of node 1 (2): " + ug.degree(1));
		System.out.println("- Degree of node 2 (2): " + ug.degree(2));
		System.out.println("- Edge (1,2) exists (true)? " + ug.existsEdge(1, 2));
		System.out.println("- Edge (2,1) exists (true)? " + ug.existsEdge(2, 1));

		// Matrix symmetric
		System.out.println("\nAdjacency matrix (symmetric):");
		int[][] matrix = ug.toAdjMatrix();
		for (int i = 0; i < matrix.length; i++) {
			System.out.println(Arrays.toString(matrix[i]));
		}

		// Export
		System.out.println("\nExporting undirected graph to DOT file: 'undirectedDemo.gv'");
		ug.toDotFile("undirectedDemo");
		System.out.println("DOT export done. Check file 'undirectedDemo.gv'.");

		// Import
		System.out.println("\nImporting undirected graph from 'undirectedDemo.gv'");
		UndirectedGraph ugImported = UndirectedGraph.fromDotFile("undirectedDemo");
		if (ugImported != null) {
			System.out.println("Graph imported successfully:\n" + ugImported);
		} else {
			System.out.println("Failed to import undirected DOT file.");
		}

		// Transitive closure
		System.out.println("\nTransitive closure of undirected graph:");
		UndirectedGraph closureUnd = ug.getTransitiveClosure();
		System.out.println(closureUnd);
	}

	public static void main(String[] args) {
		importDot();
		fn1();
		fn2();
		allPathFinder();
	}

	private static void importDot() {
		System.out.println("\nImporting flownetwork from 'flowNetwork.gv'");
		FlowNetwork imported = FlowNetwork.fromDotFile("flowNetwork");
		if (imported != null) {
			System.out.println("flownetwork imported successfully:\n" + imported);
			System.out.println("Imported flownetwork edges (8): " + imported.nbEdges());
			System.out.println(imported.toDotString());
			System.out.println(FordFulkerson.maxFlow(imported, PathFinder.bfsPathFinder()).toDotString());
		} else {
			System.out.println("Failed to import the DOT file.");
		}
	}

	private static void fn1() {
		FlowNetwork fnBFS = createFn();
		FlowNetwork resultBFS = FordFulkerson.maxFlow(fnBFS, PathFinder.bfsPathFinder());
		System.out.println(resultBFS.toDotString());
	}

	private static void fn2() {
		FlowNetwork fnBFS2 = createFn2();
		System.out.println(fnBFS2.toDotString());
	}

	private static void allPathFinder() {

		FlowNetwork fnBFS = createFn();
		FlowNetwork fnDFS = createFn();
		FlowNetwork fnDIJ = createFn();
		FlowNetwork fnDIJMax = createFn();

		FlowNetwork resultBFS = FordFulkerson.maxFlow(fnBFS, PathFinder.bfsPathFinder());
		FlowNetwork resultDFS = FordFulkerson.maxFlow(fnDFS, PathFinder.dfsPathFinder());
		FlowNetwork resultDIJ = FordFulkerson.maxFlow(fnDIJ, PathFinder.dijkstraPathFinder());
		FlowNetwork resultDIJMax = FordFulkerson.maxFlow(fnDIJMax, PathFinder.dijkstraMaxPathFinder());

		System.out.println("Max flow network BFS:\n" + resultBFS.toDotString());
		System.out.println("Max flow network DFS:\n" + resultDFS.toDotString());
		System.out.println("Max flow network DIJ:\n" + resultDIJ.toDotString());
		System.out.println("Max flow network DIJMax:\n" + resultDIJMax.toDotString());
	}

	private static FlowNetwork createFn() {
		FlowNetwork fn = new FlowNetwork();
		fn.addEdge(1, 2, 8);
		fn.addEdge(1, 3, 6);
		fn.addEdge(2, 4, 6);
		fn.addEdge(3, 4, 10);
		fn.addEdge(3, 5, 12);
		fn.addEdge(4, 5, 3);
		fn.addEdge(4, 6, 4);
		fn.addEdge(5, 6, 6);
		return fn;
	}

	private static FlowNetwork createFn2() {
		FlowNetwork fn = new FlowNetwork();
		fn.addEdge(new Node(1, "s", fn), new Node(2, fn), 1);
		fn.addEdge(2, 3, 2);
		fn.addEdge(1, 4, 8);
		fn.addEdge(4, 2, 1);
		fn.addEdge(4, 5, 7);
		fn.addEdge(5, 3, 5);
		fn.addEdge(fn.getNode(3), new Node(6, "t", fn), 7);
		fn.addEdge(5, 6, 2);
		return fn;
	}
}
