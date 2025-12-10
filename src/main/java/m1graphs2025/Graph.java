package m1graphs2025;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class represents a directed graph
 * 
 * The graph is made of an HashMap of Node from wich the edge will start,
 * and a list of Edge to know every Node in wich it can go.
 * 
 * @see Node
 * @see Edge
 */
public class Graph {
	protected final Map<Node, List<Edge>> adjEdList;

	/**
	 * Constructs an unweighted Graph.
	 *
	 * This constructor creates a directed graph from a successor array.
	 * If the node has no edges, he will not be added
	 *
	 * @param nodes multiple integers representing the different nodes
	 *              (given in the form of a Successor Array)
	 */
	public Graph(int... nodes) {
		adjEdList = new HashMap<>();
		int idFrom = 1;
		for (int idTo : nodes) {
			if (idTo == 0) {
				idFrom++;
			} else {
				addNodeIfAbsent(idFrom);
				addNodeIfAbsent(idTo);
				addEdge(idFrom, idTo);
			}
		}
	}

	/**
	 * Constructs an empty Graph.
	 *
	 * Creates an empty adjacency list with no nodes or edges.
	 */
	public Graph() {
		adjEdList = new HashMap<>();
	}

	// Node-related methods //

	/**
	 * Creates a String representation of the Graph.
	 *
	 * Displays each node with all its successors.
	 *
	 * @return a String containing all nodes and their outgoing edges
	 */
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

	/**
	 * Returns the number of nodes in the Graph.
	 *
	 * @return the number of nodes in the adjacency list
	 */
	public int nbNodes() {
		return adjEdList.size();
	}

	/**
	 * Checks whether the graph already uses a given Node.
	 *
	 * @param n the Node to check
	 * @return true if the Node exists in the graph, false otherwise
	 */
	public boolean usesNode(Node n) {
		return usesNode(n.getId());
	}

