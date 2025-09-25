package m1graphs2025;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest 
{
  private Graph graph;

  void setUp() {
    graph = new Graph();
  }
  
  @Test
  public void testAllConstructors()
  {
    Node node = new Node(1, "1", graph);
    Node node1 = new Node(1, graph);
  }

  @Test
  public void testNullConstructors()
  {
    /*
    assertThrow(Exception.class, () -> {
      new Node(-1, graph);
    });
    */
  }

  @Test
  public void testGetters()
  {
    Node node = new Node(1, "2", graph);
    assertTrue(node.getId() == 1);
    assertTrue(node.getGraph().equals(graph));
    assertTrue(node.getName().equals("2"));
  }

  @Test
  public void testEquals()
  {
    Node node = new Node(1, "1", graph);
    assertTrue(node.equals(node));
    assertTrue(new Node(1, "1", graph).equals(new Node(1, "1", graph))); //same
    assertFalse(new Node(2, "1", graph).equals(new Node(1, "1", graph))); //id
    assertFalse(new Node(1, "1", graph).equals(new Node(1, "2", graph))); //name
    assertFalse(new Node(1, "1", graph).equals(new Edge(1, 2))); //Other object
  }

  @Test
  public void testCompareTo()
  {
    assertTrue(new Node(1, graph).compareTo(new Node(1, graph)) == 0); //Equal
    assertTrue(new Node(1, graph).compareTo(new Node(2, graph)) < 0); //Lower
    assertTrue(new Node(2, graph).compareTo(new Node(1, graph)) > 0); //Higher
  }
}
