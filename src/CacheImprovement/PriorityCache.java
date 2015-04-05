package CacheImprovement;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityCache {
	double hitRatio;
	double missRatio;
	double total;
	int maxSize;
	public final double alpha=0.8;
	public final double beta=0.2;
	
	PriorityQueue<PriorityCacheNode> pcache;
	public PriorityCache(int s){
		this.maxSize = s;
		this.hitRatio =0;
		this.missRatio=0;
		this.total=0;		
		this.pcache = new PriorityQueue<PriorityCacheNode>(maxSize, new Comparator<PriorityCacheNode>(){
			public int compare(PriorityCacheNode n1, PriorityCacheNode n2){
				if (n1.priority < n2.priority)
					return -1;
				if (n1.priority > n2.priority)
					return 1;
				return 0;
			}
		});
	}
	private PriorityCacheNode findNodeByKey(Object userKey) {
		for(PriorityCacheNode n : this.pcache){
			if(n.key.equals((String)userKey))
				return n;
		}
		return null;
	}	
	
	public void shrinkToSize(int s){
		if(this.pcache.size() == s)
			this.pcache.remove();
	}
	
	public void addObject(String userKey) {//change to object later
		PriorityCacheNode node;
		this.total += 1;
		node = findNodeByKey(userKey);
		if (node != null) {
			this.hitRatio += 1;
			this.pcache.remove(node);
			node.setValue(alpha,beta);
			this.pcache.add(node);
		} else {
			this.missRatio += 1;
			double new_priority=alpha+beta*10;
			node = new PriorityCacheNode(userKey,1,System.currentTimeMillis(),new_priority);
			shrinkToSize(maxSize);
			this.pcache.add(node);
		}
	}

	public void CacheHitRatio() {
		System.out.println("Hit Ratio----->" + this.hitRatio / this.total);
		System.out.println("MissRatio----->" + this.missRatio / this.total);
		System.out.println("Total--------->" + this.total);
		//System.out.println("cache size--------->" + this.pcache.size());		
	}
	
} 
