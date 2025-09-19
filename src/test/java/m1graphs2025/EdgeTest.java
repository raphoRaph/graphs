
package m1graphs2025;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EdgeTest 
{
  private Graph graph;

  void setUp() {
    graph = new Graph();
  }
  
  @Test
  public void testAllConstructors()
  {
    Edge edge = new Edge(new Node(1, graph), new Node(2, graph), 1);
    Edge edge1 = new Edge(new Node(1, graph), new Node(2, graph));
    Edge edge2 = new Edge(1, 2, 1);
    Edge edge3 = new Edge(1, 2);
  }

  @Test
  public void testGetters()
  {
    Edge edge = new Edge(1, 2);
    assertTrue(edge.from().getId() == 1);
    assertTrue(edge.to().getId() == 2);
  }

  @Test
  public void testSymetricNotWeighted()
  {
    Edge edge = new Edge(1, 2);
    Edge sym = edge.getSymetric();
    assertTrue(sym.from().getId() == 1);
    assertTrue(sym.to().getId() == 2);
    assertFalse(sym.isWeighted();
    assertTrue(sym.getWeight() == null);
  }

  @Test
  public void testSymetricWeighted()
  {
    Edge edge = new Edge(1, 2, 1);
    Edge sym = edge.getSymetric();
    assertTrue(sym.from().getId() == 1);
    assertTrue(sym.to().getId() == 2);
    assertTrue(sym.isWeighted();
    assertTrue(sym.getWeight() == 1);
  } 

  @Test
  public void testShelfLoop()
  {
    Edge edge = new Edge(1, 1);
    assertTrue(edge.isSelfLoop());
  }

  @Test
  public void testNotSelfLoop()
  {
    Edge edge = new Edge(1, 2);
    assertFalse(edge.isSelfLoop());
  }

  @Test
  public void testIsMultiEdge()
  {
    assertTrue(false);
  }

  @Test
  public void testNotIsMutliEdge()
  {
    assertTrue(false);
  }
  
  @Test
  public void testEquals()
  {
    assertTrue(new Edge(1, 2).equals(new Edge(1, 2)); //Equals
    assertTrue(new Edge(1, 2, 1).equals(new Edge(1, 2, 1)); // Equals
    assertFalse(new Edge(1, 2).equals(new Edge(1, 1)); //To diff
    assertFalse(new Edge(1, 2, 1).equals(new Edge(1, 1, 1)); //To diff
    assertFalse(new Edge(1, 2).equals(new Edge(3, 2)); //From diff
    assertFalse(new Edge(1, 2, 1).equals(new Edge(3, 2, 1)); //From diff
    assertFalse(new Edge(1, 2, 1).equals(new Edge(1, 2, 3)); //Weight diff
  }

  @Test 
  public void testWeight()
  {
    Edge edge = new Edge(1, 2, 1);
    assertTrue(edge.isWeighted());
    assertTrue(edge.getWeight() == 1);
  }

  @Test
  public void testNotWeight()
  {
    Edge edge = new Edge(1, 2);
    assertFalse(edge.isWeighted());
    assertTrue(edge.getWeight() == null);
  }
}
