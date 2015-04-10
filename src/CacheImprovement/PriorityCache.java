package CacheImprovement;

import java.util.*;

public class PriorityCache {
	double hitRatio;
	double missRatio;
	double total;
	int maxSize;

	int freq = 1;
	long rec = 0;
	String besturl = "";
	PriorityQueue<String> pcache;
	HashMap<String, PriorityCacheNode> hm;

	public PriorityCache(int s) {
		this.maxSize = s;
		this.hitRatio = 0;
		this.missRatio = 0;
		this.total = 0;
		this.rec = 0;
		this.hm = new HashMap<String, PriorityCacheNode>();
		this.pcache = new PriorityQueue<String>(maxSize,
				new Comparator<String>() {
					public int compare(String s1, String s2) {
						PriorityCacheNode n1 = hm.get(s1);
						PriorityCacheNode n2 = hm.get(s2);
						if (n1.priority < n2.priority)
							return -1;
						if (n1.priority > n2.priority)
							return 1;
						return 0;
					}
				});
	}

	public void shrinkToSize(int s) {
		if (this.pcache.size() == s) {
			String key = this.pcache.poll();
			hm.remove(key);
		}
	}

	public void addObject(String userKey) {// change to object later
		PriorityCacheNode node = null;
		this.total += 1;
		if (this.hm.containsKey(userKey)) {
			node = this.hm.get(userKey);
		}
		rec++;

		if (node != null) {
			this.hitRatio += 1;
			freq = Math.max(freq, node.frequency + 1);
			node.setValue(freq, rec);
			this.hm.put(userKey, node);
			this.pcache.remove(userKey);
			this.pcache.add(userKey);
		} else {
			this.missRatio += 1;
			node = new PriorityCacheNode(1, rec, 0.0);
			shrinkToSize(maxSize);
			this.hm.put(userKey, node);
			this.pcache.add(userKey);
		}

		removeExpiredElements();

	}

	private void removeExpiredElements() {
		int i = 0;
		while (pcache.peek() != null && i++ < 10) {
			String key = pcache.peek();
			PriorityCacheNode node = hm.get(key);
			if (rec - node.recency > 50000) {
				System.out.println("deleting key$$$$$$$$:" + key);
				pcache.poll();
				continue;
			}
			break;
		}
	}

	public void CacheHitRatio() {
		System.out.println("Hit Ratio----->" + this.hitRatio / this.total);
		System.out.println("MissRatio----->" + this.missRatio / this.total);
		System.out.println("Total--------->" + this.total);
		System.out.println("freq--------->" + freq);
		// System.out.println("url--------->" + besturl);
		System.out.println("rec--------->" + rec);
		System.out.println("max prio " + PriorityCacheNode.max_prio);
		System.out.println("max freq " + PriorityCacheNode.max_freq);
		System.out.println("max rec " + PriorityCacheNode.max_rec);

		// System.out.println("cache size--------->" + this.pcache.size());
	}

}
