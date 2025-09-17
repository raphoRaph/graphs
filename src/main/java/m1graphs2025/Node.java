package m1graphs2025;

public class Node implements Comparable<Node>{
  final int id;
  Graph graphHolder;

  public Node(int id, String name, Graph graphHolder) {
    this.id = id;
    this.name = name;
    this.graphHolder = graphHolder;
  }


  public Node(int id, Graph graphHolder) {
    this(id, "", graphHolder);
  }

  public Node() {

  }

  public int getId() {
    return this.id;
  }

  public Graph getGraph() {
    return null;
  }

  public String getName() {
    return this.name;
  }

  public List<Node> getSuccessors() {
    return new ArrayList<Node>();
  }

  public List<Node> getSuccessorsMulti() {
    return new ArrayList<Node>();
  }

  public bool adjacent(Node u) {
    return true;
  }

  public int inDegree() {
    return -1;
  }

  public int outDegree() {
    return -1;
  }

  public int degree() {
    return -1;
  }

  public List<Edge> getOutEdges() {
    return new ArrayList<Edge>();
  }

  public List<Edge> getInEdges() {
    return new ArrayList<Edge>();
  }

  public List<Edge> getIncidentEdges() {
    return new ArrayList<Edge>();
  }

  public List<Edge> getEdgesTo(Node u) {
    return new ArrayList<Edge>();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Node)) {
      return false;
    }

    Node other = (Node)obj;

    return (this.id == other.id && this.name == other.name);
  }

  @Override
  public int hashCode() {
    return this.id;
  }

  @Override
  public int compareTo(Node o) {
    return this.id - o.id;
  }
}
