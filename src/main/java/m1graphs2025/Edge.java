package m1graphs2025;

import java.util.Objects;

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

  /**
   * Constructs a directed edge between two nodes with an optional weight
   *
   * @param from   the source node (must not be null)
   * @param to     the target node (must not be null)
   * @param weight the wieght of the edge, or null for unweighted
   * @throws IllegalArgumentException if from or to is null
   */
  public Edge(Node from, Node to, Integer weight) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("From and to cannot be null");
    }

    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  /**
   * Constructs an unweighted directed edge between two nodes
   *
   * @param from the sorce node (must not be null)
   * @param to the target node (must not be null)
   * @throws IllegalArgumentException if from or to is null
   */
  public Edge(Node from, Node to) {
    this(from, to, null);
  }

  /**
   * Constructs a directed edge from node IDs with an optional weight
   *
   * @param fromId the ID of the source node
   * @param toId   the ID of the target node
   * @param weight thie weight of the edge, or null for unweighted
   * @throws IllegalArgumentException if fromId or toId <= 0
   */
  public Edge(int fromId, int toId, Integer weight) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Constructs an unweighted directed edge from node IDs
   *
   * @param fromId the ID of the source node
   * @param toId   the ID of the target node
   * @throws IllegalArgumentException of fromId or toId <= 0
   */
  public Edge(int fromId, int toId) {
    this(fromId, toId, null);
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
   * Returns a new edge with the same weight but reversed direction
   * 
   * @return a symmetric edge (from -> to becomes to -> from)
   */
  public Edge getSymmetric() {
    return new Edge(to, from, weight);
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
    return (this.from.equals(other.from) && this.to.equals(other.to) && this.weight.equals(other.weight));
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

    return this.weight.compareTo(o.weight);
  }
}
