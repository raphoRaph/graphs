package m1maxflow2025;

import m1graphs2025.Graph;

import java.util.List;

import m1graphs2025.Edge;

public class ResidualGraph extends Graph {
	private List<Integer> lastPath;
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

	public int bottleneckOf(List<Integer> path) {
		int b = Integer.MAX_VALUE;

		for (int i = 0; i < path.size() - 1; i++) {
			int u = path.get(i);
			int v = path.get(i + 1);

			int cap = (getResidualCapacity(u, v));

			b = Math.min(b, cap);
		}
		lastPath = path;
		residualCapacity = b;
		return b;
	}

	private int getResidualCapacity(int u, int v) {
		for (Edge e : getOutEdges(u)) {
			if (e.to().getId() == v) {
				return e.getWeight();
			}
		}
		return 0;
	}

	@Override
	public String toDotString() {
		// TODO Auto-generated method stub
		return super.toDotString();
	}
}
