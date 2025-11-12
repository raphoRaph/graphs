package  m1maxflow2025;
import m1graphs2025.Edge;
import m1graphs2025.Graph;
import m1graphs2025.Node;

public class FlowEdge extends Edge{
    private Integer flow;

    public FlowEdge(Node from, Node to, Graph graphHolder, Integer weight, Integer flow) {
        super(from, to, graphHolder, weight);
        this.flow = flow;
    }

    public Integer getFlow(){
        return this.flow;
    }

    public void setFlow(Integer newflow){
        this.flow = newflow;
    }


}