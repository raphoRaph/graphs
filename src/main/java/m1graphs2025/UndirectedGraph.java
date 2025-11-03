package m1graphs2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UndirectedGraph extends Graph {
	public UndirectedGraph(int... nodes) {
		super(nodes);
	}

	public UndirectedGraph() {
		super();
	}

	@Override
	public boolean existsEdge(Edge e) {
		return existsEdge(e);
	}

	@Override
	public boolean existsEdge(Node u, Node v) {
		return existsEdge(u, v);
	}

	@Override
	public boolean existsEdge(int uId, int vId) {
		return super.existsEdge(uId, vId) && super.existsEdge(vId, uId);
	}

	@Override
	public List<Node> getSuccessors(Node n) {
		// TODO Auto-generated method stub
		return super.getSuccessors(n);
	}

	@Override
	public List<Node> getSuccessorsMulti(Node n) {
		// TODO Auto-generated method stub
		return super.getSuccessorsMulti(n);
	}

	@Override
	public List<Edge> getInEdges(int nodeId) {
		ArrayList<Edge> list = new ArrayList<>();
		for (Edge edge : getAllEdges()) {
			if (edge.to().getId() == nodeId) {
				list.add(edge.getSymetric());
			} else if (edge.from().getId() == nodeId) {
				list.add(edge);
			}
		}
		return list;
	}

	@Override
	public List<Edge> getOutEdges(int nodeId) {
		return getInEdges(nodeId);
	}

	@Override
	public int inDegree(int nodeId) {
		int nb = 0;
		for (Edge edge : getAllEdges()) {
			if (edge.from().getId() == nodeId || edge.to().getId() == nodeId) {
				nb++;
				if (edge.from().getId() == edge.to().getId()) {
					nb++;
				}
			}
		}
		return nb;
	}

	@Override
	public int outDegree(int nodeId) {
		return inDegree(nodeId);
	}

	@Override
	public int degree(int nodeId) {
		return inDegree(nodeId);
	}

	@Override
	public List<Edge> getIncidentEdges(int nodeId) {
		return getInEdges(nodeId);
	}

	@Override
	public UndirectedGraph getTransitiveClosure() {
		Graph graph = super.getTransitiveClosure();
		return new UndirectedGraph(graph.toSuccessorArray());
	}

	@Override
	public UndirectedGraph getReverse() {
		return this;
	}

	@Override
	public int[][] toAdjMatrix() {
		int maxNode = largestNodeId();
		int[][] matrix = new int[maxNode][maxNode];

		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			int fromId = pair.getKey().getId();
			for (Edge edge : getOutEdges(pair.getKey())) {
				int toId = edge.to().getId();
				matrix[fromId - 1][toId - 1]++;
			}
		}
		return matrix;
	}

	@Override
	public String toDotString() {
		return super.toDotString(false);
	}

	public static UndirectedGraph fromDotFile(String filename) {
		return fromDotFile(filename, ".gv");
	}

	// NOTE: don't take the node all but only edges
	public static UndirectedGraph fromDotFile(String filename, String extension) {
		Graph graph = Graph.fromDotFile(filename, extension);
		UndirectedGraph undirectedGraph = new UndirectedGraph();
		for (Edge edge : graph.getAllEdges()) {
			undirectedGraph.addEdge(edge.from(), edge.to(), edge.getWeight());
		}
		return undirectedGraph;
	}
}
