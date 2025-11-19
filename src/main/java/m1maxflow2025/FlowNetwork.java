package m1maxflow2025;

import java.util.List;

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
}
