
package m1graphs2025;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class EdgeTest 
{
  private Graph graph;
	private Node node1;
	private Node node2;

	private void expectException(Runnable r) {
		try {
			r.run();
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
  }

	@Before
  public void setUp() {
		graph = new Graph();
		node1 = new Node(1, graph);
		node2 = new Node(2, graph);
		graph.addNode(node1);
		graph.addNode(node2);
		graph.addNode(3);
  }
  
  @Test
  public void testAllConstructors() {
		new Edge(node1, node2, graph, 1);
		new Edge(node1, node2, graph);
		new Edge(1, 2, graph, 1);
		new Edge(1, 2, graph);
  }

  @Test
  public void testNullConstructors() {
		expectException(() -> new Edge(null, node1, graph));
		expectException(() -> new Edge(node1, null, graph));
		expectException(() -> new Edge(-1, 1, graph));
		expectException(() -> new Edge(0, 1, graph));
		expectException(() -> new Edge(1, -1, graph));
		expectException(() -> new Edge(1, 0, graph));
		expectException(() -> new Edge(null, node1, graph, 1));
		expectException(() -> new Edge(node1, null, graph, 1));
		expectException(() -> new Edge(-1, 1, graph, 1));
		expectException(() -> new Edge(0, 1, graph, 1));
		expectException(() -> new Edge(1, -1, graph, 1));
		expectException(() -> new Edge(1, 0, graph, 1));
		expectException(() -> new Edge(1, 1, null, 1));
  }
	
  @Test
  public void testGetters() {
		Edge edge = new Edge(1, 2, graph);
		assertTrue(edge.from().getId() == 1);
		assertTrue(edge.to().getId() == 2);
		assertTrue(edge.getGraph() == graph);
  }


  @Test
  public void testSymetricNotWeighted() {
		Edge edge = new Edge(1, 2, graph);
		Edge sym = edge.getSymetric();
		assertTrue(sym.from().getId() == 2);
		assertTrue(sym.to().getId() == 1);
		assertFalse(sym.isWeighted());
		assertTrue(sym.getWeight() == null);
  }

  @Test
  public void testSymetricWeighted() {
		Edge edge = new Edge(1, 2, graph, 1);
		Edge sym = edge.getSymetric();
		assertTrue(sym.from().getId() == 2);
		assertTrue(sym.to().getId() == 1);
		assertTrue(sym.isWeighted());
		assertTrue(sym.getWeight() == 1);
  } 

  @Test
  public void testShelfLoop() {
		Edge edge = new Edge(1, 1, graph);
		assertTrue(edge.isSelfLoop());
  }

  @Test
  public void testNotSelfLoop() {
		Edge edge = new Edge(1, 2, graph);
		assertFalse(edge.isSelfLoop());
  }

	@Test
	public void testIsMultiedge_SingleEdge() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		graph.addEdge(e1);

		assertFalse(e1.isMultiEdge());
	}

	@Test
	public void testIsMultiedge_TwoParallelEdges() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		Edge e2 = new Edge(n1, n2, graph);
		graph.addEdge(e1);
		graph.addEdge(e2);

		assertTrue(e1.isMultiEdge());
		assertTrue(e2.isMultiEdge());
	}

	@Test
	public void testIsMultiedge_DifferentTarget() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		Edge e1 = new Edge(n1, n2, graph);
		Edge e2 = new Edge(n1, n3, graph);
		graph.addEdge(e1);
		graph.addEdge(e2);

		assertFalse(e1.isMultiEdge());
		assertFalse(e2.isMultiEdge());
	}

	@Test
	public void testIsMultiedge_SelfLoopWithDuplicate() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		Edge e1 = new Edge(n1, n1, graph);
		Edge e2 = new Edge(n1, n1, graph);
		graph.addEdge(e1);
		graph.addEdge(e2);

		assertTrue(e1.isMultiEdge());
		assertTrue(e2.isMultiEdge());
	}

  @Test
  public void testEquals() {
		assertTrue(new Edge(1, 2, graph).equals(new Edge(1, 2, graph))); //Equals
		assertTrue(new Edge(1, 2, graph, 1).equals(new Edge(1, 2, graph, 1))); // Equals
		assertFalse(new Edge(1, 2, graph).equals(new Edge(1, 1, graph))); //To diff
		assertFalse(new Edge(1, 2, graph, 1).equals(new Edge(1, 1, graph, 1))); //To diff
		assertFalse(new Edge(1, 2, graph).equals(new Edge(3, 2, graph))); //From diff
		assertFalse(new Edge(1, 2, graph, 1).equals(new Edge(3, 2, graph, 1))); //From diff
		assertFalse(new Edge(1, 2, graph, 1).equals(new Edge(1, 2, graph, 3))); //Weight diff
		Edge edge = new Edge(1, 2, graph);
		assertTrue(edge.equals(edge));
		assertFalse(edge.equals(1));
  }

  @Test
  public void testCompareTo() {
		assertTrue(new Edge(1, 2, graph).compareTo(new Edge(1, 2, graph)) == 0); //Equals all
		assertTrue(new Edge(1, 2, graph).compareTo(new Edge(2, 2, graph)) < 0); //Lower from
		assertTrue(new Edge(3, 2, graph).compareTo(new Edge(2, 2, graph)) > 0); //Higher from
		assertTrue(new Edge(1, 1, graph).compareTo(new Edge(1, 2, graph)) < 0); //Lower to
		assertTrue(new Edge(1, 3, graph).compareTo(new Edge(1, 2, graph)) > 0); //Higher to
		assertTrue(new Edge(1, 2, graph, 0).compareTo(new Edge(1, 2, graph, 1)) < 0); //Lower weight
		assertTrue(new Edge(1, 2, graph, 1).compareTo(new Edge(1, 2, graph, 0)) > 0); //Higher weight
  }

  @Test 
  public void testWeight() {
		Edge edge = new Edge(1, 2, graph, 1);
		assertTrue(edge.isWeighted());
		assertTrue(edge.getWeight() == 1);
  }

  @Test
  public void testNotWeight() {
		Edge edge = new Edge(1, 2, graph);
		assertFalse(edge.isWeighted());
		assertTrue(edge.getWeight() == null);
  }
}
