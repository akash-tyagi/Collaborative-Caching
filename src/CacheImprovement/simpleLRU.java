package CacheImprovement;

import java.util.*;

public class simpleLRU {
	Map<String,Integer> cache;
	int maxSize;
	double hitRatio;
	double missRatio;
	double total;
	
	simpleLRU(int s){
		this.maxSize=s;
		this.hitRatio =0;
		this.missRatio=0;
		this.total=0;			
		this.cache = new LinkedHashMap<String,Integer>(maxSize+1, .75F, true) {
		    // This method is called just after a new entry has been added
		    public boolean removeEldestEntry(Map.Entry eldest) {
		        return size() > maxSize;
		    }
		};
	}
	public void addObject(String userKey) {//change to object later
		PriorityCacheNode node = null;
		this.total += 1;
		//node = findNodeByKey(userKey);
		if (this.cache.get(userKey) != null) {
			this.hitRatio += 1;
			this.cache.remove(userKey);			
			this.cache.put(userKey,1);			
		} else {
			this.missRatio += 1;
			this.cache.put(userKey,1);
		}
	}	
	public void CacheHitRatio() {
		System.out.println("Hit Ratio----->" + this.hitRatio / this.total);
		System.out.println("MissRatio----->" + this.missRatio / this.total);
		System.out.println("Total--------->" + this.total);	
	}	
	
}
