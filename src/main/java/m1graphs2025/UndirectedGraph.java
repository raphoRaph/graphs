package m1graphs2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an undirected graph.
 *
 * This class extends {@link Graph} but treats every edge as bidirectional.
 * All methods are redefined to ensure edges are interpreted symmetrically.
 *
 * @see Graph
 */
public class UndirectedGraph extends Graph {

	/**
	 * Constructs an undirected graph from a Successor Array.
	 *
	 * Each edge is considered bidirectional.
	 *
	 * @param nodes multiple integers representing nodes (given as a Successor
	 *              Array)
	 */
	public UndirectedGraph(int... nodes) {
		super(nodes);
	}

	/**
	 * Constructs an empty undirected graph.
	 */
	public UndirectedGraph() {
		super();
	}

	/**
	 * Checks if an edge exists in the undirected graph.
	 *
	 * Both directions are considered equivalent.
	 *
	 * @param e the Edge to check
	 * @return true if the edge exists in either direction
	 */
	@Override
	public boolean existsEdge(Edge e) {
		return super.existsEdge(e) && super.existsEdge(e.getSymetric());
	}

	/**
	 * Retrieves all incoming edges of a given node ID.
	 *
	 * In an undirected graph, this includes all edges connected to the node,
	 * regardless of direction.
	 *
	 * @param nodeId ID of the node
	 * @return list of connected edges (both directions)
	 */
	@Override
	public List<Edge> getInEdges(int nodeId) {
		ArrayList<Edge> list = new ArrayList<>();
		for (Edge edge : getAllEdges()) {
			if (edge.to().getId() == nodeId) {
				list.add(edge.getSymetric());
			} else if (edge.from().getId() == nodeId) {
				list.add(edge);
			}
		}
		return list;
	}

	/**
	 * Retrieves all outgoing edges of a given node ID.
	 *
	 * For undirected graphs, outgoing edges are identical to incoming ones.
	 *
	 * @param nodeId ID of the node
	 * @return list of connected edges
	 */
	@Override
	public List<Edge> getOutEdges(int nodeId) {
		return getInEdges(nodeId);
	}

	/**
	 * Calculates the in-degree of a node.
	 *
	 * In undirected graphs, in-degree equals the total number of incident edges.
	 *
	 * @param nodeId ID of the node
	 * @return the number of edges connected to this node
	 */
	@Override
	public int inDegree(int nodeId) {
		int nb = 0;
		for (Edge edge : getAllEdges()) {
			if (edge.from().getId() == nodeId || edge.to().getId() == nodeId) {
				nb++;
				if (edge.from().getId() == edge.to().getId()) {
					nb++;
				}
			}
		}
		return nb;
	}

	/**
	 * Calculates the out-degree of a node.
	 *
	 * For undirected graphs, out-degree equals in-degree.
	 *
	 * @param nodeId ID of the node
	 * @return the number of edges connected to this node
	 */
	@Override
	public int outDegree(int nodeId) {
		return inDegree(nodeId);
	}

	/**
	 * Returns the total degree of a node.
	 *
	 * For undirected graphs, the degree is identical to in-degree.
	 *
	 * @param nodeId ID of the node
	 * @return the total number of edges connected to this node
	 */
	@Override
	public int degree(int nodeId) {
		return inDegree(nodeId);
	}

	/**
	 * Retrieves all incident edges of a given node.
	 *
	 * @param nodeId ID of the node
	 * @return list of all edges connected to this node
	 */
	@Override
	public List<Edge> getIncidentEdges(int nodeId) {
		return getInEdges(nodeId);
	}

	/**
	 * Computes the transitive closure of the undirected graph.
	 *
	 * @return a new UndirectedGraph where edges represent reachability
	 */
	@Override
	public UndirectedGraph getTransitiveClosure() {
		UndirectedGraph closure = new UndirectedGraph();

		// Simple graph
		for (Node from : getAllNodes()) {
			for (Edge edge : super.getOutEdges(from)) {
				if (!edge.isSelfLoop() && !closure.isMultiEdge(edge) && !closure.existsEdge(edge)) {
					closure.addEdge(edge.from(), edge.to());
				}
			}
		}
		System.out.println("AH" + closure.toDotString());
		// Transitive closure
		for (Node from : getAllNodes()) {
			for (Edge fromMidEdge : getOutEdges(from)) {
				for (Edge midToEdge : getOutEdges(fromMidEdge.to())) {
					Node to = midToEdge.to();
					if (from.getId() == 3 && to.getId() == 4) {
						System.out.println("BH");
					}
					if (!closure.existsEdge(from, to) && from.getId() != to.getId()) {
						closure.addEdge(from, to);
					}
				}
			}
		}
		return closure;
	}

	/**
	 * Returns the reverse of the undirected graph.
	 *
	 * For undirected graphs, the reverse is identical to the original graph.
	 *
	 * @return this graph (since it is symmetric)
	 */
	@Override
	public UndirectedGraph getReverse() {
		return this;
	}

	/**
	 * Converts the undirected graph to an adjacency matrix.
	 *
	 * Each undirected edge is represented symmetrically in the matrix.
	 *
	 * @return a 2D integer array representing adjacency
	 */
	@Override
	public int[][] toAdjMatrix() {
		int maxNode = largestNodeId();
		int[][] matrix = new int[maxNode][maxNode];

		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			int fromId = pair.getKey().getId();
			for (Edge edge : getOutEdges(pair.getKey())) {
				int toId = edge.to().getId();
				matrix[fromId - 1][toId - 1]++;
			}
		}
		return matrix;
	}

	/**
	 * Converts the undirected graph to a DOT format string.
	 *
	 * Uses the "--" connector to represent bidirectional edges.
	 *
	 * @return a string representing the graph in DOT format
	 */
	@Override
	public String toDotString() {
		return super.toDotString(false);
	}

	/**
	 * Creates an undirected graph from a DOT file (.gv by default).
	 *
	 * @param filename name of the file without extension
	 * @return the imported UndirectedGraph
	 */
	public static UndirectedGraph fromDotFile(String filename) {
		return fromDotFile(filename, ".gv");
	}

	/**
	 * Creates an undirected graph from a DOT file with a custom extension.
	 *
	 * Only edges are imported; isolated nodes are not added.
	 *
	 * @param filename  the file name without extension
	 * @param extension the file extension (e.g., ".gv" or ".dot")
	 * @return the imported UndirectedGraph
	 */
	public static UndirectedGraph fromDotFile(String filename, String extension) {
		Graph graph = Graph.fromDotFile(filename, extension);
		UndirectedGraph undirectedGraph = new UndirectedGraph();
		for (Edge edge : graph.getAllEdges()) {
			undirectedGraph.addEdge(edge.from(), edge.to(), edge.getWeight());
		}
		return undirectedGraph;
	}
}
