package m1graphs2025;

import java.util.Objects;
import java.util.NoSuchElementException;

/**
 * This class represents a directed edge between two Node objects in a graph structure
 * 
 * An edge connects a source node ("from") to a target node ("to") and it may optionally have an associated weight
 * 
 * @see Node
 * @see Graph
 */
public class Edge implements Comparable<Edge>{
	private Node from;
	private Node to;
	private Integer weight;
	private Graph graphHolder;

	/**
	 * Constructs a directed edge between two nodes with an optional weight
	 *
	 * @param from	 the source node (must not be null)
	 * @param to		 the target node (must not be null)
	 * @param graphHolder the graph that owns this edge
	 * @param weight the wieght of the edge, or null for unweighted
	 * @throws IllegalArgumentException if from or to is null or graphHolder is null
	 * @throws NullPointerException if graphHolder is null
	 * @throws NoSuchElementException if fromId or toId is not in graphHolder
	 */
	public Edge(Node from, Node to, Graph graphHolder, Integer weight) {
		if (from == null || to == null) {
			throw new NullPointerException("From and to cannot be null");
		}
		if (graphHolder == null) {
			throw new NullPointerException("GraphHolder cannot be null");
		}
		if (!graphHolder.usesNode(from) || !graphHolder.usesNode(to)) {
			throw new NoSuchElementException("From or to must be in graphHolder");
		}
		this.from = from;
		this.to = to;
		this.graphHolder = graphHolder;
		this.weight = weight;
	}

	/**
	 * Constructs an unweighted directed edge between two nodes
	 *
	 * @param from the sorce node (must not be null)
	 * @param to the target node (must not be null)
	 * @throws IllegalArgumentException if from or to is null
	 */
	public Edge(Node from, Node to, Graph graphHolder) {
		this(from, to, graphHolder, null);
	}

	/**
	 * Constructs a directed edge from node IDs with an optional weight
	 *
	 * @param fromId the ID of the source node
	 * @param toId	 the ID of the target node
	 * @param graphHolder the graph that owns this edge 
	 * @param weight thie weight of the edge, or null for unweighted
	 * @throws IllegalArgumentException if fromId or toId <= 0
	 * @throws NullPointerException if graphHolder is null
	 * @throws NoSuchElementException if fromId or toId is not in graphHolder
	 */
	public Edge(int fromId, int toId, Graph graphHolder, Integer weight) {
		if (fromId <= 0 || toId <= 0) {
			throw new IllegalArgumentException("FromId and toId must be higher than 0");
		}
		if (graphHolder == null) {
			throw new NullPointerException("GraphHolder cannot be null");
		}
		if (!graphHolder.usesNode(fromId) || !graphHolder.usesNode(toId)) {
			throw new NoSuchElementException("FromId or toId must be in graphHoder");
		}
		this.from = graphHolder.getNode(fromId);
		this.to = graphHolder.getNode(toId);
		this.graphHolder = graphHolder;
		this.weight = weight;
	}

	/**
	 * Constructs an unweighted directed edge from node IDs
	 *
	 * @param fromId the ID of the source node
	 * @param toId	 the ID of the target node
	 * @param graphHolder the graph that owns this edge
	 * @throws IllegalArgumentException if fromId or toId <= 0
	 * @throws NullPointerException if graphHolder is null
	 * @throws NoSuchElementException if fromId or toId is not in graphHolder
	 */
	public Edge(int fromId, int toId, Graph graphHolder) {
		this(fromId, toId, graphHolder, null);
	}

	/**
	 * @return the source node of this edge
	 */
	public Node from() {
		return this.from;
	}

	/**
	 * @return the target node of this edge
	 */
	public Node to() {
		return this.to;
	}

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return this.graphHolder;
	}

	/**
	 * Returns a new edge with the same weight but reversed direction
	 * 
	 * @return a symmetric edge (from -> to becomes to -> from)
	 */
	public Edge getSymetric() {
		return new Edge(to, from, graphHolder, weight);
	}

	/**
	 * Checks whether this edge is a self-loop
	 *
	 * @return true if this edge is a self-loop; false otherwier
	 */
	public boolean isSelfLoop() {
		return from == to;
	}

	/**
	 * TODO
	 */
	public boolean isMultiEdge() {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * @return true if the edge has a weight; false if it is unweighted
	 */
	public boolean isWeighted() {
		return weight != null;
	}

	/**
	 * @return the weight of this edge, or null if unweighted
	 */
	public Integer getWeight() {
		return this.weight;
	}

	/**
	 * Compares this edge to another object for equality
	 *
	 * Two edges are considerd equal if they have the same source node, targe node, and weight
	 *
	 * @param obj the object to comapre
	 * @return true if obj is an Edge with the same properties
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Edge)) {
			return false;
		}

		Edge other = (Edge)obj;
		return (this.from.equals(other.from) && this.to.equals(other.to) && this.weight == other.weight);
	}

	/**
	 * @return a hash code consistent with Object
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.from, this.to, this.weight);
	}

	/**
	 * Compares this edge to another edge for ordering
	 *
	 * The comparison is performed in the followind order : source, target, weight
	 *
	 * @param o the other edge
	 * @return a negative integer, zero, or a positive integer as ths edge is less than, equal to, or greated than the specified edge
	 */
	@Override
	public int compareTo(Edge o) {
		if (this.from != o.from) {
			return this.from.compareTo(o.from);
		}

		if (this.to != o.to) {
			return this.to.compareTo(o.to);
		}

		if (this.weight == null || o.weight == null) { //weight equal null
			return 0;
		}

		return this.weight.compareTo(o.weight);
	}
}
