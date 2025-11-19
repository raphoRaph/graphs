package m1maxflow2025;

import java.util.ArrayList;

import m1graphs2025.Graph;
import m1graphs2025.Node;

public class FlowNetwork extends Graph {

	@Override
	public void addEdge(int fromId, int toId, Integer weight) {
		addEdge(fromId, toId, weight, 0);
	}

	public void addEdge(int fromId, int toId, Integer weight, Integer flow) {
		if (weight == null || flow == null) {
			throw new NullPointerException("Parameters cannot be null");
		}
		addNodeIfAbsent(fromId);
		addNodeIfAbsent(toId);
		Node from = getNode(fromId);
		Node to = getNode(toId);

		adjEdList.get(from).add(new FlowEdge(from, to, this, weight, flow));
	}

	@Override
	public String toDotString() {
		StringBuilder sb = new StringBuilder();

		sb.append("digraph {\n").append("\trankdir=LR\n");

		ArrayList<FlowEdge> lst = (ArrayList) getAllEdges();
		lst.sort(null);
		for (FlowEdge edge : lst) {
			sb.append("\t").append(edge.from()).append(" ").append("->").append(" ").append(edge.to());
			if (edge.isWeighted()) {
				String div = edge.getWeight() + "/" + edge.getFlow();
				sb.append(" [label=").append(edge.getFlow() != 0 ? div : edge.getWeight()).append(", len=").append(edge.getWeight()).append("]");
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
}
