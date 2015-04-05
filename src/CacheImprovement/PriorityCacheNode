package CacheImprovement;

public class PriorityCacheNode {
	String key;
	int frequency;
	long recency;
	double priority;
	public PriorityCacheNode(){
		key="";
		frequency=0;
		recency=0;
		priority=0.0;
	}
	
	public PriorityCacheNode(String u, int f, long r, double p){
		key=u;
		frequency=f;
		recency=r;
		priority=p;
	}
	public void setValue(double alpha, double beta){
		this.frequency=this.frequency+1;
		this.priority=alpha*this.frequency + beta*(System.currentTimeMillis()-this.recency);		
		this.recency=System.currentTimeMillis();// change this
	}
}

