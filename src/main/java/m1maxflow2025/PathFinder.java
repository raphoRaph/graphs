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

import m1graphs2025.Node;
import m1graphs2025.Edge;

public class PathFinder {
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
}
