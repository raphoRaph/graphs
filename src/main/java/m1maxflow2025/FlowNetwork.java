package m1maxflow2025;

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
	public void toDotFile(String fileName) {
		// TODO Auto-generated method stub
		super.toDotFile(fileName);
	}
}
