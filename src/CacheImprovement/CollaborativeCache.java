package CacheImprovement;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CollaborativeCache {

	private final int numberOfReplicas;
	private final int totalNodes;
	private final SortedMap<Integer, LruCache> circle = new TreeMap<Integer, LruCache>();
	private final List<LruCache> caches = new ArrayList<LruCache>();

	public CollaborativeCache(int numberOfReplicas, int totalNodes,
			int timeoutMilliSeconds, int maxSize) {
		this.numberOfReplicas = numberOfReplicas;
		this.totalNodes = totalNodes;

		for (int i = 0; i < totalNodes; i++) {
			LruCache cache = new LruCache("con_lru_" + i, timeoutMilliSeconds,
					maxSize);
			add(cache, i);
			caches.add(cache);
		}

	}

	private void add(LruCache node, int startNum) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(startNum, node);
			startNum += totalNodes;
		}
	}

	public void addObject(Object key, Integer integer) {
		getCache(key).addObject(key, integer);
	}

	private LruCache getCache(Object key) {
		if (circle.isEmpty()) {
			return null;
		}

		int hash = getHashValue(key.toString());
		return circle.get(hash);
	}

	private int getHashValue(String string) {
		int hash = 0;
		for (int i = 0; i < string.length(); i++) {
			hash = (hash << 5) - hash + string.charAt(i);
		}
		hash = (hash < 0 ? -hash : hash);
		return hash % (totalNodes * numberOfReplicas);
	}

	public void CacheHitRatio() {
		double hitRatio = 0;
		double missRatio = 0;
		double total = 0;
		for (LruCache lruCache : caches) {
			total += lruCache.getTotal();
			hitRatio += lruCache.getHitRatio();
			missRatio += lruCache.getMissRatio();
		}
		System.out.println("Hit Ratio----->" + hitRatio / total);
		System.out.println("MissRatio----->" + missRatio / total);
		System.out.println("Total--------->" + total);
	}
}
