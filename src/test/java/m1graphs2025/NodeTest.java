package m1graphs2025;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.util.List;
import java.util.Arrays;

public class NodeTest 
{
	private Graph graph;
	
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
		expectException(() -> new Node(-1, graph));
		expectException(() -> new Node(1, null));
		graph.addNode(1);
		expectException(() -> new Node(1, graph));
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
		assertFalse(new Node(1, "1", graph).equals(1)); //Other object
	}

	@Test
	public void testCompareTo()
	{
		assertTrue(new Node(1, graph).compareTo(new Node(1, graph)) == 0); //Equal
		assertTrue(new Node(1, graph).compareTo(new Node(2, graph)) < 0); //Lower
		assertTrue(new Node(2, graph).compareTo(new Node(1, graph)) > 0); //Higher
	}

	@Test
	public void testgetSuccessors_Normal() {
		Graph graph = new Graph();

		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		Node n4 = new Node(4, graph);

		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);

		graph.addEdge(n1, n2);
		graph.addEdge(n1, n3);
		graph.addEdge(n1, n2); // duplicate to test distinct()
		graph.addEdge(n2, n4);

		List<Node> successors = n1.getSuccessors();
		assertEquals(2, successors.size());
		assertTrue(successors.contains(n2));
		assertTrue(successors.contains(n3));

		List<Node> successorsMulti = n1.getSuccessorsMulti();
		assertEquals(3, successorsMulti.size());
		assertEquals(Arrays.asList(n2, n3, n2), successorsMulti);
	}
	@Test
	public void testGetSuccessors_NoOutgoingEdges() {
		Graph graph = new Graph();
		Node n1 = new Node(1,graph);
		graph.addNode(n1);

		List<Node> successors = n1.getSuccessors();
		assertTrue(successors.isEmpty());
		List<Node> successorsMulti = n1.getSuccessorsMulti();
		assertTrue(successorsMulti.isEmpty());
	}

	@Test
	public void testGetSuccessors_SelfLoop() {
		Graph graph1 = new Graph();
		Node n1 = new Node(1, graph1);
		graph1.addNode(n1);

		graph1.addEdge(n1, n1);
		graph1.addEdge(n1, n1);

		List<Node> successors = n1.getSuccessors();

		assertEquals(1, successors.size());
		assertTrue(successors.contains(n1));

		List<Node> successorsMulti = n1.getSuccessorsMulti();

		assertEquals(2, successorsMulti.size());
		assertEquals(Arrays.asList(n1, n1), successorsMulti);
	}
	@Test
	public void testGetSuccessors_DisconnectedNodes() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		graph.addEdge(n1, n2);

		assertEquals(Arrays.asList(n2), n1.getSuccessors());
		assertTrue(n2.getSuccessors().isEmpty());
		assertTrue(n3.getSuccessors().isEmpty());
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

		graph.addEdge(n1, n2);
		graph.addEdge(n2, n3);

		assertTrue(n1.adjacent(n2));
		assertTrue(n1.adjacent(2));
		assertFalse(n1.adjacent(n3));
		assertFalse(n1.adjacent(3));
	}
	
	@Test
	public void testDegrees_NoEdges() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		assertEquals(0, n1.inDegree());
		assertEquals(0, n1.outDegree());
		assertEquals(0, n1.degree());
	}
	
	@Test
	public void testDegrees_SingleOutgoingEdge() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		graph.addEdge(n1, n2);

		assertEquals(0, n1.inDegree());
		assertEquals(1, n1.outDegree());
		assertEquals(1, n1.degree());

		assertEquals(1, n2.inDegree());
		assertEquals(0, n2.outDegree());
		assertEquals(1, n2.degree());
	}

	@Test
	public void testDegrees_MultipleOutgoingEdges() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		graph.addEdge(n1, n2);
		graph.addEdge(n1, n3);

		assertEquals(0, n1.inDegree());
		assertEquals(2, n1.outDegree());
		assertEquals(2, n1.degree());

		assertEquals(1, n2.inDegree());
		assertEquals(0, n2.outDegree());
		assertEquals(1, n2.degree());

		assertEquals(1, n3.inDegree());
		assertEquals(0, n3.outDegree());
		assertEquals(1, n3.degree());
	}

	@Test
	public void testDegrees_SelfLoop() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		// self-loop adds both an incoming and outgoing edge
		graph.addEdge(n1, n1);

		assertEquals(1, n1.inDegree());
		assertEquals(1, n1.outDegree());
		assertEquals(2, n1.degree());
	}

	@Test
	public void testDegrees_MultipleEdgesIncludingSelfLoop() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		graph.addEdge(n1, n2);
		graph.addEdge(n2, n1);
		graph.addEdge(n1, n1);

		assertEquals(2, n1.inDegree());
		assertEquals(2, n1.outDegree());
		assertEquals(4, n1.degree());

		assertEquals(1, n2.inDegree());
		assertEquals(1, n2.outDegree());
		assertEquals(2, n2.degree());
	}
	@Test
	public void testGetOutAndInEdges_SingleEdge() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		graph.addEdge(e1);

		// Outgoing from n1
		List<Edge> out1 = n1.getOutEdges();
		assertEquals(1, out1.size());
		assertEquals(e1, out1.get(0));

		// Incoming to n2
		List<Edge> in2 = n2.getInEdges();
		assertEquals(1, in2.size());
		assertEquals(e1, in2.get(0));

		// No incoming to n1
		assertTrue(n1.getInEdges().isEmpty());

		// No outgoing from n2
		assertTrue(n2.getOutEdges().isEmpty());
	}

	@Test
	public void testGetEdgesTo_SpecificTarget() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		Edge e2 = new Edge(n1, n2, graph);
		graph.addEdge(e1);
		graph.addEdge(e2); // multi-edge

		List<Edge> edgesToN2 = n1.getEdgesTo(n2);
		assertEquals(2, edgesToN2.size());
		assertTrue(edgesToN2.containsAll(List.of(e1, e2)));

		// Reverse direction — should be empty
		assertTrue(n2.getEdgesTo(n1).isEmpty());
	}

	@Test
	public void testGetOutAndInEdges_SelfLoop() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		Edge e1 = new Edge(n1, n1, graph);
		graph.addEdge(e1);

		List<Edge> out = n1.getOutEdges();
		List<Edge> in = n1.getInEdges();

		assertEquals(1, out.size());
		assertEquals(1, in.size());
		assertEquals(e1, out.get(0));
		assertEquals(e1, in.get(0));
	}

	@Test
	public void testGetIncidentEdges() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		Edge e1 = new Edge(n1, n1, graph);
		Edge e2 = new Edge(n3, n1, graph);
		graph.addEdge(e1);
		graph.addEdge(e2);

		List<Edge> incident = n1.getIncidentEdges();

		// n1 has one outgoing and one incoming edge
		assertEquals(3, incident.size());
		assertTrue(incident.containsAll(List.of(e1, e2, e1)));
	}

	@Test
	public void testIncidentEdges_NoEdges() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		assertTrue(n1.getIncidentEdges().isEmpty());
	}

	@Test
	public void testIncidentEdges_OutgoingOnly() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		graph.addEdge(e1);

		List<Edge> incident = n1.getIncidentEdges();
		assertEquals(1, incident.size());
		assertTrue(incident.contains(e1));
	}

	@Test
	public void testIncidentEdges_IncomingOnly() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n2, n1, graph);
		graph.addEdge(e1);

		List<Edge> incident = n1.getIncidentEdges();
		assertEquals(1, incident.size());
		assertTrue(incident.contains(e1));
	}

	@Test
	public void testIncidentEdges_IncomingAndOutgoing() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		Edge e1 = new Edge(n1, n2, graph); // outgoing
		Edge e2 = new Edge(n3, n1, graph); // incoming
		graph.addEdge(e1);
		graph.addEdge(e2);

		List<Edge> incident = n1.getIncidentEdges();
		assertEquals(2, incident.size());
		assertTrue(incident.containsAll(List.of(e1, e2)));
	}

	@Test
	public void testIncidentEdges_SelfLoop() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		Edge e1 = new Edge(n1, n1, graph);
		graph.addEdge(e1);

		List<Edge> incident = n1.getIncidentEdges();
		assertEquals(2, incident.size());
		assertTrue(incident.containsAll(List.of(e1, e1)));
	}

	@Test
	public void testIncidentEdges_MultipleEdgesAndSelfLoop() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);


		Edge e1 = new Edge(n1, n2, graph); // out
		Edge e2 = new Edge(n2, n1, graph); // in
		Edge e3 = new Edge(n1, n1, graph); // self-loop (both in & out)
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);

		List<Edge> incident = n1.getIncidentEdges();

		// Outgoing = [e1, e3], Incoming = [e2, e3]
		// => total = 4
		assertEquals(4, incident.size());
		assertTrue(incident.containsAll(List.of(e1, e2, e3)));
	}


