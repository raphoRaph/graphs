package m1maxflow2025;

import java.util.ArrayList;

import m1graphs2025.Edge;
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

	private int flowValue(){
		int res = 0;
		for (Edge edge: getOutEdges(smallestNodeId())) {
			FlowEdge flow = (FlowEdge) edge;
			res += flow.getFlow();	
		}
		return res;
	}


	@Override
	public String toDotString() {
		StringBuilder sb = new StringBuilder();

		sb.append("digraph {\n").append("\trankdir=LR\n");
		sb.append("label =\"Value: ").append(flowValue()).append("\n");

		ArrayList<Edge> lst = (ArrayList) getAllEdges();

		lst.sort(null);
		for (Edge edge : lst) {
			FlowEdge flow = (FlowEdge) edge;
			sb.append("\t").append(flow.from()).append(" ").append("->").append(" ").append(flow.to());
			if (flow.isWeighted()) {
				String div = flow.getFlow() + "/" + flow.getWeight();
				sb.append(" [label=").append(flow.getFlow() != 0 ? div : flow.getWeight()).append(", len=").append(flow.getWeight()).append("]");
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
