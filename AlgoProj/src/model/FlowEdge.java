package model;

public class FlowEdge {
	// edge is from origin -> dest
	private FlowVertex origin;
	private FlowVertex dest;
	
	// capacity of edge, for forward edges this is the real capacity, hence residual capacity = capacity - flow
	// for backward edges, flow = 0, and capacity = flow of forward edge
	private double capacity;
	private double flow;
	
	// real forward edge in case this is a backward edge in residual graph
	private FlowEdge realForwardEdge;
	
	// backward edge in case this edge is forward edge and flow > 0
	private FlowEdge backwardEdge;
	
	public FlowEdge(FlowVertex origin, FlowVertex dest, double capacity) {
		this.origin = origin;
		this.dest = dest;
		this.capacity = capacity;
	}
	
	public void increaseFlow(double increment) throws Exception {
		if (this.isBackwardEdge()) {
			if (increment > capacity) {
				throw new Exception("Increment of " + increment + " on backward edge of capacity " + this.capacity);
			}
			
			// Reduce the capacity of this edge, and reduce the flow in real forward edge
			this.capacity = this.capacity - increment;
			this.realForwardEdge.flow -= increment;
			
			// Adjust excess in origin of real edge, decreasing outgoing flow from origin, so excess increases in origin
			this.realForwardEdge.origin.increaseExcess(increment);

			// Adjust excess in dest of real edge, decreasing incoming flow into dest, so excess decreases in dest
			this.realForwardEdge.dest.increaseExcess(-increment);
			
			if (this.capacity == 0) {
				// no back edge for zero flow, remove edge from forward edge and vertex
				this.realForwardEdge.backwardEdge = null;
				this.origin.removeEdge(this);
			}
		} else {
			if (this.flow + increment > this.capacity) {
				throw new Exception("Increment of " + increment + " on forward edge of capacity " + this.capacity + " and flow " + this.flow);
			}
			
			this.flow += increment;
			
			if (this.backwardEdge == null) {
				// add backward edge if not already there
				this.backwardEdge = new FlowEdge(this.dest, this.origin, this.flow);
				this.backwardEdge.realForwardEdge = this;
				this.dest.addEdge(this.backwardEdge);
			} else {
				// backward edge already there, adjust it's capacity
				this.backwardEdge.setCapacity(this.flow);
			}

			// Adjust excess in origin, increasing outgoing flow from origin, so excess decreases in origin
			this.origin.increaseExcess(-increment);

			// Adjust excess in dest, increasing incoming flow into dest, so excess increases in dest
			this.dest.increaseExcess(increment);
		}
	}
	
	public String getName() {
		String name = this.origin.getName() + "-" + this.dest.getName();
		
		if (this.isBackwardEdge()) {
			name += "-back";
		}
		
		return name;
	}
	
	public FlowVertex getOrigin() {
		return this.origin;
	}
	
	public FlowVertex getDest() {
		return this.dest;
	}
	
	public double getFlow() {
		return this.flow;
	}
	
	public double getCapacity() {
		return this.capacity;
	}
	
	public double getResidualCapacity() {
		if (this.isBackwardEdge()) {
			// for backward edge, residual capacity = capacity = flow in forward edge 
			return this.capacity;
		} else {
			// for forward edge, residual capacity = capacity - flow
			return this.capacity - this.flow;
		}
	}
	
	public boolean isBackwardEdge() {
		return this.realForwardEdge != null;
	}
	
	private void setCapacity(double capacity) {
		this.capacity = capacity;
	}
}
