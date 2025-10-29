package m1graphs2025;

public class NodeVisitInfo {
	private colour color;
	private Node predecessor;
	private int timestampDisc;
	private int timestampFin;

	public NodeVisitInfo() {
		this.color = colour.WHITE;
		this.predecessor = null;
	}

	public colour getColor() {
		return this.color;
	}

	public int timestampDisc() {
		return this.timestampDisc;
	}

	public void setTimesTampDisc(int time) {
		this.timestampDisc = time;
	}

	public void setTimestampFin(int time) {
		this.timestampFin = time;
	}

	public void setColor(colour color) {
		this.color = color;
	}

	public void setPredecessor(Node node) {
		this.predecessor = node;
	}

	@Override
	public String toString() {
		return "NodeVisitInfo [colour=" + color + ", predecessor=" + predecessor + ", discovery=" + timestampDisc
				+ ", finished=" + timestampFin + "]";
	}
}
