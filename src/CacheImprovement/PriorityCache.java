package CacheImprovement;

import java.util.*;


public class PriorityCache {
	double hitRatio;
	double missRatio;
	double total;
	int maxSize;
	public final double alpha=0.8;
	public final double beta=0.2;
	
	PriorityQueue<String> pcache;
	HashMap<String,PriorityCacheNode> hm;
	public PriorityCache(int s){
		this.maxSize = s;
		this.hitRatio =0;
		this.missRatio=0;
		this.total=0;	
		this.hm = new HashMap<String,PriorityCacheNode>();
		this.pcache = new PriorityQueue<String>(maxSize, new Comparator<String>(){
			public int compare(String s1, String s2){
				PriorityCacheNode n1=hm.get(s1);
				PriorityCacheNode n2=hm.get(s2);				
				if (n1.priority < n2.priority)
					return -1;
				if (n1.priority > n2.priority)
					return 1;
				return 0;
			}
		});
	}
	
	
	public void shrinkToSize(int s){
		if(this.pcache.size() == s){
			String key = this.pcache.poll();		
			hm.remove(key);
		}
	}
	
	public void addObject(String userKey) {//change to object later
		PriorityCacheNode node = null;
		this.total += 1;
		if(this.hm.containsKey(userKey)){
			node = this.hm.get(userKey);
		}
		
		//node = findNodeByKey(userKey);
		if (node != null) {
			this.hitRatio += 1;
			node.setValue(alpha,beta);
			this.hm.put(userKey, node);
			this.pcache.remove(userKey);			
			this.pcache.add(userKey);			
		} else {
			this.missRatio += 1;
			double new_priority=alpha+beta*10;
			node = new PriorityCacheNode(1,System.currentTimeMillis(),new_priority);
			shrinkToSize(maxSize);
			this.hm.put(userKey, node);
			this.pcache.add(userKey);
		}
	}

	public void CacheHitRatio() {
		System.out.println("Hit Ratio----->" + this.hitRatio / this.total);
		System.out.println("MissRatio----->" + this.missRatio / this.total);
		System.out.println("Total--------->" + this.total);
		//System.out.println("cache size--------->" + this.pcache.size());		
	}
	
} 
