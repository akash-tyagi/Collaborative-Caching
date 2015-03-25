package CacheImprovement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistenHashing {

	private final int numberOfReplicas;
	private final SortedMap<Integer, LruCache> circle = new TreeMap<Integer, LruCache>();
	private final List<LruCache> caches = new ArrayList<LruCache>();

	public ConsistenHashing(int numberOfReplicas, int totalNodes,
			int timeoutMilliSeconds, int maxSize) {
		this.numberOfReplicas = numberOfReplicas;

		for (int i = 0; i < totalNodes; i++) {
			LruCache cache = new LruCache("con_lru_" + i, timeoutMilliSeconds,
					maxSize);
			add(cache);
			caches.add(cache);
		}

	}

	public void add(LruCache node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			System.out.println(getHashValue(node.toString() + i));
			circle.put(getHashValue(node.toString() + i), node);
		}
	}

	public List<LruCache> getCaches() {
		return caches;
	}

	public void remove(LruCache node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(getHashValue(node.toString() + i));
		}
	}

	public LruCache get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = getHashValue(key.toString());
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, LruCache> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	public int getHashValue(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageDigest.update(str.getBytes());
		String encryptedString = new String(messageDigest.digest());
		int hash = 0;
		for (int i = 0; i < encryptedString.length(); i++) {
			hash += encryptedString.charAt(i);
		}
		System.out.println(hash);
		return hash;
	}

	public void CacheHitRatio() {
		double hitRatio = 0;
		double missRatio = 0;
		double total = 0;
		List<LruCache> caches = getCaches();
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
