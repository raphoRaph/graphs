package m1graphs2025;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Node, List<Edge>> adjEdList;

    // create a graph through dedicated constructors (unweighted graph)
    public Graph(int ... node){
        //id >= 1
    }
    

    // Node part //

    public int nbNodes(){
        return adjEdList.size();
    }

    public boolean usesNode(Node n){
        return usesNode(n.getId());
    }
    
    public boolean usesNode(int nodeId){
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
            if (pair.getkey().getId == nodeId){
                return true;
            }
        }
        return false;
    }
    
    public boolean holdsNode(Node n){
        return adjEdList.containsKey(n);
    }
    
    public Node getNode(int id){
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
            if (pair.getkey().getId == id){
                return pair.getkey();
            }
        }
        return null;
    }

    public boolean addNode(Node n){
        if (n.getId() <= 0 || usesNode(n)){
            return false;
        }
        adjEdList.put(n, new List<Edge>);
        return true;
    }

    public boolean removeNode(Node n){
        return removeNode(n.getId());
    }

    public boolean removeNode(int nodeId){
        Node node = getNode(nodeId)
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
            if (pair.getkey().getId == nodeId){
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
        List<Node> list = new ArrayList<Node>();
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
            list.add(pair.getkey());
        }
        return list;
    }

    public int largestNodeId(){
        int largest = -1;
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
            if (pair.getKey().getId() > largest){
                largest = pair.getKey().getId();
            }
        }
        return largest;
    }

    public int samllestNodeId(){
        int smallest = -1;
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
            if (smallest == -1){
                smallest = pair.getkey().getId();
            }
            if (pair.getKey().getId() < smallest){
                smallest = pair.getKey().getId();
            }
        }
        return smallest;
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
    }

    public int nbEdges(){
        int count = 0;
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entrySet()){
            count += pair.getValue().size();
        }            
        return count;
    }

    public boolean existsEdge(Edge e){
        return existsEdge(e.from(), e.to());
    }

    public boolean existsEdge(Node u, Node v){
        return existsEdge(u.getId(), v.getId());
    }

    public boolean existsEdge(int uId, int vId){
        List<Edge> list = adjEdList.get(getNode(uId));
        for (Edge edge : list){
            if(edge.to == getNode(vId)){
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
            if (edge.to == getNode(vId)){
                count ++;
            }
        }
        return count >= 2;
    }

    public void addEdge(Edge e){
        addEdge(e.from(), e.to());
    }

    public void addEdge(Node from, Node to){
        addEdge(from.getId(), to.getId());
    }
    
    public void addEdge(int fromId, int toId){
        Node from = getNode(fromId);
        adjEdList.put(from, adjEdList.get(from).add(new Edge(from, getNode(toId))));
    }

    public boolean removeEdge(Edge e){
        return removeEdge(e.from(), e.to());
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
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
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
        return lst
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
        for (adjEdList.Entry<Node, List<Edge>> pair : adjEdList.entySet()){
            for (Edge edge : pair.getValue()){
                lst.add(edge);
            }
        }
        return lst;
    }


    // graph's representations and transformations //

    public int[] toSuccessorArray(){
        return -1;
    }

    public int[][] toAdjMatrix(){
    }

    public Graph getReverse(){
    }

    public Graph getTransitiveClosure(){
    }

    public boolean isMultiGraph(){
        return true;
    }

    public boolean isSimpleGraph(){
        return true;
    }

    public boolean hasSelfLoops(){
        return true;
    }

    public Graph copy(){
    }

    
    // Graph traversal //

    public List<Node> getDFS(){
    }

    public List<Node> getDFS(Node u){
    }

    public List<Node> getBFS(){
    }

    public List<Node> getBFS(Node u){
    }

    public List<Node> getDFSWithVisitInfo(Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
    }

    public List<Node> getDFSWithVisitInfo(Node u, Map<Node, NodeVisitInfo> nodeVisit, Map<Edge, EdgeVisitType> edgeVisit){
    }

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
}
