package m1graphs2025;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.util.List;

public class GraphTest
{
	private Graph emptyGraph;
	private Graph	graph;

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
		emptyGraph = new Graph();
		graph = new Graph(2, 0, 1, 0, 0);
  }
  
  @Test
  public void testAllConstructors()
  {
		new Graph();
		new Graph(1, 2, 2, 1, 2, 1, 0);
  }

  @Test
  public void testNullConstructors()
  {
		expectException(() -> new Graph(-1, 0));
		expectException(() -> new Graph(0, 0, -1));
  }

/**
 * NODE TEST
 */
	@Test
	public void testNbNodes()
	{
		assertTrue(new Graph().nbNodes() == 0);
		assertTrue(new Graph(1, 0, 2, 0).nbNodes() == 2);
		assertTrue(new Graph(1, 0, 0, 0).nbNodes() == 3);
	}

	@Test
	public void testUsesNode()
	{
		Node node = graph.getNode(1);
		
		assertFalse(emptyGraph.usesNode(node));
		assertFalse(emptyGraph.usesNode(1));
		assertTrue(graph.usesNode(node));
		assertFalse(graph.usesNode(0));
		assertTrue(graph.usesNode(1));
		assertTrue(graph.usesNode(2));
		assertTrue(graph.usesNode(3));
		assertFalse(graph.usesNode(4));

		assertFalse(emptyGraph.usesNode(0));
		assertFalse(emptyGraph.usesNode(-1));
		assertFalse(emptyGraph.usesNode(-1));
		assertFalse(graph.usesNode(0));
		assertFalse(graph.usesNode(-1));
	}

	@Test
	public void testHoldsNode()
	{
		Graph graph2 = new Graph(2, 0, 0);
		Node node = graph2.getNode(1);
		assertFalse(graph.holdsNode(node));
		assertTrue(graph.holdsNode(graph.getNode(1)));
	}

	@Test
	public void testGetNode() {
		assertTrue(graph.getNode(1).getId() == 1);
		assertTrue(graph.getNode(0) == null);
		assertTrue(graph.getNode(-1) == null);
	}

	@Test
	public void testAddNode() {
		assertFalse(graph.addNode(1));
		assertFalse(graph.addNode(2));
		expectException(() -> graph.addNode(0));
		expectException(() -> graph.addNode(-1));
		Graph graphEmpty = new Graph();
		Node node = new Node(1, graphEmpty);
		assertFalse(graph.addNode(node));
		Node node2 = new Node(7, graphEmpty);
		assertTrue(graph.addNode(node2));
		assertTrue(graph.usesNode(node2));
	}

	@Test
	public void testRemoveNode() {
		assertFalse(graph.removeNode(0));
		assertFalse(graph.removeNode(-1));
		assertFalse(graph.removeNode(6));
		assertTrue(graph.removeNode(1));
		assertTrue(graph.removeNode(graph.getNode(2)));

		assertFalse(graph.usesNode(1));
		assertFalse(graph.usesNode(2));
		assertTrue(graph.usesNode(3));
	}

	@Test
	public void testAllNodes() {
		Graph graph2 = new Graph();

		List<Node> emptyList = graph2.getAllNodes();
		assertEquals(0, emptyList.size());

		Node n1 = new Node(1, graph2);
		Node n2 = new Node(2, graph2);
		Node n3 = new Node(3, graph2);

		graph2.addNode(n1);
		graph2.addNode(n2);
		graph2.addNode(n3);

		List<Node> nodesList = graph2.getAllNodes();
		assertEquals(3, nodesList.size());

		assertTrue(nodesList.contains(n1));
		assertTrue(nodesList.contains(n2));
		assertTrue(nodesList.contains(n3));
	}

	@Test
	public void testLargestNodeId() {
		//NORMAL
		Graph graph1 = new Graph();

		graph1.addNode(1);
		graph1.addNode(5);
		graph1.addNode(3);

		int result = graph1.largestNodeId();

		assertEquals(5, result);

		//SINGLE
		Graph graph2 = new Graph();

		graph2.addNode(42);
		assertEquals(42, graph2.largestNodeId());

		//EMPTY
		Graph graph3 = new Graph();
		assertEquals(0, graph3.largestNodeId());
	}

	@Test
	public void testSmallestNodeId() {
		//NORMAL
		Graph graph1 = new Graph();

		graph1.addNode(10);
		graph1.addNode(2);
		graph1.addNode(5);

		int result = graph1.smallestNodeId();

		assertEquals(2, result);

		//SINGLE
		Graph graph2 = new Graph();

		graph2.addNode(99);

		assertEquals(99, graph2.smallestNodeId());

		//EMPTY
		Graph graph3 = new Graph();

		assertEquals(-1, graph3.smallestNodeId());
	}


@Test
	public void testGetSuccessors() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		graph.addEdge(e1);

		assertEquals(n1.getSuccessors(), graph.getSuccessors(n1));
		assertEquals(n1.getSuccessorsMulti(), graph.getSuccessorsMulti(n1));
	}

	@Test
	public void testAdjacent() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		Edge e1 = new Edge(n1, n2, graph);
		graph.addEdge(e1);

		// Node wrappers
		assertTrue(graph.adjacent(n1, n2));
		assertFalse(graph.adjacent(n2, n1));
		assertFalse(graph.adjacent(n1, n3));

		// ID wrappers
		assertTrue(graph.adjacent(1, 2));
		assertFalse(graph.adjacent(2, 1));
		assertFalse(graph.adjacent(1, 3));
	}

	@Test
	public void testDegreeMethods() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		Edge e1 = new Edge(n1, n2, graph);
		Edge e2 = new Edge(n2, n3, graph);
		Edge e3 = new Edge(n3, n1, graph);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);

		// inDegree
		assertEquals(n1.inDegree(), graph.inDegree(n1));
		assertEquals(n1.inDegree(), graph.inDegree(1));

		assertEquals(n2.inDegree(), graph.inDegree(n2));
		assertEquals(n2.inDegree(), graph.inDegree(2));

		// outDegree
		assertEquals(n1.outDegree(), graph.outDegree(n1));
		assertEquals(n1.outDegree(), graph.outDegree(1));

		assertEquals(n2.outDegree(), graph.outDegree(n2));
		assertEquals(n2.outDegree(), graph.outDegree(2));

		// total degree
		assertEquals(n1.degree(), graph.degree(n1));
		assertEquals(n1.degree(), graph.degree(1));
		assertEquals(n2.degree(), graph.degree(n2));
		assertEquals(n2.degree(), graph.degree(2));
	}
}