@Test
	public void testGetEdgesTo_NoEdges() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		List<Edge> edges = n1.getEdgesTo(2);
		assertTrue(edges.isEmpty());
	}

	@Test
	public void testGetEdgesTo_SingleEdgeMatch() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		graph.addEdge(e1);

		List<Edge> edges = n1.getEdgesTo(2);
		assertEquals(1, edges.size());
		assertEquals(e1, edges.get(0));
	}

	@Test
	public void testGetEdgesTo_MultipleEdgesToSameNode() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		graph.addNode(n1);
		graph.addNode(n2);

		Edge e1 = new Edge(n1, n2, graph);
		Edge e2 = new Edge(n1, n2, graph);
		graph.addEdge(e1);
		graph.addEdge(e2);

		List<Edge> edges = n1.getEdgesTo(2);
		assertEquals(2, edges.size());
		assertTrue(edges.containsAll(List.of(e1, e2)));
	}

	@Test
	public void testGetEdgesTo_NoMatchingEdge() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		Node n2 = new Node(2, graph);
		Node n3 = new Node(3, graph);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);

		Edge e1 = new Edge(n1, n2, graph);
		graph.addEdge(e1);

		List<Edge> edges = n1.getEdgesTo(3);
		assertTrue(edges.isEmpty());
	}

	@Test
	public void testGetEdgesTo_SelfLoop() {
		Graph graph = new Graph();
		Node n1 = new Node(1, graph);
		graph.addNode(n1);

		Edge e1 = new Edge(n1, n1, graph);
		graph.addEdge(e1);

		List<Edge> edges = n1.getEdgesTo(1);
		assertEquals(1, edges.size());
		assertEquals(e1, edges.get(0));
	}
}
