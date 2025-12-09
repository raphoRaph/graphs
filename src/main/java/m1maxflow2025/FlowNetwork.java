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

public class FlowNetwork extends Graph {

	@Override
	public void addEdge(int fromId, int toId, Integer weight) {
		addEdge(fromId, toId, weight, 0);
	}

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

	private FlowEdge getEdge(Node u, Node v) {
		List<Edge> edges = getEdges(u, v);
		return edges.size() == 1 ? (FlowEdge) edges.get(0) : null;
	}

	private int flowValue() {
		int s = smallestNodeId();
		int total = 0;
		for (Edge edge : getOutEdges(s)) {
			total += ((FlowEdge) edge).getFlow();
		}
		return total;
	}

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

	public static FlowNetwork fromDotFile(String filename) {
		return fromDotFile(filename, ".gv");
	}

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

		// 1. Read file and collect all node names
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#") || line.startsWith("{") || line.startsWith("}"))
					continue;
				
				// Handle graph label or other properties if necessary (ignored for now)
				
				Matcher matcher = edgePattern.matcher(line);
				if (matcher.find()) {
					nodeNames.add(matcher.group(1)); // Source
					nodeNames.add(matcher.group(2)); // Target
					lines.add(line); // Store valid edge line for second pass
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + filepath + " " + e.getMessage());
			return null;
		}

		// 2. Map names to IDs
		Map<String, Integer> nameToId = new HashMap<>();
		Set<Integer> usedIds = new HashSet<>();

		// First pass: Assign IDs to integer names
		for (String name : nodeNames) {
			if (name.matches("\\d+")) {
				int id = Integer.parseInt(name);
				nameToId.put(name, id);
				usedIds.add(id);
			}
		}

		// Second pass: Assign IDs to non-integer names
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

		// 3. Create Nodes with names and add to graph
		// We use the 'protected' access to adjEdList to insert named nodes, 
		// because standard addNode(int) creates nodes without names.
		for (Map.Entry<String, Integer> entry : nameToId.entrySet()) {
			Node n = new Node(entry.getValue(), entry.getKey(), graph);
			graph.adjEdList.put(n, new ArrayList<>());
		}

		// 4. Parse edges and add to graph
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
						String p1 = labelMatcher.group(1); // First number
						String p2 = labelMatcher.group(2); // Second number (optional)

						if (p2 != null) {
							// Format: flow/capacity
							flow = Integer.parseInt(p1);
							capacity = Integer.parseInt(p2);
						} else {
							// Format: capacity (flow is 0)
							capacity = Integer.parseInt(p1);
							flow = 0;
						}
					}
				}
				
				// Use addEdge with flow and capacity
				// Note: addEdge will call addNodeIfAbsent, which checks usesNode.
				// Since we manually added all nodes, it won't overwrite them.
				graph.addEdge(fromId, toId, capacity, flow);
			}
		}

		return graph;
	}

}