	/**
	 * Checks whether the graph already uses a node with the given ID.
	 *
	 * @param nodeId the ID of the node
	 * @return true if the node exists in the graph, false otherwise
	 */
	public boolean usesNode(int nodeId) {
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			if (pair.getKey().getId() == nodeId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the given node belongs to this graph.
	 *
	 * @param n the Node to verify
	 * @return true if the node belongs to this graph, false otherwise
	 */
	public boolean holdsNode(Node n) {
		return n.getGraph() == this;
	}

	/**
	 * Retrieves a node from its ID.
	 *
	 * @param id the ID of the node to retrieve
	 * @return the Node object if found, null otherwise
	 */
	public Node getNode(int id) {
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			if (pair.getKey().getId() == id) {
				return pair.getKey();
			}
		}
		return null;
	}

	/**
	 * Adds a node to the graph.
	 *
	 * @param n the Node to add
	 * @return true if the node was added, false if it already exists
	 */
	public boolean addNode(Node n) {
		return addNode(n.getId());
	}

	/**
	 * Adds a node to the graph using its ID.
	 *
	 * @param nodeId the ID of the node to add
	 * @return true if the node was successfully added, false if it already exists
	 */
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

	/**
	 * Adds a node only if it does not already exist.
	 *
	 * Helper for internal use.
	 *
	 * @param nodeId the ID of the node to add
	 */
	protected void addNodeIfAbsent(int nodeId) {
		if (!usesNode(nodeId))
			addNode(nodeId);
	}

	protected void addNodeIfAbsent(int nodeId, String name) {
		if (!usesNode(nodeId)) {
			adjEdList.put(new Node(nodeId, name, this), new ArrayList<>());
		}
	}

	/**
	 * Removes a given Node from the graph.
	 *
	 * @param n the Node to remove
	 * @return true if the node was found and removed, false otherwise
	 */
	public boolean removeNode(Node n) {
		return removeNode(n.getId());
	}

	/**
	 * Removes a node by its ID from the graph.
	 *
	 * @param nodeId the ID of the node to remove
	 * @return true if the node was found and removed, false otherwise
	 */
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

	/**
	 * Retrieves all nodes from the graph.
	 *
	 * @return a List containing all Node objects in the graph
	 */
	public List<Node> getAllNodes() {
		List<Node> nodeList = new ArrayList<>();
		nodeList.addAll(adjEdList.keySet());
		return nodeList;
	}

	/**
	 * Returns the largest node ID in the graph.
	 *
	 * @return the maximum node ID value
	 */
	public int largestNodeId() {
		int largest = 0;
		for (Node node : adjEdList.keySet()) {
			largest = Math.max(largest, node.getId());
		}
		return largest;
	}

	/**
	 * Returns the smallest node ID in the graph.
	 *
	 * @return the minimum node ID value
	 */
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

	/**
	 * Gets all successor nodes of a given node.
	 *
	 * @param n the Node whose successors are requested
	 * @return a List of successor nodes without duplicates
	 */
	public List<Node> getSuccessors(Node n) {
		return new ArrayList<>(getOutEdges(n).stream().map(Edge::to).distinct().toList());
	}

	/**
	 * Gets all successor nodes of a given node, including duplicates.
	 *
	 * @param n the Node whose successors are requested
	 * @return a List of successor nodes including duplicates
	 */
	public List<Node> getSuccessorsMulti(Node n) {
		return new ArrayList<>(getOutEdges(n).stream().map(Edge::to).collect(Collectors.toList()));
	}

	/**
	 * Checks whether two nodes are adjacent.
	 *
	 * @param u the first Node
	 * @param v the second Node
	 * @return true if there is an edge from u to v
	 */
	public boolean adjacent(Node u, Node v) {
		if (u == null || v == null) {
			throw new NullPointerException("Node must be initialize");
		}
		return adjacent(u.getId(), v.getId());
	}

	/**
	 * Checks whether two node IDs are adjacent.
	 *
	 * @param uId the source node ID
	 * @param vId the destination node ID
	 * @return true if there is an edge from uId to vId
	 */
	public boolean adjacent(int uId, int vId) {
		if (uId <= 0 || vId <= 0) {
			throw new IllegalArgumentException("Node ID must be higher than 0");
		}
		return getSuccessors(getNode(uId)).stream().anyMatch(n -> n.getId() == vId);
	}

	/**
	 * Computes the in-degree of a given node.
	 *
	 * @param n the Node
	 * @return the number of incoming edges
	 */
	public int inDegree(Node n) {
		return getInEdges(n).size();
	}

	/**
	 * Computes the in-degree of a node by its ID.
	 *
	 * @param nodeId the ID of the node
	 * @return the number of incoming edges
	 */
	public int inDegree(int nodeId) {
		return inDegree(getNode(nodeId));
	}

	/**
	 * Computes the out-degree of a given node.
	 *
	 * @param n the Node
	 * @return the number of outgoing edges
	 */
	public int outDegree(Node n) {
		return getOutEdges(n).size();
	}

	/**
	 * Computes the out-degree of a node by its ID.
	 *
	 * @param nodeId the ID of the node
	 * @return the number of outgoing edges
	 */
	public int outDegree(int nodeId) {
		return outDegree(getNode(nodeId));
	}

	/**
	 * Returns the total degree (in + out) of a given node.
	 *
	 * @param n the Node
	 * @return the total number of connected edges
	 */
	public int degree(Node n) {
		return inDegree(n) + outDegree(n);
	}

	/**
	 * Returns the total degree (in + out) of a node by its ID.
	 *
	 * @param nodeId the ID of the node
	 * @return the total number of connected edges
	 */
	public int degree(int nodeId) {
		return degree(getNode(nodeId));
	}

	// Edge-related methods //

	/**
	 * Retrieves an edge between two nodes.
	 *
	 * @param fromId the ID of the source node
	 * @param toId   the ID of the destination node
	 * @return the corresponding Edge object if it exists, null otherwise
	 */
	private Edge getEdge(int fromId, int toId) {
		for (Edge edge : adjEdList.get(getNode(fromId))) {
			if (edge.from().getId() == fromId && edge.to().getId() == toId) {
				return edge;
			}
		}
		return null;
	}

	/**
	 * Counts the total number of edges in the graph.
	 *
	 * @return the number of edges
	 */
	public int nbEdges() {
		int count = 0;
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			count += pair.getValue().size();
		}
		return count;
	}

	/**
	 * Checks whether an edge already exists in the graph.
	 *
	 * @param e the Edge to check
	 * @return true if the edge exists, false otherwise
	 */
	public boolean existsEdge(Edge e) {
		return existsEdge(e.from(), e.to());
	}

	/**
	 * Checks whether an edge exists between two nodes.
	 *
	 * @param u the source Node
	 * @param v the destination Node
	 * @return true if there is an edge between u and v
	 */
	public boolean existsEdge(Node u, Node v) {
		return existsEdge(u.getId(), v.getId());
	}

