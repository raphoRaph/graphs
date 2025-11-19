package m1maxflow2025;

import java.util.ArrayList;
import java.util.List;

import m1graphs2025.Edge;
import m1graphs2025.Graph;
import m1graphs2025.Node;

public class ResidualGraph extends Graph {
	private List<Node> lastPath;
	Integer residualCapacity;

	public ResidualGraph() {
		super();
	}

	public static ResidualGraph from(FlowNetwork flowNetwork) {
		ResidualGraph residualGraph = new ResidualGraph();

		for (Edge edge : flowNetwork.getAllEdges()) {
			FlowEdge flow = (FlowEdge) edge;
			int fromId = flow.from().getId();
			int toId = flow.to().getId();
			Integer capacity = flow.getWeight();
			Integer iFlow = flow.getFlow();

			residualGraph.addNodeIfAbsent(fromId);
			residualGraph.addNodeIfAbsent(toId);

			if (capacity - iFlow > 0) {
				residualGraph.addEdge(fromId, toId, capacity - iFlow);
			}
			if (iFlow > 0) {
				residualGraph.addEdge(toId, fromId, iFlow);
			}
		}
		return residualGraph;
	}

	public int bottleneckOf(List<Node> path) {
		int b = Integer.MAX_VALUE;

		for (int i = 0; i < path.size() - 1; i++) {
			Node u = path.get(i);
			Node v = path.get(i + 1);

			int cap = (getResidualCapacity(u, v));

			b = Math.min(b, cap);
		}
		lastPath = path;
		residualCapacity = b;
		return b;
	}

	private int getResidualCapacity(Node u, Node v) {
		for (Edge e : getOutEdges(u)) {
			if (e.to() == v) {
				return e.getWeight();
			}
		}
		return 0;
	}

	private String pathString(){
		String path = "Augmenting path: [";
		for (int i = 0; i < lastPath.size(); i++){
			path += lastPath.get(i).toString();
			if(i != lastPath.size() -1){
				path += ", ";
			}
		}
		path += "].\n";
		return path;
	}

	@Override
	public String toDotString() {
		StringBuilder sb = new StringBuilder();

		sb.append("digraph {\n").append("\trankdir=LR\n");
		String path = pathString();
		String resiCap = "Residual capacity: " + residualCapacity;
		sb.append("label = residual graph.\n").append(path).append(resiCap).append("\n");

		ArrayList<Edge> lst = (ArrayList) getAllEdges();

		lst.sort(null);
		for (Edge edge : lst) {
			FlowEdge flow = (FlowEdge) edge;
			String bigArrow = "";
			if (lastPath.contains(edge.from()) && lastPath.contains(edge.to())){
				bigArrow = ", penwidth=3, color=\"blue\"";
			}
			sb.append("\t").append(flow.from()).append(" ").append("->").append(" ").append(flow.to());
			String label = (flow.getFlow() != 0) ? (flow.getFlow() + "/" + flow.getWeight()) : "" + flow.getWeight();
			sb.append(" [label=").append(label).append(", len=").append(flow.getWeight()).append(bigArrow).append("]");
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
