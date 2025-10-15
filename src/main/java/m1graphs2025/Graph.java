package m1graphs2025;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jdk.nashorn.internal.ir.Node;

import java.util.HashMap;

public class Graph {
	private Map<Node, List<Edge>> adjEdList;

	// create a graph through dedicated constructors (unweighted graph)
	public Graph(int ... nodes) {
		adjEdList = new HashMap<>();
		int idFrom = 1;
		for (int idTo : nodes) {
			addNodeIfAbsent(idFrom);
			if (idTo == 0) {
				idFrom++;
			} else {
				addNodeIfAbsent(idTo);
				addEdge(idFrom, idTo);
			}
		}
	}

	public Graph() {
		adjEdList = new HashMap<>();
	}
	// Node part //

	public String toString(){
		String res = "";
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
			res += "\n" + pair.getKey().toString() + " : \n\t";
			for (Edge edge : adjEdList.get(pair.getKey())){
				res += edge.to().toString() + ", ";
			}
		}
		return res;
	}

	public int nbNodes() {
		return adjEdList.size();
	}

	public boolean usesNode(Node n){
		return usesNode(n.getId());
	}

	public boolean usesNode(int nodeId){
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
			if (pair.getKey().getId() == nodeId){
				return true;
			}
		}
		return false;
	}
		
	public boolean holdsNode(Node n){
		return n.getGraph() == this;
	}

	public Node getNode(int id){
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
			if (pair.getKey().getId() == id){
				return pair.getKey();
			}
		}
		return null;
	}

	public boolean addNode(Node n){
		return addNode(n.getId());
	}

	public boolean addNode(int nodeId) {
		if (nodeId <= 0) {
			throw new IllegalArgumentException("nodeId must be higher than 0");
		}
		if (usesNode(nodeId)) {
			return false;
		}
		adjEdList.put(new Node(nodeId, this), new ArrayList<Edge>());
		return true;
	}

	private void addNodeIfAbsent(int nodeId) {
		if (!usesNode(nodeId)) addNode(nodeId);
	}

	public boolean removeNode(Node n){
		return removeNode(n.getId());
	}

	public boolean removeNode(int nodeId){
		Node node = getNode(nodeId);
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
			if (pair.getKey().getId() == nodeId){
				adjEdList.remove(node);
			}
			for(Edge elt : pair.getValue()){
				if(elt.from() == node || elt.to() == node){
					pair.getValue().remove(elt);
				}
			}
		}
		return true;
	}

	public List<Node> getAllNodes(){
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.addAll(adjEdList.keySet());
		return nodeList;
	}

	public int largestNodeId(){
		int largest = -1;
		for (Node node : adjEdList.keySet()){
			largest = Math.max(largest, node.getId());
		}
		return largest;
	}

	public int smallestNodeId(){
		int smallest = Integer.MAX_VALUE;
		for (Node node : adjEdList.keySet()){
			smallest = Math.min(smallest, node.getId());
		}
		return smallest == Integer.MAX_VALUE ? -1 : smallest;
	}

	public List<Node> getSuccessors(Node n){
		return n.getSuccessors();
	}

	public List<Node> getSuccessorsMulti(Node n){
		return n.getSuccessorsMulti();
	}

	public boolean adjacent(Node u, Node v){
		return u.adjacent(v);
	}

	public boolean adjacent(int uId, int vId){
		return getNode(uId).adjacent(vId);
	}

	public int inDegree(Node n){
		return n.inDegree();
	}

	public int inDegree(int nodeId){
		return inDegree(getNode(nodeId));
	}

	public int outDegree(Node n){
		return n.outDegree();
	}

	public int outDegree(int nodeId){
		return outDegree(getNode(nodeId));
	}

	public int degree(Node n){
		return n.degree();
	}

	public int degree(int nodeId){
		return degree(getNode(nodeId));
	}

	// edge part //

    private Edge getEdge(int fromId, int toId){
        for (Edge edge : adjEdList.get(getNode(fromId))){
            if (edge.from().getId() == fromId && edge.to().getId() == toId){
                return edge;
            }
        }
        return null;
    }

    public int nbEdges(){
        int count = 0;
        for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
            count += pair.getValue().size();
        }            
        return count;
    }

    public boolean existsEdge(Edge e){
        List<Edge> list = adjEdList.get(e.from());
        for (Edge edge : list){
            if(edge.equals(e)){
                return true;
            }
        }
        return false;
    }

    public boolean existsEdge(Node u, Node v){
        return existsEdge(u.getId(), v.getId());
    }

    public boolean existsEdge(int uId, int vId){
        List<Edge> list = adjEdList.get(getNode(uId));
        for (Edge edge : list){
            if(edge.to() == getNode(vId)){
                return true;
            }
        }
        return false;
    }

    public boolean isMultiEdge(Edge e){
        return isMultiEdge(e.from(), e.to());
    }
    
    public boolean isMultiEdge(Node u, Node v){
        return isMultiEdge(u.getId(), v.getId());
    }

    public boolean isMultiEdge(int uId, int vId){
        int count = 0;
        List<Edge> list = adjEdList.get(getNode(uId));
        for (Edge edge : list){
            if (edge.to() == getNode(vId)){
                count ++;
            }
        }
        return count >= 2;
    }

	// TODO why we add the nodes if node is absent ?
    private void addEdge(int fromId, int toId, Integer weight){
        addNodeIfAbsent(fromId);
        addNodeIfAbsent(toId);
        Node from = getNode(fromId);
        Node to = getNode(toId);

        adjEdList.get(from).add(new Edge(from, to, this, weight));
    }

    public void addEdge(Edge e){
        addEdge(e.from().getId(), e.to().getId(), e.getWeight());
    }

    public void addEdge(Node from, Node to){
        addEdge(from.getId(), to.getId());
    }

	public void addEdge(Node from, Node to, Integer weight){
        addEdge(from.getId(), to.getId(), weight);
    }
    
    public void addEdge(int fromId, int toId){
        addEdge(fromId, toId, null);
    }

    public boolean removeEdge(Edge e){
        return removeEdge(e.from(), e.to(), e.getWeight());
    }
    
    public boolean removeEdge(Node from, Node to){
        return removeEdge(from.getId(), to.getId());
    }

    public boolean removeEdge(Node from, Node to, Integer weight){
        return removeEdge(from.getId(), to.getId(), weight);
    }
    
    public boolean removeEdge(int fromId, int toId){
        Node from = getNode(fromId);
        return adjEdList.get(from).remove(getEdge(fromId, toId));
    }

	// why remove all edges ?
    public boolean removeEdge(int fromId, int toId, Integer weight){
        Node from = getNode(fromId);
        List<Edge> lst = getEdges(from, getNode(toId));
        for(Edge edge : lst){
            if (edge.getWeight() == weight){
                return adjEdList.get(from).remove(edge);
            }
        }
        return false;
    }

    public List<Edge> getOutEdges(Node n){
        return getOutEdges(n.getId());
    }

    public List<Edge> getOutEdges(int nodeId){
        return adjEdList.get(getNode(nodeId));
    }

    public List<Edge> getInEdges(Node n){
        return getInEdges(n.getId());
    }

    public List<Edge> getInEdges(int nodeId){
        List<Edge> lst = new ArrayList<Edge>();
        for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
            for (Edge edge : pair.getValue()){
                if(edge.to().getId() == nodeId){
                    lst.add(edge);
                }
            }
        }
        return lst;
    }

    public List<Edge> getIncidentEdges(Node n){
        return getIncidentEdges(n.getId());
    }

    public List<Edge> getIncidentEdges(int nodeId){
        List<Edge> lst = getOutEdges(nodeId);
        for (Edge edge : getInEdges(nodeId)){
            lst.add(edge);
        }
        return lst;
    }

    public List<Edge> getEdges(Node u, Node v){
        List<Edge> lst = new ArrayList<Edge>();
        for (Edge edge : adjEdList.get(u)){
            if (edge.from() == u && edge.to() == v){
                lst.add(edge);
            }
        }
        return lst;
    }

    public List<Edge> getAllEdges(){
        List<Edge> lst = new ArrayList<Edge>();
        for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
            for (Edge edge : pair.getValue()){
                lst.add(edge);
            }
        }
        return lst;
    }


	// graph's representations and transformations //

	public static int[] convertIntegers(List<Integer> integers){
		int[] ret = new int[integers.size()];
		for (int i=0; i < ret.length; i++)
		{
			ret[i] = integers.get(i).intValue();
		}
    	return ret;
	}

	public int[] toSuccessorArray(){
		List<int> myList = new ArrayList<int>();		
		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
			myList.add(pair.getKey().getId());
		}
		Collections.sort(myList);
		
		List<int> res = new ArrayList<>();
		int count = 0;
		for (int i = 1; i < myList.get(myList.size()-1); i++){
			if (i == myList.get(count)){
				for (Edge edge : adjEdList.get(getNode(i))){
					res.add(edge.to().getId())
				}
				count++
			}
			res.add(0);
		}
		return convertIntegers(res);
	}

	public int[][] toAdjMatrix(){
		int maxNode = largestNodeId();
		int[][] res = new int[maxNode][maxNode];
		for (int i = 0; i < maxNode; i++){
			for (int j = 0; j < maxNode; j++){
				res[i][j] = 0;
			}
		}

		for (Map.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
			for (Edge edge : adjEdList.get(pair.getKey())){
				res[edge.from().getId()][edge.to().getId()]++;
			}
		}
        return res;
	}

	public Graph getReverse(){
        Graph graph = new Graph();
		for (Edge edge : getAllEdges()) {
			graph.addEdge(edge.getSymetric());
		}
		return graph;
	}

	public Graph getTransitiveClosure(){
		Graph graph = copy();

		for (Node from : adjEdList.keySet()) {
			for (Node to : adjEdList.keySet()) {
				for (Node inter : adjEdList.keySet()) {
					if(!existsEdge(from, to) && existsEdge(from, inter) && existsEdge(inter, to)){
						graph.addEdge(from, to);
					}
				}
			}
		}

        return graph;
	}

	public boolean isMultiGraph(){
		for (Edge edge : getAllEdges()) {
			if (edge.isMultiEdge()){
				return true;
			}
		}
		return false;
	}

	public boolean isSimpleGraph(){
		return !isMultiGraph();
	}

	public boolean hasSelfLoops(){
		for (Edge edge : getAllEdges()) {
			if (edge.isSelfLoop()){
				return true;
			}
		}
		return false;
	}

	public Graph copy(){
		Graph graph = new Graph();
		for (Edge edge : getAllEdges()) {
			graph.addEdge(edge.from(), edge.to(), edge.getWeight());
		}
		return graph;
	}
	
	// Graph traversal //
  
	public List<Node> getDFS(){
		return getDFS(smallestNodeId());
	}

	public List<Node> getDFS(Node u){
		return getDFS(u.getId());
	}

	public List<Node> getDFS(int id){
		Set<Node> lst = new Set<Node>();
		getDFS(id, lst);
		return new ArrayList<>(lst);
	}

	private void getDFS(int id, Set<Node> lst){
		for (Node node : getSuccessors(getNode(id))) {
			lst.add(node);
			getDFS(node.getId(), lst);
		}
		return ;
	}

	public List<Node> getBFS(){
		return getBFS(smallestNodeId())
	}

	public List<Node> getBFS(Node u){
		return getBFS(u.getId())
	}

	public List<Node> getBFS(int id){
		List<Node> lst = new ArrayList();
		Queue<Node> queue = new Queue<Node>();
		queue.add(getNode(id))
		while (queue.peek() != null) {
			Node currNode = queue.remove();
			for (Node node : getSuccessors(currNode)) {
				if (!lst.contains(node)) {
					lst.add(node)
					queue.add(node)
				}
			}
		}
		return lst;
	}

  /*
	public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
	}

	public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
	}

	// Graph Import and Export //

	public static Graph fromDotFile(String filename){
	}

	public static Graph fromDotFile(String filename, String extension){
	}

	public String toDotString(){
	}

	public void toDotFile(String fileName){
	}

	public void toDotFile(String fileName, String extension){
	}
    */
}