	/**
	 * Checks whether an edge exists between two nodes using their IDs.
	 *
	 * @param uId the ID of the source node
	 * @param vId the ID of the destination node
	 * @return true if there is an edge between uId and vId
	 */
	public boolean existsEdge(int uId, int vId) {
		if (!usesNode(uId) || !usesNode(vId)) {
			return false;
		}

		for (Edge edge : getOutEdges(uId)) {
			if (edge.to() == getNode(vId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether there are multiple edges between two nodes.
	 *
	 * @param e the Edge to check
	 * @return true if multiple edges exist between the same nodes
	 */
	public boolean isMultiEdge(Edge e) {
		return isMultiEdge(e.from(), e.to());
	}

	/**
	 * Checks whether multiple edges exist between two nodes.
	 *
	 * @param u the source Node
	 * @param v the destination Node
	 * @return true if multiple edges exist
	 */
	public boolean isMultiEdge(Node u, Node v) {
		return isMultiEdge(u.getId(), v.getId());
	}

	/**
	 * Checks whether multiple edges exist between two node IDs.
	 *
	 * @param uId the ID of the source node
	 * @param vId the ID of the destination node
	 * @return true if multiple edges exist
	 */
	public boolean isMultiEdge(int uId, int vId) {
		int count = 0;
		for (Edge edge : getOutEdges(uId)) {
			if (edge.to() == getNode(vId)) {
				count++;
			}
		}
		return count >= 2;
	}

	/**
	 * Adds a weighted edge between two nodes.
	 * 
	 * To make this method easier to use, nodes that are not already in the graph
	 * will be added automatically.
	 *
	 * @param fromId ID of the source node
	 * @param toId   ID of the destination node
	 * @param weight the weight of the edge
	 */
	protected void addEdge(int fromId, int toId, Integer weight) {
		addNodeIfAbsent(fromId);
		addNodeIfAbsent(toId);
		Node from = getNode(fromId);
		Node to = getNode(toId);

		adjEdList.get(from).add(new Edge(from, to, this, weight));
	}

	/**
	 * Adds an existing Edge object to the graph.
	 *
	 * @param e the Edge to add
	 */
	public void addEdge(Edge e) {
		addEdge(e.from().getId(), e.to().getId(), e.getWeight());
	}

	/**
	 * Adds an edge between two nodes.
	 *
	 * @param from the source Node
	 * @param to   the destination Node
	 */
	public void addEdge(Node from, Node to) {
		addEdge(from.getId(), to.getId());
	}

	/**
	 * Adds a weighted edge between two nodes.
	 *
	 * @param from   the source Node
	 * @param to     the destination Node
	 * @param weight the weight of the edge
	 */
	public void addEdge(Node from, Node to, Integer weight) {
		addEdge(from.getId(), to.getId(), weight);
	}

	/**
	 * Adds an unweighted edge between two node IDs.
	 *
	 * @param fromId ID of the source node
	 * @param toId   ID of the destination node
	 */
	public void addEdge(int fromId, int toId) {
		addEdge(fromId, toId, null);
	}

	/**
	 * Removes a specific edge between two nodes with the given weight.
	 * 
	 * In the case of a multiple edge, all the edges will be removed.
	 *
	 * @param fromId source node ID
	 * @param toId   destination node ID
	 * @param weight edge weight to match
	 * @return true if the edge was removed, false otherwise
	 */
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

	/**
	 * Removes a specific Edge object from the graph.
	 *
	 * @param e the Edge to remove
	 * @return true if removed successfully
	 */
	public boolean removeEdge(Edge e) {
		return removeEdge(e.from(), e.to(), e.getWeight());
	}

	/**
	 * Removes all edges between two nodes.
	 *
	 * @param from source Node
	 * @param to   destination Node
	 * @return true if any edge was removed
	 */
	public boolean removeEdge(Node from, Node to) {
		return removeEdge(from.getId(), to.getId());
	}

	/**
	 * Removes a weighted edge between two nodes.
	 *
	 * @param from   source Node
	 * @param to     destination Node
	 * @param weight weight of the edge to remove
	 * @return true if the edge was removed
	 */
	public boolean removeEdge(Node from, Node to, Integer weight) {
		return removeEdge(from.getId(), to.getId(), weight);
	}

	/**
	 * Removes an edge between two node IDs.
	 *
	 * @param fromId source node ID
	 * @param toId   destination node ID
	 * @return true if the edge was removed
	 */
	public boolean removeEdge(int fromId, int toId) {
		if (fromId < 0 || toId < 0) {
			return false;
		}
		if (!usesNode(fromId) || !usesNode(toId)) {
			return false;
		}

		Node from = getNode(fromId);
		return adjEdList.get(from).remove(getEdge(fromId, toId));
	}

	/**
	 * Returns all incoming edges of a given node.
	 *
	 * @param n the Node
	 * @return list of incoming edges
	 */
	public List<Edge> getInEdges(Node n) {
		return getInEdges(n.getId());
	}

	/**
	 * Returns all incoming edges for a node ID.
	 *
	 * @param nodeId ID of the node
	 * @return list of incoming edges
	 */
	public List<Edge> getInEdges(int nodeId) {
		return getIn(nodeId);
	}

	protected List<Edge> getIn(int nodeId) {
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

	/**
	 * Returns all outgoing edges of a given node.
	 *
	 * @param n the Node
	 * @return list of outgoing edges
	 */
	public List<Edge> getOutEdges(Node n) {
		return getOutEdges(n.getId());
	}

	/**
	 * Returns all outgoing edges of a node by its ID.
	 *
	 * @param nodeId ID of the node
	 * @return list of outgoing edges
	 */
	public List<Edge> getOutEdges(int nodeId) {
		return getOut(nodeId);
	}

	protected List<Edge> getOut(int nodeId) {
		return adjEdList.get(getNode(nodeId));
	}

	/**
	 * Retrieves all edges between two nodes.
	 *
	 * @param u source Node
	 * @param v destination Node
	 * @return list of all edges connecting u and v
	 */
	public List<Edge> getEdges(Node u, Node v) {
		List<Edge> lst = new ArrayList<>();
		for (Edge edge : adjEdList.get(u)) {
			if (edge.from().equals(u) && edge.to().equals(v)) {
				lst.add(edge);
			}
		}
		return lst;
	}

	/**
	 * Retrieves all edges of the graph.
	 *
	 * @return list of all Edge objects in the graph
	 */
	public List<Edge> getAllEdges() {
		List<Edge> lst = new ArrayList<>();
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()) {
			for (Edge edge : pair.getValue()) {
				lst.add(edge);
			}
		}
		return lst;
	}

	/**
	 * Retrieves all edges incident to a given node.
	 *
	 * @param n the Node
	 * @return list of incident edges
	 */
	public List<Edge> getIncidentEdges(Node n) {
		return getIncidentEdges(n.getId());
	}

	/**
	 * Retrieves all edges incident to a node by its ID.
	 *
	 * @param nodeId ID of the node
	 * @return list of incident edges
	 */
	public List<Edge> getIncidentEdges(int nodeId) {
		List<Edge> lst = getOutEdges(nodeId);
		lst.addAll(getInEdges(nodeId));
		return lst;
	}

	// Graph Representations and Transformations //

	/**
	 * Converts a list of Integers to an array of int.
	 *
	 * @param integers list of Integer objects
	 * @return an array of int
	 */
	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	/**
	 * Converts the graph to a Successor Array representation.
	 *
	 * @return an array of integers representing the Successor Array
	 */
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

	/**
	 * Converts the graph to an adjacency matrix.
	 *
	 * @return a 2D integer array representing adjacency
	 */
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

	/**
	 * Returns the reverse (transposed) version of the graph.
	 *
	 * @return a new Graph with all edges reversed
	 */
	public Graph getReverse() {
		Graph graph = new Graph();
		for (Edge edge : getAllEdges()) {
			graph.addEdge(edge.getSymetric());
		}
		return graph;
	}

	/**
	 * Computes the transitive closure of the graph.
	 *
	 * @return a new Graph where edges represent reachability
	 */
	public Graph getTransitiveClosure() {
		Graph closure = new Graph();

		for (Node from : adjEdList.keySet()) {
			for (Edge edge : adjEdList.get(from)) {
				if (!edge.isSelfLoop() && !closure.existsEdge(edge.from(), edge.to())) {
					closure.addEdge(from, edge.to());
				}
			}
		}

		List<Node> nodes = new ArrayList<>(adjEdList.keySet());
		for (Node u : nodes) {
			for (Node p : nodes) {
				if (closure.existsEdge(p, u)) {
					for (Node s : nodes) {
						if (!closure.existsEdge(p, s) && closure.existsEdge(u, s) && !p.equals(s)) {
							closure.addEdge(p, s);
						}
					}
				}
			}
		}
		return closure;
	}

	/**
	 * Checks if the graph is a multigraph (contains multiple edges between nodes).
	 *
	 * @return true if it is a multigraph
	 */
	public boolean isMultiGraph() {
		for (Edge edge : getAllEdges()) {
			if (edge.isMultiEdge()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the graph is simple (no multiple edges).
	 *
	 * @return true if the graph is simple
	 */
	public boolean isSimpleGraph() {
		return !isMultiGraph();
	}

	/**
	 * Checks if the graph contains self-loops.
	 *
	 * @return true if any node has an edge to itself
	 */
	public boolean hasSelfLoops() {
		for (Edge edge : getAllEdges()) {
			if (edge.isSelfLoop()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates a deep copy of the graph.
	 *
	 * @return a new Graph identical to the current one
	 */
	public Graph copy() {
		Graph graph = new Graph();
		for (Edge edge : getAllEdges()) {
			graph.addEdge(edge.from(), edge.to(), edge.getWeight());
		}
		return graph;
	}

	// Graph Traversal //

	/**
	 * Performs a Depth-First Search from the smallest node ID.
	 *
	 * @return list of nodes visited in DFS order
	 */
	public List<Node> getDFS() {
		return getDFS(smallestNodeId());
	}

	/**
	 * Performs a Depth-First Search starting from a specific Node.
	 *
	 * @param u starting Node
	 * @return list of nodes visited in DFS order
	 */
	public List<Node> getDFS(Node u) {
		return getDFS(u.getId());
	}

	/**
	 * Performs a Depth-First Search starting from a specific node ID.
	 *
	 * @param id ID of the starting node
	 * @return list of nodes visited in DFS order
	 */
	public List<Node> getDFS(int id) {
		List<Node> lst = new ArrayList<>();
		Set<Node> visited = new HashSet<>();

		Node start = getNode(id);
		if (start != null) {
			getDFS(start, lst, visited);
		}

		for (Node node : getAllNodes()) {
			if (!visited.contains(node)) {
				getDFS(node, lst, visited);
			}
		}

		return lst;
	}

	/**
	 * Recursive helper for DFS traversal.
	 *
	 * @param node    current Node
	 * @param lst     list of visited nodes
	 * @param visited set of already visited nodes
	 */
	private void getDFS(Node node, List<Node> lst, Set<Node> visited) {
		visited.add(node);
		lst.add(node);

		for (Node next : getSuccessors(node)) {
			if (!visited.contains(next)) {
				getDFS(next, lst, visited);
			}
		}
	}

	/**
	 * Performs a Breadth-First Search from the smallest node ID.
	 *
	 * @return list of nodes visited in BFS order
	 */
	public List<Node> getBFS() {
		return getBFS(smallestNodeId());
	}

	/**
	 * Performs a Breadth-First Search starting from a specific Node.
	 *
	 * @param u starting Node
	 * @return list of nodes visited in BFS order
	 */
	public List<Node> getBFS(Node u) {
		return getBFS(u.getId());
	}

	/**
	 * Performs a Breadth-First Search starting from a node ID.
	 *
	 * @param id ID of the starting node
	 * @return list of nodes visited in BFS order
	 */
	public List<Node> getBFS(int id) {
		List<Node> lst = new ArrayList<>();
		Set<Node> visited = new HashSet<>();
		Node start = getNode(id);
		if (start == null)
			return lst;

		visited.add(start);

		subGetBFS(start, lst, visited);
		for (Node node : getAllNodes()) {
			if (!visited.contains(node)) {
				subGetBFS(node, lst, visited);
			}
		}
		return lst;
	}

	private void subGetBFS(Node start, List<Node> lst, Set<Node> visited) {
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
	}

	/**
	 * Performs a Depth-First Search while tracking visit information for nodes and
	 * edges.
	 *
	 * @param nodeVisit map of node visit info
	 * @param edgeVisit map of edge visit types
	 * @return list of nodes visited in DFS order
	 */
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

	/**
	 * Performs DFS with visit information starting from a specific node.
	 *
	 * @param u         starting Node
	 * @param nodeVisit map of node visit info
	 * @param edgeVisit map of edge visit types
	 * @return list of visited nodes
	 */
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

	/**
	 * Recursive helper for DFS with visit information.
	 *
	 * @param time      current timestamp counter
	 * @param u         current Node
	 * @param nodeVisit map of node visit info
	 * @param edgeVisit map of edge visit types
	 * @param finalList final list of nodes in visit order
	 * @return updated time counter
	 */
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
			} else if (currInfo.getColor() == colour.GRAY) {
				edgeVisit.put(getEdge(u.getId(), node.getId()), EdgeVisitType.BACKWARD);
			} else if (currInfo.getColor() == colour.BLACK) {
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

	/**
	 * Creates a Graph from a DOT file (.gv extension by default).
	 *
	 * @param filename name of the file without extension
	 * @return the imported Graph, or null if reading fails
	 */
	public static Graph fromDotFile(String filename) {
		return fromDotFile(filename, ".gv");
	}

	/**
	 * Creates a Graph from a DOT file with a custom extension.
	 *
	 * @param filename  file name without extension
	 * @param extension extension of the DOT file
	 * @return the imported Graph, or null if reading fails
	 */
	public static Graph fromDotFile(String filename, String extension) {
		Graph graph = new Graph();

		if (!extension.startsWith(".")) {
			extension = "." + extension;
		}

		String filepath = filename + extension;

		Pattern edgePattern = Pattern.compile(
				"(\\d+)\\s*(--|->)\\s*(\\d+)(?:\\s*\\[label=(\\d+(?:\\.\\d+)?), len=(\\d+(?:\\.\\d+)?)\\])?");

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line;

			while ((line = br.readLine()) != null) {
				line = line.trim();

				if (line.isEmpty() || line.startsWith("#") || line.startsWith("{") || line.startsWith("}"))
					continue;

				Matcher matcher = edgePattern.matcher(line);
				if (matcher.find()) {
					int from = Integer.parseInt(matcher.group(1));
					int to = Integer.parseInt(matcher.group(3));

					if (matcher.group(4) != null) {
						Integer weight = Integer.valueOf(matcher.group(4));
						graph.addEdge(from, to, weight);
					} else {
						graph.addEdge(from, to);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + filepath);
			return null;
		}

		return graph;
	}

	/**
	 * Converts the graph to a DOT format string.
	 *
	 * @return a String in DOT format representing the graph
	 */
	public String toDotString() {
		return toDotString(true);
	}

	/**
	 * Converts the graph to a DOT format string.
	 *
	 * @param directed a boolean to precise if the graph is directed
	 * @return a String in DOT format representing the graph
	 */
	protected String toDotString(boolean directed) {
		String connector = directed ? "->" : "--";
		StringBuilder sb = new StringBuilder();

		sb.append(directed ? "digraph {\n" : "graph {\n").append("\trankdir=LR\n");

		ArrayList<Edge> lst = (ArrayList) getAllEdges();
		lst.sort(null);
		for (Edge edge : lst) {
			sb.append("\t").append(edge.from()).append(" ").append(connector).append(" ").append(edge.to());
			if (edge.isWeighted()) {
				sb.append(" [label=").append(edge.getWeight()).append(", len=").append(edge.getWeight()).append("]");
			}
			sb.append("\n");
		}

		for (Node node : getAllNodes()) {
			if (getInEdges(node).isEmpty() && getOutEdges(node).isEmpty()) {
				sb.append("\t").append(node).append("\n");
			}
		}
		sb.append("}\n");
		return sb.toString();
	}

	/**
	 * Exports the graph to a DOT file (.gv by default).
	 *
	 * @param fileName name of the output file without extension
	 */
	public void toDotFile(String fileName) {
		toDotFile(fileName, ".gv");
	}

	/**
	 * Exports the graph to a DOT file with a custom extension.
	 *
	 * @param fileName  name of the output file
	 * @param extension file extension (e.g., ".gv" or ".dot")
	 */
	public void toDotFile(String fileName, String extension) {
		if (extension == null || extension.isEmpty()) {
			extension = ".gv";
		} else if (!extension.startsWith(".")) {
			extension = "." + extension;
		}

		String filePath = fileName + extension;

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(this.toDotString());
		} catch (IOException e) {
			System.err.println("Error writing DOT file: " + filePath);
			e.printStackTrace();
		}
	}
}
