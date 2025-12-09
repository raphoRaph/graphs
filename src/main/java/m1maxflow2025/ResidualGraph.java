package m1maxflow2025;

import java.util.ArrayList;
import java.util.List;

import m1graphs2025.Edge;
import m1graphs2025.Graph;
import m1graphs2025.Node;

/**
 * Represents the Residual Graph derived from a Flow Network.
 * It contains forward edges (remaining capacity) and backward edges (current flow)
 * to facilitate finding augmenting paths.
 */
public class ResidualGraph extends Graph {
	private List<Node> lastPath;
	Integer residualCapacity;

	/**
	 * Default constructor for an empty Residual Graph.
	 */
	public ResidualGraph() {
		super();
	}

	/**
	 * Constructs a Residual Graph from a given Flow Network.
	 * Forward edges are added if capacity > flow (residual capacity).
	 * Backward edges are added if flow > 0.
	 *
	 * @param flowNetwork The original flow network
	 * @return The constructed ResidualGraph
	 */
	public static ResidualGraph from(FlowNetwork flowNetwork) {
		ResidualGraph residualGraph = new ResidualGraph();

		for (Edge edge : flowNetwork.getAllEdges()) {
			FlowEdge flow = (FlowEdge) edge;
			int fromId = flow.from().getId();
			int toId = flow.to().getId();
			Integer capacity = flow.getWeight();
			Integer iFlow = flow.getFlow();

			residualGraph.addNodeIfAbsent(fromId);
			residualGraph.addNodeIfAbsent(toId);

			if (capacity - iFlow > 0) {
				residualGraph.addEdge(fromId, toId, capacity - iFlow);
			}
			if (iFlow > 0) {
				residualGraph.addEdge(toId, fromId, iFlow);
			}
		}
		return residualGraph;
	}

	/**
	 * Identifies the source node of the graph (assumed to be the node with the smallest ID).
	 *
	 * @return The source node
	 */
	public Node sourceNode() {
		int sourceId = smallestNodeId();
		return getNode(sourceId);
	}

	/**
	 * Identifies the target node of the graph (assumed to be the node with the largest ID).
	 *
	 * @return The target node
	 */
	public Node targetNode() {
		int sourceId = largestNodeId();
		return getNode(sourceId);
	}

	/**
	 * Calculates the bottleneck capacity (minimum edge weight) along a given path.
	 * Side effect: stores the path and bottleneck value for visualization purposes.
	 *
	 * @param path The path to evaluate
	 * @return The bottleneck capacity
	 */
	public int bottleneckOf(List<Node> path) {
		int b = Integer.MAX_VALUE;

		for (int i = 0; i < path.size() - 1; i++) {
			Node u = path.get(i);
			Node v = path.get(i + 1);

			int cap = (getResidualCapacity(u, v));

			b = Math.min(b, cap);
		}
		lastPath = path;
		residualCapacity = b;
		return b;
	}

	private int getResidualCapacity(Node u, Node v) {
		for (Edge e : getOutEdges(u)) {
			if (e.to() == v) {
				return e.getWeight();
			}
		}
		return 0;
	}

	private String pathString() {
		StringBuilder sb = new StringBuilder("Augmenting path: [");
		for (int i = 0; i < lastPath.size(); i++) {
			sb.append(lastPath.get(i));
			if (i < lastPath.size() - 1)
				sb.append(", ");
		}
		return sb.append("].").toString();
	}

	private boolean areConsecutive(Node from, Node to) {
		if (lastPath == null)
			return false;

		for (int i = 0; i < lastPath.size() - 1; i++) {
			if (lastPath.get(i).equals(from) && lastPath.get(i + 1).equals(to))
				return true;
		}
		return false;
	}

	/**
	 * Generates a DOT representation of the residual graph.
	 * If an augmenting path was last calculated, it highlights the path (blue) and the bottleneck edge (red font).
	 *
	 * @return A string containing the DOT representation
	 */
	@Override
	public String toDotString() {
		StringBuilder sb = new StringBuilder();

		sb.append("digraph residualGraph {\n")
				.append("\trankdir=\"LR\";\n");

		if (lastPath != null) {
			sb.append("\tlabel=\"Residual graph.\\n")
					.append(pathString() + "\\n")
					.append("Residual capacity: ")
					.append(residualCapacity)
					.append("\";\n");
		}

		for (Edge e : getAllEdges()) {
			boolean inPath = lastPath != null && areConsecutive(e.from(), e.to());
			boolean isBottleneck = inPath && e.getWeight() == residualCapacity;

			sb.append("\t")
					.append(e.from()).append(" -> ").append(e.to())
					.append(" [label=").append(e.getWeight())
					.append(", len=").append(e.getWeight());

			if (inPath) {
				sb.append(", penwidth=3, color=\"blue\"");
			}
			if (isBottleneck) {
				sb.append(", fontcolor=\"red\"");
			}

			sb.append("];\n");
		}

		sb.append("}\n");
		return sb.toString();
	}
}
