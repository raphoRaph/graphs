package m1graphs2025;

public class Edge implements Comparable<Edge>{
  Node from;
  Node to;
  int weight;

  public Edge() {

  }

  public Node from() {
    return this.from;
  }

  public Node to() {
    return this.to;
  }

  public Edge getSymmetric() {
    return new Edge(to, weight, from);
  }

  public boolean isSelfLoop() {
    return from == to;
  }

  public boolean isMultiEdge() {
    
  }

  public boolean isWeighted() {
    return weight == null;
  }

  public Integer getWeight() {
    return this.weight;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Edge)) {
      return false;
    }

    Edge other = (Edge)obj;

    return (this.from == other.from && this.to == other.to && this.weight == other.weight);
  }

  @Override
  public int hashCode() {
    return this.;
  }

  @Override
  public int compareTo(Node o) {
    if (this.from != o.from) {
      return Node.compareTo(this.from, o.from);
    }

    if (this.to != o.to) {
      return Node.compareTo(this.to, o.to);
    }

    return Integer.compareTo(this.weight, o.weight);
  }
}
