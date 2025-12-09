package m1maxflow2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import m1graphs2025.Edge;
import m1graphs2025.Graph;
import m1graphs2025.Node;

/**
 * Represents a Flow Network, extending the generic Graph class.
 * It supports edges with capacity and flow.
 */
public class FlowNetwork extends Graph {

	/**
	 * Adds an edge with a weight, interpreted as capacity with 0 initial flow.
	 *
	 * @param fromId ID of the source node
	 * @param toId   ID of the destination node
	 * @param weight The capacity of the edge
	 */
	@Override
	public void addEdge(int fromId, int toId, Integer weight) {
		addEdge(fromId, toId, weight, 0);
	}

	/**
	 * Adds an edge with specific capacity and flow.
	 *
	 * @param fromId   ID of the source node
	 * @param toId     ID of the destination node
	 * @param capacity The capacity of the edge
	 * @param flow     The current flow of the edge
	 * @throws NullPointerException if capacity or flow is null
	 */
	public void addEdge(int fromId, int toId, Integer capacity, Integer flow) {
		if (capacity == null || flow == null) {
			throw new NullPointerException("Parameters cannot be null");
		}
		addNodeIfAbsent(fromId);
		addNodeIfAbsent(toId);

		Node from = getNode(fromId);
		Node to = getNode(toId);

		adjEdList.get(from).add(new FlowEdge(from, to, this, capacity, flow));
	}

	/**
	 * Updates the flow along a given path by a delta value.
	 * It adds flow to forward edges and subtracts flow from backward edges.
	 *
	 * @param path  The list of nodes representing the path
	 * @param delta The amount of flow to add
	 */
	public void addFlow(List<Node> path, int delta) {
		for (int i = 0; i < path.size() - 1; i++) {
			Node u = path.get(i);
			Node v = path.get(i + 1);

			FlowEdge forward = getEdge(u, v);
			FlowEdge backward = getEdge(v, u);

			if (forward != null) {
				forward.addFlow(delta);
			} else if (backward != null) {
				backward.addFlow(-delta);
			}
		}
	}

	/**
	 * Retrieves the flow edge between two nodes.
	 *
	 * @param u The source node
	 * @param v The destination node
	 * @return The FlowEdge if it exists and is unique, null otherwise
	 */
	private FlowEdge getEdge(Node u, Node v) {
		List<Edge> edges = getEdges(u, v);
		return edges.size() == 1 ? (FlowEdge) edges.get(0) : null;
	}

	/**
	 * Calculates the total flow coming out of the source (smallest node ID).
	 *
	 * @return The total flow value
	 */
	private int flowValue() {
		int s = smallestNodeId();
		int total = 0;
		for (Edge edge : getOutEdges(s)) {
			total += ((FlowEdge) edge).getFlow();
		}
		return total;
	}

	/**
	 * Generates a DOT representation of the flow network.
	 * Includes the total flow value in the label and edge attributes for flow/capacity.
	 *
	 * @return A string containing the DOT representation
	 */
	@Override
	public String toDotString() {
		StringBuilder sb = new StringBuilder();

		sb.append("digraph FlowNetwork {\n")
				.append("\trankdir=\"LR\";\n")
				.append("\tlabel=\"Value: ").append(flowValue()).append("\";\n");

		getAllEdges().stream().sorted().forEach(e -> {
			FlowEdge f = (FlowEdge) e;

			sb.append("\t")
					.append(f.from()).append(" -> ").append(f.to())
					.append(" [label=\"");

			sb.append(f.getFlow() != 0 ? f.getFlow() + "/" + f.getWeight()
					: f.getWeight());

			sb.append("\", len=").append(f.getWeight()).append("];\n");
		});

		sb.append("}\n");
		return sb.toString();
	}

	/**
	 * Creates a FlowNetwork from a DOT file (.gv extension by default).
	 *
	 * @param filename name of the file without extension
	 * @return the imported FlowNetwork, or null if reading fails
	 */
	public static FlowNetwork fromDotFile(String filename) {
		return fromDotFile(filename, ".gv");
	}

	/**
	 * Creates a FlowNetwork from a DOT file with a specific extension.
	 * It parses labels to extract flow and capacity (e.g., label="flow/capacity" or label="capacity").
	 *
	 * @param filename  name of the file without extension
	 * @param extension the file extension (e.g., ".gv")
	 * @return the imported FlowNetwork, or null if reading fails
	 */
	public static FlowNetwork fromDotFile(String filename, String extension) {
		FlowNetwork graph = new FlowNetwork();

		if (!extension.startsWith(".")) {
			extension = "." + extension;
		}

		String filepath = filename + extension;

		// Regex to parse edge: src -> dst [attributes]
		Pattern edgePattern = Pattern.compile("^\\s*(\\w+)\\s*(?:->|--)\\s*(\\w+)(?:\\s*\\[(.*)\\])?");
		// Regex to parse label: label="6/8" (flow/cap) or label="8" (cap)
		Pattern labelPattern = Pattern.compile("label\\s*=\s*\"?(\\d+)(?:/(\\d+))?\"?");

		List<String> lines = new ArrayList<>();
		Set<String> nodeNames = new HashSet<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#") || line.startsWith("{") || line.startsWith("}"))
					continue;
				
				
				Matcher matcher = edgePattern.matcher(line);
				if (matcher.find()) {
					nodeNames.add(matcher.group(1));
					nodeNames.add(matcher.group(2));
					lines.add(line); 
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + filepath + " " + e.getMessage());
			return null;
		}

		Map<String, Integer> nameToId = new HashMap<>();
		Set<Integer> usedIds = new HashSet<>();

		for (String name : nodeNames) {
			if (name.matches("\\d+")) {
				int id = Integer.parseInt(name);
				nameToId.put(name, id);
				usedIds.add(id);
			}
		}

		int nextId = 1;
		for (String name : nodeNames) {
			if (!nameToId.containsKey(name)) {
				while (usedIds.contains(nextId)) {
					nextId++;
				}
				nameToId.put(name, nextId);
				usedIds.add(nextId);
			}
		}

		for (Map.Entry<String, Integer> entry : nameToId.entrySet()) {
			Node n = new Node(entry.getValue(), entry.getKey(), graph);
			graph.adjEdList.put(n, new ArrayList<>());
		}

		for (String line : lines) {
			Matcher matcher = edgePattern.matcher(line);
			if (matcher.find()) {
				String fromName = matcher.group(1);
				String toName = matcher.group(2);
				String attributes = matcher.group(3);

				int fromId = nameToId.get(fromName);
				int toId = nameToId.get(toName);

				Integer capacity = 0;
				Integer flow = 0;

				if (attributes != null) {
					Matcher labelMatcher = labelPattern.matcher(attributes);
					if (labelMatcher.find()) {
						String p1 = labelMatcher.group(1);
						String p2 = labelMatcher.group(2);

						if (p2 != null) {
							flow = Integer.parseInt(p1);
							capacity = Integer.parseInt(p2);
						} else {
							capacity = Integer.parseInt(p1);
							flow = 0;
						}
					}
				}
				
				graph.addEdge(fromId, toId, capacity, flow);
			}
		}

		return graph;
	}

}
