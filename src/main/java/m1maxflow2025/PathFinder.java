package m1maxflow2025;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import m1graphs2025.Edge;
import m1graphs2025.Node;

/**
 * Provides static factory methods to create different implementations of
 * FordFulkerson.PathFinder for finding augmenting paths in a residual graph.
 * These pathfinding strategies can be used with the Ford-Fulkerson algorithm
 * to determine the maximum flow.
 */
public class PathFinder {
	/**
	 * Returns a FordFulkerson.PathFinder implementation that uses Breadth-First
	 * Search (BFS) to find an augmenting path in the residual graph. This strategy
	 * corresponds to the Edmonds-Karp algorithm.
	 *
	 * @return A PathFinder instance using BFS.
	 */
	public static FordFulkerson.PathFinder bfsPathFinder() {
		return (residual, source, target) -> {
			Queue<List<Node>> queue = new LinkedList<>();
			List<Node> startPath = new ArrayList<>();
			startPath.add(source);
			queue.add(startPath);

			while (!queue.isEmpty()) {
				List<Node> path = queue.poll();
				Node last = path.get(path.size() - 1);
				if (last.equals(target)) {
					return path;
				}

				for (Node succ : residual.getSuccessors(last)) {
					if (!path.contains(succ)) {
						List<Node> newPath = new ArrayList<>(path);
						newPath.add(succ);
						queue.add(newPath);
					}
				}
			}
			return null;
		};
	}

	/**
	 * Returns a FordFulkerson.PathFinder implementation that uses Depth-First
	 * Search (DFS) to find an augmenting path in the residual graph.
	 *
	 * @return A PathFinder instance using DFS.
	 */
	public static FordFulkerson.PathFinder dfsPathFinder() {
		return (residual, source, target) -> {
			Stack<List<Node>> stack = new Stack<>();
			List<Node> startPath = new ArrayList<>();
			startPath.add(source);
			stack.push(startPath);

			while (!stack.isEmpty()) {
				List<Node> path = stack.pop();
				Node last = path.get(path.size() - 1);
				if (last.equals(target)) {
					return path;
				}

				for (Node succ : residual.getSuccessors(last)) {
					if (!path.contains(succ)) {
						List<Node> newPath = new ArrayList<>(path);
						newPath.add(succ);
						stack.push(newPath);
					}
				}
			}
			return null;
		};
	}

	/**
	 * Returns a FordFulkerson.PathFinder implementation that uses Dijkstra's
	 * algorithm to find an augmenting path in the residual graph, prioritizing
	 * paths with higher residual capacity.
	 *
	 * @return A PathFinder instance using Dijkstra's algorithm.
	 */
	public static FordFulkerson.PathFinder dijkstraPathFinder() {
		return (residual, source, target) -> {
			Map<Node, Integer> dist = new HashMap<>();
			Map<Node, Node> prev = new HashMap<>();
			PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

			for (Node n : residual.getAllNodes())
				dist.put(n, Integer.MAX_VALUE);
			dist.put(source, 0);
			pq.add(source);

			while (!pq.isEmpty()) {
				Node u = pq.poll();
				if (u.equals(target))
					break;

				for (Edge e : residual.getOutEdges(u)) {
					Node v = e.to();
					int weight = e.getWeight();
					int alt = dist.get(u) + weight;
					if (alt < dist.get(v)) {
						dist.put(v, alt);
						prev.put(v, u);
						pq.add(v);
					}
				}
			}

			if (!prev.containsKey(target))
				return null;

			List<Node> path = new ArrayList<>();
			for (Node at = target; at != null; at = prev.get(at))
				path.add(at);
			Collections.reverse(path);

			return path;
		};
	}

	/**
	 * Creates a PathFinder implementing a "maximum capacity" variant of Dijkstra,
	 * also known as the *Widest Path* algorithm.
	 *
	 * @return a PathFinder implementing the widest-path strategy
	 */
	public static FordFulkerson.PathFinder dijkstraMaxPathFinder() {
		return (residual, source, target) -> {

			Map<Node, Integer> best = new HashMap<>();
			Map<Node, Node> prev = new HashMap<>();

			PriorityQueue<Node> pq = new PriorityQueue<>(
					(a, b) -> Integer.compare(best.get(b), best.get(a)));

			for (Node n : residual.getAllNodes())
				best.put(n, -1);

			best.put(source, Integer.MAX_VALUE);
			pq.add(source);

			while (!pq.isEmpty()) {
				Node u = pq.poll();

				if (u.equals(target))
					break;

				for (Edge e : residual.getOutEdges(u)) {
					Node v = e.to();
					int cap = e.getWeight();
					if (cap <= 0)
						continue;

					int newCap = Math.min(best.get(u), cap);

					if (newCap > best.get(v)) {
						best.put(v, newCap);
						prev.put(v, u);
						pq.add(v);
					}
				}
			}

			if (!prev.containsKey(target))
				return null;

			List<Node> path = new ArrayList<>();
			for (Node at = target; at != null; at = prev.get(at))
				path.add(at);

			Collections.reverse(path);
			return path;
		};
	}
}
