package m1graphs2025;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

public class GraphTest {
	private Graph emptyGraph;
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
		emptyGraph = new Graph();
		graph = new Graph(2, 0, 1, 0, 0);
	}

	@Test
	public void testAllConstructors() {
		new Graph();
		new Graph(1, 2, 2, 1, 2, 1, 0);
	}

	@Test
	public void testNullConstructors() {
		expectException(() -> new Graph(-1, 0));
		expectException(() -> new Graph(0, 0, -1));
	}

	/**
	 * NODE TEST
	 */
	@Test
	public void testNbNodes() {
		assertTrue(new Graph().nbNodes() == 0);
		assertTrue(new Graph(1, 0, 2, 0).nbNodes() == 2);
		assertTrue(new Graph(1, 0, 0, 0).nbNodes() == 3);
	}

	@Test
	public void testUsesNode() {
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
	public void testHoldsNode() {
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
		// NORMAL
		Graph graph1 = new Graph();

		graph1.addNode(1);
		graph1.addNode(5);
		graph1.addNode(3);

		int result = graph1.largestNodeId();

		assertEquals(5, result);

		// SINGLE
		Graph graph2 = new Graph();

		graph2.addNode(42);
		assertEquals(42, graph2.largestNodeId());

		// EMPTY
		Graph graph3 = new Graph();
		assertEquals(0, graph3.largestNodeId());
	}

	@Test
	public void testSmallestNodeId() {
		// NORMAL
		Graph graph1 = new Graph();

		graph1.addNode(10);
		graph1.addNode(2);
		graph1.addNode(5);

		int result = graph1.smallestNodeId();

		assertEquals(2, result);

		// SINGLE
		Graph graph2 = new Graph();

		graph2.addNode(99);

		assertEquals(99, graph2.smallestNodeId());

		// EMPTY
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

	@Test
	public void testAdjacentException() {
		assertThrows(NullPointerException.class, () -> graph.adjacent(null, null));
		assertThrows(NullPointerException.class, () -> graph.adjacent(graph.getNode(1), null));

		assertThrows(IllegalArgumentException.class, () -> graph.adjacent(-1, 1));
		assertThrows(IllegalArgumentException.class, () -> graph.adjacent(1, -1));
	}

	@Test
	public void testEdges() {
		graph = new Graph();

		assertEquals(0, graph.nbEdges());
		graph.addNode(1);
		graph.addNode(2);
		graph.addNode(3);

		Edge edge1 = new Edge(1, 2, graph, 1);
		Edge edge2 = new Edge(1, 2, graph, 2);
		Edge edge3 = new Edge(2, 3, graph);
		Edge edge4 = new Edge(3, 2, graph);
		Edge edge5 = new Edge(3, 1, graph);

		assertFalse(graph.existsEdge(edge1));
		graph.addEdge(edge1);
		assertEquals(1, graph.nbEdges());
		assertTrue(graph.existsEdge(edge1));
		assertTrue(graph.existsEdge(1, 2));
		assertFalse(graph.existsEdge(edge2));
		graph.addEdge(edge2);
		assertEquals(2, graph.nbEdges());
		assertTrue(graph.existsEdge(edge2));
		assertTrue(graph.existsEdge(1, 2));
		assertFalse(graph.existsEdge(edge3));
		assertFalse(graph.existsEdge(2, 3));
		graph.addEdge(edge3);
		assertEquals(3, graph.nbEdges());
		assertTrue(graph.existsEdge(edge3));
		assertTrue(graph.existsEdge(2, 3));
		assertFalse(graph.existsEdge(edge4));
		assertFalse(graph.existsEdge(3, 2));
		graph.addEdge(edge4);
		assertEquals(4, graph.nbEdges());
		assertTrue(graph.existsEdge(edge4));
		assertTrue(graph.existsEdge(3, 2));
		assertFalse(graph.existsEdge(edge5));
		assertFalse(graph.existsEdge(3, 1));
		graph.addEdge(edge5);
		assertEquals(5, graph.nbEdges());
		assertTrue(graph.existsEdge(edge5));
		assertTrue(graph.existsEdge(3, 1));

		List<Edge> edgesList = graph.getAllEdges();
		assertEquals(5, edgesList.size());

		assertTrue(edgesList.contains(edge1));
		assertTrue(edgesList.contains(edge2));
		assertTrue(edgesList.contains(edge3));
		assertTrue(edgesList.contains(edge4));
		assertTrue(edgesList.contains(edge5));

		List<Edge> edgesList2 = graph.getEdges(graph.getNode(1), graph.getNode(2));
		assertEquals(2, edgesList2.size());

		assertTrue(edgesList2.contains(edge1));
		assertTrue(edgesList2.contains(edge2));

		assertTrue(graph.isMultiEdge(edge1));
		assertTrue(graph.isMultiEdge(edge2));
		assertFalse(graph.isMultiEdge(edge3));
		assertFalse(graph.isMultiEdge(edge4));
		assertFalse(graph.isMultiEdge(edge5));

		assertTrue(graph.removeEdge(edge5));
		assertFalse(graph.existsEdge(edge5));

		assertFalse(graph.removeEdge(1, 3));

		assertTrue(graph.removeEdge(edge1));
		assertFalse(graph.existsEdge(edge1));
		assertTrue(graph.existsEdge(edge2));

		assertFalse(graph.removeEdge(edge1));
	}

	@Test
	public void testtoSuccessorArray() {
		int[] list = { 1, 0, 1, 0, 1, 0 };
		graph = new Graph(list);
		assertTrue(graph.existsEdge(1, 1));
		assertTrue(graph.existsEdge(2, 1));
		assertTrue(graph.existsEdge(3, 1));
		int[] listSuc = graph.toSuccessorArray();
		assertEquals(list.length, listSuc.length);
		for (int i = 0; i < list.length; i++) {
			assertEquals(list[i], listSuc[i]);
		}
	}

	@Test
	public void testtoAdjMatrix() {
		int[] list = { 1, 1, 0, 1, 0, 1, 0 };
		int[][] returnList = { { 2, 0, 0 }, { 1, 0, 0 }, { 1, 0, 0 } };
		graph = new Graph(list);
		int[][] listMat = graph.toAdjMatrix();
		assertEquals(returnList.length, listMat.length);
		assertEquals(returnList[0].length, listMat[0].length);

		for (int i = 0; i < returnList.length; i++) {
			for (int j = 0; j < returnList[i].length; j++) {
				assertEquals(returnList[i][j], listMat[i][j]);
			}
		}
	}

	@Test
	public void testGetReverse() {
		int[] list = { 1, 0, 1, 0, 1, 0 };
		graph = new Graph(list);
		assertTrue(graph.existsEdge(1, 1));
		assertTrue(graph.existsEdge(2, 1));
		assertTrue(graph.existsEdge(3, 1));
		Graph graphRev = graph.getReverse();
		int[] listRev = graphRev.toSuccessorArray();
		assertEquals(list.length, listRev.length);
		assertEquals(1, listRev[0]);
		assertEquals(2, listRev[1]);
		assertEquals(3, listRev[2]);
		assertEquals(0, listRev[3]);
		assertEquals(0, listRev[4]);
		assertEquals(0, listRev[5]);

		int[] listSuc = graphRev.getReverse().toSuccessorArray();
		assertEquals(list.length, listSuc.length);
		for (int i = 0; i < list.length; i++) {
			assertEquals(list[i], listSuc[i]);
		}
	}

	@Test
	public void testTransitiveClosure() {
		int[] list = { 2, 0, 3, 0, 1, 0 };
		graph = new Graph(list);
		assertTrue(graph.existsEdge(1, 2));
		assertTrue(graph.existsEdge(2, 3));
		assertTrue(graph.existsEdge(3, 1));

		Graph graphTrans = graph.getTransitiveClosure();
		assertTrue(graphTrans.existsEdge(1, 1));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(1), graphTrans.getNode(1)).size());
		assertTrue(graphTrans.existsEdge(1, 2));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(1), graphTrans.getNode(2)).size());
		assertTrue(graphTrans.existsEdge(1, 3));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(1), graphTrans.getNode(3)).size());
		assertTrue(graphTrans.existsEdge(2, 1));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(2), graphTrans.getNode(1)).size());
		assertTrue(graphTrans.existsEdge(2, 2));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(2), graphTrans.getNode(2)).size());
		assertTrue(graphTrans.existsEdge(2, 3));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(2), graphTrans.getNode(3)).size());
		assertTrue(graphTrans.existsEdge(3, 1));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(3), graphTrans.getNode(1)).size());
		assertTrue(graphTrans.existsEdge(3, 2));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(3), graphTrans.getNode(2)).size());
		assertTrue(graphTrans.existsEdge(3, 3));
		assertEquals(1, graphTrans.getEdges(graphTrans.getNode(3), graphTrans.getNode(3)).size());
	}

	@Test
	public void testisMultiGraph() {
		int[] list = { 1, 1, 0, 1, 0, 1, 0 };
		graph = new Graph(list);
		assertTrue(graph.existsEdge(1, 1));
		assertTrue(graph.existsEdge(2, 1));
		assertTrue(graph.existsEdge(3, 1));

		assertTrue(graph.isMultiGraph());
		assertFalse(graph.isSimpleGraph());

		assertTrue(graph.removeEdge(1, 1));

		assertFalse(graph.isMultiGraph());
		assertTrue(graph.isSimpleGraph());
	}

	@Test
	public void testHasSelfLoops() {
		int[] list = { 1, 0, 1, 0, 1, 0 };
		graph = new Graph(list);
		assertTrue(graph.existsEdge(1, 1));
		assertTrue(graph.existsEdge(2, 1));
		assertTrue(graph.existsEdge(3, 1));

		assertTrue(graph.hasSelfLoops());

		assertTrue(graph.removeEdge(1, 1));

		assertFalse(graph.hasSelfLoops());
	}

	private void sameArray(int[] res, List<Node> list) {
		for (int i = 0; i < res.length; i++) {
			assertEquals(res[i], list.get(i).getId());
		}
	}

	@Test
	public void testDFS() {
		int[] list = { 2, 0, 3, 0, 1, 0 };
		graph = new Graph(list);
		List<Node> dfs = graph.getDFS();

		for (int i = 0; i < dfs.size(); i++) {
			assertEquals(i + 1, dfs.get(i).getId());
		}

		int[] test1 = { 2, 0, 3, 0, 1, 0 };
		int[] res1 = { 1, 2, 3 };
		graph = new Graph(test1);
		var list1 = graph.getDFS(1);
		sameArray(res1, list1);

		int[] test2 = { 2, 3, 0, 4, 0, 4, 5, 0, 0, 0 };
		int[] res2 = { 1, 2, 4, 3, 5 };
		graph = new Graph(test2);
		var list2 = graph.getDFS(1);
		sameArray(res2, list2);

		int[] test3 = { 2, 0, 0, 4, 0, 5, 0 };
		int[] res3 = { 1, 2 };
		graph = new Graph(test3);
		var list3 = graph.getDFS(1);
		sameArray(res3, list3);

		int[] test4 = { 2, 3, 0, 4, 0, 2, 5, 0, 1, 0, 6, 0, 0 };
		int[] res4 = { 1, 2, 4, 3, 5, 6 };
		graph = new Graph(test4);
		var list4 = graph.getDFS(1);
		sameArray(res4, list4);
	}

	@Test
	public void testBFS() {
		int[] test1 = { 2, 0, 3, 0, 0 };
		int[] res1 = { 1, 2, 3 };
		graph = new Graph(test1);
		var list1 = graph.getBFS(1);
		sameArray(res1, list1);

		int[] test2 = { 2, 3, 0, 4, 0, 4, 5, 0, 0, 0 };
		int[] res2 = { 1, 2, 3, 4, 5 };
		graph = new Graph(test2);
		var list2 = graph.getBFS(1);
		sameArray(res2, list2);

		int[] test3 = { 2, 0, 3, 0, 1, 0 };
		int[] res3 = { 1, 2, 3 };
		graph = new Graph(test3);
		var list3 = graph.getBFS(1);
		sameArray(res3, list3);

		int[] test4 = { 2, 0, 0, 4, 0, 5, 0 };
		int[] res4 = { 1, 2 };
		graph = new Graph(test4);
		var list4 = graph.getBFS(1);
		sameArray(res4, list4);
	}
}
