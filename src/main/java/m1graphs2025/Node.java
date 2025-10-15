package m1graphs2025;

import java.util.Objects;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a node (vertex) in a graph
 *
 * A node is identified by a unique integer ID and may have an associated name
 * 
 * @see Graph
 * @see Edge
 */
public class Node implements Comparable<Node> {
	private final int id;
	private final String name;
	private Graph graphHolder;

	/**
	 * Creates a new node
	 *
	 * @param id          the unique identifier of the node, must be greater than 0
	 * @param name        the display name of the node (may be empty, but not null)
	 * @param graphHolder the graph that owns this node
	 * @throws IllegalArgumentException if id <= 0
	 * @throws NullPointerException     if graphHolder is null
	 * @throws IllegalArgumentException if id is already used
	 */
	public Node(int id, String name, Graph graphHolder) {
		if (id <= 0) {
			throw new IllegalArgumentException("Node id must be higher than 0");
		}
		if (graphHolder == null) {
			throw new NullPointerException("GraphHolder cannot be null");
		}
		if (graphHolder.usesNode(id)) {
			throw new IllegalArgumentException("Graph does not contain node with id: " + id);
		}
		this.id = id;
		this.name = name;
		this.graphHolder = graphHolder;
	}

	/**
	 * Creates a new node with an empty name
	 *
	 * @param id          the unique identifier of the node, must be greate than 0
	 * @param graphHolder the graph that owns this node
	 * @throws IllegalArgumentException if id <= 0
	 * @throws NullPointerException     if graphHolder is null
	 * @throws IllegalArgumentException if id is already used
	 */
	public Node(int id, Graph graphHolder) {
		this(id, "", graphHolder);
	}

	@Override
	public String toString() {
		return "" + this.id;
	}

	/**
	 * @retrun the node's unique identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the graph to which this node belongs
	 */
	public Graph getGraph() {
		return this.graphHolder;
	}

	/**
	 * @return the display name of this node (may be empty but not null)
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets all unique successors of this node (nodes reachable via outgoind edges)
	 *
	 * @return a list of distinct successor nodes
	 */
	public List<Node> getSuccessors() {
		return graphHolder.getSuccessors(this);
	}

	/**
	 * Gets all successors of this node, keeping duplicates
	 * (i.e., one entry per outgoind edge)
	 *
	 * @return a list of successor nodes, possibly with duplicates
	 */
	public List<Node> getSuccessorsMulti() {
		return graphHolder.getSuccessorsMulti(this);
	}

	/**
	 * Checks whether this node is adjacent to another node
	 *
	 * @param u the other node
	 * @return true if an edge from this node to u
	 * @throws NullPointerException if u is null
	 */
	public boolean adjacent(Node u) {
		return graphHolder.adjacent(this, u);
	}

	/**
	 * Checks whether this node is adjacent to a node with the given ID
	 *
	 * @param nodeId the identifier of the other node
	 * @return true if an edge from this node to the node with nodeId
	 * @throws IllegalArgumentException if nodeId <= 0
	 */
	public boolean adjacent(int nodeId) {
		return graphHolder.adjacent(this.getId(), nodeId);
	}

	/**
	 * @return the number of incoming edges of this node
	 */
	public int inDegree() {
		return graphHolder.inDegree(this);
	}

	/**
	 * @return the number of outgoing edges of this node
	 */
	public int outDegree() {
		return graphHolder.outDegree(this);
	}

	/**
	 * @return the total degree (sum of in-degree and out-degree) of this node
	 */
	public int degree() {
		return graphHolder.degree(this);
	}

	/**
	 * @return a list of outgoing edges from this node
	 */
	public List<Edge> getOutEdges() {
		return graphHolder.getOutEdges(this);
	}

	/**
	 * @return a list of incoming edges to this node
	 */
	public List<Edge> getInEdges() {
		return graphHolder.getInEdges(this);
	}

	/**
	 * @return a list of all incident edges (incoming + outgoing)
	 */
	public List<Edge> getIncidentEdges() {
		return graphHolder.getIncidentEdges(this);
	}

	/**
	 * Gets all edges from this node to another node
	 *
	 * @param u the target node
	 * @return a list of edges from this node to u
	 */
	public List<Edge> getEdgesTo(Node u) {
		return getEdgesTo(u.getId());
	}

	/**
	 * Gets all edges from this node to a node with a given ID
	 *
	 * @param nodeId the identifier of the target node
	 * @return a list of edges from this node to the node with nodeId
	 * @throws IllegalArgumentException if nodeId <= 0
	 */
	public List<Edge> getEdgesTo(int nodeId) {
		if (nodeId <= 0) {
			throw new IllegalArgumentException("Node ID must be higher than 0");
		}
		return getOutEdges().stream().filter(e -> e.to().getId() == nodeId).toList();
	}

	/**
	 * Comapres this node to another object for equality
	 *
	 * Nodes are considered equal if they have the same ID and the same name
	 *
	 * @param obj the object to compare
	 * @return true if obj is a Node with the same ID and name
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Node)) {
			return false;
		}

		Node other = (Node) obj;
		return (this.id == other.id && this.name.equals(other.name));
	}

	/**
	 * @return a hash code consistent with Object
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	/**
	 * Compares this node to another node by ID
	 *
	 * @param o the other node
	 * @return a negative integer, zero, or a positive integer if this node's ID is
	 *         less than, equal to, or greater than o's ID
	 */
	@Override
	public int compareTo(Node o) {
		return this.id - o.id;
	}
}
