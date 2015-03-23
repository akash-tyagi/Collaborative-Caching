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
	protected SxLruNode findNodeByKey(Object key) {
		SxLruNode sxLruNode = null;
		for (LruCache lruCache : xlruCaches) {
			sxLruNode = (SxLruNode) lruCache.findNodeByKey(key);
			if (sxLruNode != null)
				return sxLruNode;
		}
		return null;
	}

	public void addObject(Object userKey, Object cacheObject) {

		CacheNode node;
		total += 1;

		node = findNodeByKey(userKey);

		if (node != null) {

			// if the node exists, then set it's value, and revalue it.
			// this is better than deleting it, because it doesn't require
			// more memory to be allocated
			hitRatio = hitRatio + 1;
			node.setValue(cacheObject);
			revalueNode(node);
		} else {
			missRatio += 1;
			shrinkToSize(getMaxSize() - 1);
			createNode(userKey, cacheObject);
		}

		removeExpiredElements();

		// checkFreeMemory();
	}

	@Override
	protected void revalueNode(CacheNode node) {
		SxLruNode xlruNode = (SxLruNode) node;
		LruCache lruCache = xlruCaches.get(xlruNode.getCacheNumber());
		if (xlruNode.getCacheNumber() == x - 1) {
			lruCache.revalueNode(xlruNode);
		} else {
			lruCache.delete(xlruNode);
			xlruCaches.get(xlruNode.getCacheNumber() + 1).addObject(
					xlruNode.getValue(), new Integer(1));
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
		// TODO Auto-generated method stub
		return null;
	}

}
