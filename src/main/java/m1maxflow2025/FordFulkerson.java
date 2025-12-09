package m1maxflow2025;

import java.util.List;

import m1graphs2025.Node;

/**
 * Implements the Ford-Fulkerson algorithm for computing the maximum flow in a flow network.
 * It uses a generic pathfinding strategy (e.g., BFS, DFS) defined by the PathFinder interface.
 */
public class FordFulkerson {

	/**
	 * Functional interface for finding a path in the residual graph.
	 * Different implementations can yield different variations of the algorithm (e.g., Edmonds-Karp).
	 */
	public interface PathFinder {
		/**
		 * Finds a path from source to target in the given residual graph.
		 *
		 * @param residual The residual graph
		 * @param source   The source node
		 * @param target   The target node
		 * @return A list of nodes representing the path, or null if no path exists
		 */
		List<Node> findPath(ResidualGraph residual, Node source, Node target);
	}

	/**
	 * Computes the maximum flow in a FlowNetwork using the Ford-Fulkerson algorithm.
	 *
	 * @param fn         The initial flow network
	 * @param pathFinder The strategy used to find augmenting paths in the residual graph
	 * @return The FlowNetwork with the updated maximum flow
	 */
	public static FlowNetwork maxFlow(FlowNetwork fn, PathFinder pathFinder) {

		while (true) {
			ResidualGraph r = ResidualGraph.from(fn);

			List<Node> path = pathFinder.findPath(r, r.sourceNode(), r.targetNode());

			if (path == null) {
				break;
			}

			int delta = r.bottleneckOf(path);
			fn.addFlow(path, delta);
		}
		return fn;
	}
}
