package CacheImprovement;

import java.util.ArrayList;
import java.util.List;

public class SxLruCache extends AbstractPolicyCache implements ReapableCache {

	List<LruCache> xlruCaches;
	int x;

	SxLruCache(String name, long timeoutMilliSeconds, int maxSize, int x) {
		super(name, timeoutMilliSeconds, maxSize / x);
		// TODO Auto-generated constructor stub
		this.x = x;
		xlruCaches = new ArrayList<LruCache>();
		for (int i = 0; i < x; i++) {
			xlruCaches.add(new LruCache("lru" + i, timeoutMilliSeconds, maxSize
					/ x));
		}
	}

	@Override
	public int size() {
		return xlruCaches.get(0).size();
	}

	@Override
	protected LruNode findNodeByKey(Object key) {
		LruNode lruNode = null;
		for (LruCache lruCache : xlruCaches) {
			lruNode = (LruNode) lruCache.findNodeByKey(key);
			if (lruNode != null)
				return lruNode;
		}
		return null;
	}

	public void addObject(Object userKey, Object cacheObject) {
		System.out.println("\nAdding Objectz");
		CacheNode node;
		total += 1;

		node = findNodeByKey(userKey);

		if (node != null) {
			System.out.println("Found");

			// if the node exists, then set it's value, and revalue it.
			// this is better than deleting it, because it doesn't require
			// more memory to be allocated
			hitRatio = hitRatio + 1;
			node.setValue(cacheObject);
			revalueNode(node);
		} else {
			System.out.println("Not Found");
			missRatio += 1;
			shrinkToSize(getMaxSize() - 1);
			createNode(userKey, cacheObject);
		}

		removeExpiredElements();

		// checkFreeMemory();
	}

	@Override
	protected void revalueNode(CacheNode node) {
		LruNode lruNode = (LruNode) node;
		LruCache lruCache = xlruCaches.get(lruNode.getCacheNumber());
		if (lruNode.getCacheNumber() == x - 1) {
			lruCache.revalueNode(lruNode);
		} else {
			lruCache.delete(lruNode);
			xlruCaches.get(lruNode.getCacheNumber() + 1).addObject(
					lruNode.getValue(), new Integer(1));
		}
	}

	@Override
	protected void delete(CacheNode node) {
		System.out.println("%%%Not implemented delete yet!!!");
	}

	@Override
	protected void removeLeastValuableNode() {
		xlruCaches.get(0).removeLeastValuableNode();
	}

	@Override
	public void removeExpiredElements() {
		for (LruCache lruCache : xlruCaches) {
			lruCache.removeExpiredElements();
		}
	}

	@Override
	protected CacheNode createNode(Object userKey, Object cacheObject) {
		return xlruCaches.get(0).createNode(userKey, cacheObject);
	}

}
