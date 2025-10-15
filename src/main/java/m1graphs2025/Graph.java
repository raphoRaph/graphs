package m1graphs2025;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.Collections;

public class Graph {
	private final Map<Node, List<Edge>> adjEdList;

	// create a graph through dedicated constructors (unweighted graph)
	public Graph(int... nodes) {
		adjEdList = new HashMap<>();
		int idFrom = 1;
		for (int idTo : nodes) {
			addNodeIfAbsent(idFrom);
			if (idTo == 0) {
				idFrom++;
			} else {
				addNodeIfAbsent(idTo);
				addEdge(idFrom, idTo);
			}
		}
	}

	public Graph() {
		adjEdList = new HashMap<>();
	}
	// Node part //

	@Override
	public String toString() {
		String res = "";
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			res += "\n" + pair.getKey().toString() + " : \n\t";
			for (Edge edge : adjEdList.get(pair.getKey())) {
				res += edge.to().toString() + ", ";
			}
		}
		return res;
	}

	public int nbNodes() {
		return adjEdList.size();
	}

	public boolean usesNode(Node n) {
		return usesNode(n.getId());
	}

	public boolean usesNode(int nodeId) {
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			if (pair.getKey().getId() == nodeId) {
				return true;
			}
		}
		return false;
	}

	public boolean holdsNode(Node n) {
		return n.getGraph() == this;
	}

	public Node getNode(int id) {
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			if (pair.getKey().getId() == id) {
				return pair.getKey();
			}
		}
		return null;
	}

	public boolean addNode(Node n) {
		return addNode(n.getId());
	}

	public boolean addNode(int nodeId) {
		if (nodeId <= 0) {
			throw new IllegalArgumentException("nodeId must be higher than 0");
		}
		if (usesNode(nodeId)) {
			return false;
		}
		adjEdList.put(new Node(nodeId, this), new ArrayList<>());
		return true;
	}

	private void addNodeIfAbsent(int nodeId) {
		if (!usesNode(nodeId))
			addNode(nodeId);
	}

	public boolean removeNode(Node n) {
		return removeNode(n.getId());
	}

	public boolean removeNode(int nodeId) {
		Node node = getNode(nodeId);
		if (node == null)
			return false;

		Iterator<Map.Entry<Node, List<Edge>>> it = adjEdList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Node, List<Edge>> entry = it.next();

			if (entry.getKey().getId() == nodeId) {
				it.remove(); // Safe removal using iterator
				continue;
			}

			Iterator<Edge> edgeIt = entry.getValue().iterator();
			while (edgeIt.hasNext()) {
				Edge edge = edgeIt.next();
				if (edge.from() == node || edge.to() == node) {
					edgeIt.remove();
				}
			}
		}
		return true;
	}

	public List<Node> getAllNodes() {
		List<Node> nodeList = new ArrayList<>();
		nodeList.addAll(adjEdList.keySet());
		return nodeList;
	}

	public int largestNodeId() {
		int largest = 0;
		for (Node node : adjEdList.keySet()) {
			largest = Math.max(largest, node.getId());
		}
		return largest;
	}

	public int smallestNodeId() {
		int smallest = -1;
		for (Node node : adjEdList.keySet()) {
			if (smallest == -1) {
				smallest = node.getId();
			}
			smallest = Math.min(smallest, node.getId());
		}
		return smallest;
	}

	public List<Node> getSuccessors(Node n) {
		return getOutEdges(n).stream().map(Edge::to).distinct().toList();
	}

	public List<Node> getSuccessorsMulti(Node n) {
		return getOutEdges(n).stream().map(Edge::to).collect(Collectors.toList());
	}

	public boolean adjacent(Node u, Node v) {
		if (u == null || v == null) {
			throw new NullPointerException("Node must be initialize");
		}
		return adjacent(u.getId(), v.getId());
	}

	public boolean adjacent(int uId, int vId) {
		if (uId <= 0 || vId <= 0) {
			throw new IllegalArgumentException("Node ID must be higher than 0");
		}
		return getSuccessors(getNode(uId)).stream().anyMatch(n -> n.getId() == vId);
	}

	public int inDegree(Node n) {
		return getInEdges(n).size();
	}

	public int inDegree(int nodeId) {
		return inDegree(getNode(nodeId));
	}

	public int outDegree(Node n) {
		return getOutEdges(n).size();
	}

	public int outDegree(int nodeId) {
		return outDegree(getNode(nodeId));
	}

	public int degree(Node n) {
		return inDegree(n) + outDegree(n);
	}

	public int degree(int nodeId) {
		return degree(getNode(nodeId));
	}

	// edge part //

	private Edge getEdge(int fromId, int toId) {
		for (Edge edge : adjEdList.get(getNode(fromId))) {
			if (edge.from().getId() == fromId && edge.to().getId() == toId) {
				return edge;
			}
		}
		return null;
	}

	public int nbEdges() {
		int count = 0;
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			count += pair.getValue().size();
		}
		return count;
	}

	public boolean existsEdge(Edge e) {
		List<Edge> list = adjEdList.get(e.from());
		for (Edge edge : list) {
			if (edge.equals(e)) {
				return true;
			}
		}
		return false;
	}

	public boolean existsEdge(Node u, Node v) {
		return existsEdge(u.getId(), v.getId());
	}

	public boolean existsEdge(int uId, int vId) {
		List<Edge> list = adjEdList.get(getNode(uId));
		for (Edge edge : list) {
			if (edge.to() == getNode(vId)) {
				return true;
			}
		}
		return false;
	}

	public boolean isMultiEdge(Edge e) {
		return isMultiEdge(e.from(), e.to());
	}

	public boolean isMultiEdge(Node u, Node v) {
		return isMultiEdge(u.getId(), v.getId());
	}

	public boolean isMultiEdge(int uId, int vId) {
		int count = 0;
		List<Edge> list = adjEdList.get(getNode(uId));
		for (Edge edge : list) {
			if (edge.to() == getNode(vId)) {
				count++;
			}
		}
		return count >= 2;
	}

	// TOOD why we add the if node is absent ?
	private void addEdge(int fromId, int toId, Integer weight) {
		addNodeIfAbsent(fromId);
		addNodeIfAbsent(toId);
		Node from = getNode(fromId);
		Node to = getNode(toId);

		adjEdList.get(from).add(new Edge(from, to, this, weight));
	}

	public void addEdge(Edge e) {
		addEdge(e.from().getId(), e.to().getId(), e.getWeight());
	}

	public void addEdge(Node from, Node to) {
		addEdge(from.getId(), to.getId());
	}

	public void addEdge(Node from, Node to, Integer weight) {
		addEdge(from.getId(), to.getId(), weight);
	}

	public void addEdge(int fromId, int toId) {
		addEdge(fromId, toId, null);
	}

	// why remove all edges ?
	public boolean removeEdge(int fromId, int toId, Integer weight) {
		Node from = getNode(fromId);
		List<Edge> lst = getEdges(from, getNode(toId));
		for (Edge edge : lst) {
			if (edge.getWeight() == weight) {
				return adjEdList.get(from).remove(edge);
			}
		}
		return false;
	}

	public boolean removeEdge(Edge e) {
		return removeEdge(e.from(), e.to(), e.getWeight());
	}

	public boolean removeEdge(Node from, Node to) {
		return removeEdge(from.getId(), to.getId());
	}

	public boolean removeEdge(Node from, Node to, Integer weight) {
		return removeEdge(from.getId(), to.getId(), weight);
	}

	public boolean removeEdge(int fromId, int toId) {
		Node from = getNode(fromId);
		return adjEdList.get(from).remove(getEdge(fromId, toId));
	}

	public List<Edge> getInEdges(int nodeId) {
		List<Edge> lst = new ArrayList<>();
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			for (Edge edge : pair.getValue()) {
				if (edge.to().getId() == nodeId) {
					lst.add(edge);
				}
			}
		}
		return lst;
	}

	public List<Edge> getOutEdges(Node n) {
		return getOutEdges(n.getId());
	}

	public List<Edge> getOutEdges(int nodeId) {
		return adjEdList.get(getNode(nodeId));
	}

	public List<Edge> getEdges(Node u, Node v) {
		List<Edge> lst = new ArrayList<>();
		for (Edge edge : adjEdList.get(u)) {
			if (edge.from() == u && edge.to() == v) {
				lst.add(edge);
			}
		}
		return lst;
	}

	public List<Edge> getAllEdges() {
		List<Edge> lst = new ArrayList<>();
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			for (Edge edge : pair.getValue()) {
				lst.add(edge);
			}
		}
		return lst;
	}

	public List<Edge> getInEdges(Node n) {
		return getInEdges(n.getId());
	}

	public List<Edge> getIncidentEdges(Node n) {
		return getIncidentEdges(n.getId());
	}

	public List<Edge> getIncidentEdges(int nodeId) {
		List<Edge> lst = getOutEdges(nodeId);
		for (Edge edge : getInEdges(nodeId)) {
			lst.add(edge);
		}
		return lst;
	}

	// graph's representations and transformations //

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	public int[] toSuccessorArray() {
		List<Integer> nodeIds = adjEdList.keySet().stream().map(Node::getId).sorted().toList();

		List<Integer> successors = new ArrayList<>();

		for (int nodeId : nodeIds) {
			List<Edge> edges = adjEdList.get(getNode(nodeId));

			if (edges != null && !edges.isEmpty()) {
				for (Edge edge : edges) {
					successors.add(edge.to().getId());
				}
			}
			successors.add(0);
		}

		return convertIntegers(successors);
	}

	public int[][] toAdjMatrix() {
		int maxNode = largestNodeId();
		int[][] matrix = new int[maxNode][maxNode];

		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			int fromId = pair.getKey().getId();
			for (Edge edge : adjEdList.get(pair.getKey())) {
				int toId = edge.to().getId();
				matrix[fromId - 1][toId - 1]++;
			}
		}
		return matrix;
	}

	public Graph getReverse() {
		Graph graph = new Graph();
		for (Edge edge : getAllEdges()) {
			graph.addEdge(edge.getSymetric());
		}
		return graph;
	}

	public Graph getTransitiveClosure() {
		Graph closure = copy(); // start with same nodes and edges
		List<Node> nodes = new ArrayList<>(adjEdList.keySet());

		for (Node k : nodes) {
			for (Node i : nodes) {
				for (Node j : nodes) {
					if (!closure.existsEdge(i, j) && closure.existsEdge(i, k) && closure.existsEdge(k, j)) {
						closure.addEdge(i, j);
					}
				}
			}
		}

		return closure;
	}

	public boolean isMultiGraph() {
		for (Edge edge : getAllEdges()) {
			if (edge.isMultiEdge()) {
				return true;
			}
		}
		return false;
	}

	public boolean isSimpleGraph() {
		return !isMultiGraph();
	}

	public boolean hasSelfLoops() {
		for (Edge edge : getAllEdges()) {
			if (edge.isSelfLoop()) {
				return true;
			}
		}
		return false;
	}

	public Graph copy() {
		Graph graph = new Graph();
		for (Edge edge : getAllEdges()) {
			graph.addEdge(edge.from(), edge.to(), edge.getWeight());
		}
		return graph;
	}

	// Graph traversal //
	public List<Node> getDFS() {
		return getDFS(smallestNodeId());
	}

	public List<Node> getDFS(Node u) {
		return getDFS(u.getId());
	}

	public List<Node> getDFS(int id) {
		List<Node> lst = new ArrayList<>();
		Set<Node> visited = new HashSet<>();

		Node start = getNode(id);
		if (start != null) {
			getDFS(start, lst, visited);
		}

		return lst;
	}

	private void getDFS(Node node, List<Node> lst, Set<Node> visited) {
		visited.add(node);
		lst.add(node);

		for (Node next : getSuccessors(node)) {
			if (!visited.contains(next)) {
				getDFS(next, lst, visited);
			}
		}
	}

	public List<Node> getBFS() {
		return getBFS(smallestNodeId());
	}

	public List<Node> getBFS(Node u) {
		return getBFS(u.getId());
	}

	public List<Node> getBFS(int id) {
		List<Node> lst = new ArrayList<>();
		Set<Node> visited = new HashSet<>();
		Node start = getNode(id);
		if (start == null)
			return lst;

		Queue<Node> queue = new LinkedList<>();
		queue.add(start);
		visited.add(start);

		while (!queue.isEmpty()) {
			Node current = queue.poll();
			lst.add(current);

			for (Node neighbor : getSuccessors(current)) {
				if (!visited.contains(neighbor)) {
					queue.add(neighbor);
					visited.add(neighbor);
				}
			}
		}

		return lst;
	}

	public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit) {
		for (Node node : getAllNodes()) {
			NodeVisitInfo info = new NodeVisitInfo();
			nodeVisit.put(node, info);
		}
		List<Node> finalList = new ArrayList<>();
		int time = 0;
		time = getDFSWithVisitInfo(time, getNode(smallestNodeId()), nodeVisit, edgeVisit, finalList);

		for (Node node : getAllNodes()) {
			if (nodeVisit.get(node).getColor() == colour.WHITE) {
				time = getDFSWithVisitInfo(time, node, nodeVisit, edgeVisit, finalList);
			}
		}

		return finalList;
	}

	public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit,
			Map<Edge, EdgeVisitType> edgeVisit) {
		for (Node node : getAllNodes()) {
			NodeVisitInfo info = new NodeVisitInfo();
			nodeVisit.put(node, info);
		}
		List<Node> finalList = new ArrayList<>();
		int time = 0;
		time = getDFSWithVisitInfo(time, u, nodeVisit, edgeVisit, finalList);

		for (Node node : getAllNodes()) {
			if (nodeVisit.get(node).getColor() == colour.WHITE) {
				time = getDFSWithVisitInfo(time, node, nodeVisit, edgeVisit, finalList);
			}
		}

		return finalList;
	}

	private int getDFSWithVisitInfo(int time, Node u, Map<Node, NodeVisitInfo> nodeVisit,
			Map<Edge, EdgeVisitType> edgeVisit, List<Node> finalList) {
		time++;
		NodeVisitInfo info = nodeVisit.get(u);
		info.setTimesTampDisc(time);
		info.setColor(colour.GRAY);
		for (Node node : u.getSuccessors()) {
			NodeVisitInfo currInfo = nodeVisit.get(node);
			if (currInfo.getColor() == colour.WHITE) {
				currInfo.setPredecessor(u);
				time = getDFSWithVisitInfo(time, node, nodeVisit, edgeVisit, finalList);
				edgeVisit.put(getEdge(u.getId(), node.getId()), EdgeVisitType.TREE);
			}
			if (currInfo.getColor() == colour.GRAY) {
				edgeVisit.put(getEdge(u.getId(), node.getId()), EdgeVisitType.BACKWARD);
			}
			if (currInfo.getColor() == colour.BLACK) {
				if (info.timestampDisc() < currInfo.timestampDisc()) {
					edgeVisit.put(getEdge(u.getId(), node.getId()), EdgeVisitType.FORWARD);
				} else {
					edgeVisit.put(getEdge(u.getId(), node.getId()), EdgeVisitType.CROSS);
				}
			}
		}
		info.setColor(colour.BLACK);
		finalList.add(u);
		time++;
		info.setTimestampFin(time);
		return time;
	}

	// Graph Import and Export //

	public static Graph fromDotFile(String filename) {
		return null;
	}

	public static Graph fromDotFile(String filename, String extension) {
		return null;
	}

	public String toDotString() {
		return "";
	}

	public void toDotFile(String fileName) {
	}

	public void toDotFile(String fileName, String extension) {
	}
}
