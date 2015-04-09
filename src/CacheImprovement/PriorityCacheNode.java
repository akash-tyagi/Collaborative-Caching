package CacheImprovement;

public class PriorityCacheNode {
//	String key;
	int frequency;
	long recency;
	double priority;
	final int max_freq=10000;
	final long max_rec=10;
	public PriorityCacheNode(){
//		key="";
		frequency=0;
		recency=0;
		priority=0.0;
	}
	
	public PriorityCacheNode(int f, long r, double p){
//		key=u;
		frequency=f;
		recency=r;
		priority=p;
	}
	public void setValue(double alpha, double beta,int freq,long rec){
		this.frequency=this.frequency+1;
		System.out.println("recency : " + (System.currentTimeMillis()-this.recency));
		this.priority=alpha*((double)this.frequency/(double)max_freq) + beta*(1.0-(double)(System.currentTimeMillis()-this.recency)/(double)max_rec);		
		this.recency=System.currentTimeMillis();
		System.out.println("priority : " + this.priority);		
		System.out.println(this.frequency);
//		System.out.println(System.currentTimeMillis()-this.recency);
//		System.out.println(freq);
//		System.out.println(rec);
		
	}
}
