package m1maxflow2025;

import m1graphs2025.Node;

import java.util.List;

public class FordFulkerson {
	public interface PathFinder {
		List<Node> findPath(ResidualGraph residual, Node source, Node target);
	}

	public static FlowNetwork maxFlow(FlowNetwork fn, PathFinder pathFinder) {

		while (true) {
			ResidualGraph r = ResidualGraph.from(fn);

			List<Node> path = pathFinder.findPath(r, r.sourceNode(), r.targetNode());

			if (path == null) {
				break;
			}

			int delta = r.bottleneckOf(path);
			System.out.println(r.toDotString());
			fn.addFlow(path, delta);
		}
		return fn;
	}
}
