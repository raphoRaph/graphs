package m1graphs2025;

import java.util.HashMap;
import java.util.Map;

public class TestClassificationDFS {

	public static void main(String[] args) {
		System.out.println("*-----------------------------------------------------------------------*");
		System.out.println("************   PART 6. DFS and Node Visit Info   ***********************");
		System.out.println("*-----------------------------------------------------------------------*");

		// Lecture VS Example
		Graph gLecture = Graph.fromDotFile("lectureDFS");
		System.out.println("Graph read as:");
		System.out.println(gLecture.toDotString());

		Map<Node, NodeVisitInfo> nodeVisit = new HashMap<Node, NodeVisitInfo>();
		Map<Edge, EdgeVisitType> edgeVisit = new HashMap<Edge, EdgeVisitType>();

		gLecture.getDFSWithVisitInfo(nodeVisit, edgeVisit);

		System.out.println("Nodes visit info\n-----------------\n");
		for (Node u : gLecture.getAllNodes()) {
			System.out.println(u + ": " + nodeVisit.get(u));
		}

		System.out.println("Edges visit info\n-----------------\n");
		for (Edge e : gLecture.getAllEdges()) {
			System.out.println(e + ": " + edgeVisit.get(e));
		}
	}
}
