package CacheImprovement;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistenHashing<T> {

	private final int numberOfReplicas;
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();
	private final List<T> caches = new ArrayList<T>();

	public ConsistenHashing(int numberOfReplicas, Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		for (T node : nodes) {
			add(node);
			caches.add(node);
		}
	}

	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			System.out.println(getHashValue(node.toString() + i));
			circle.put(getHashValue(node.toString() + i), node);
		}
	}

	public List<T> getCaches() {
		return caches;
	}

	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(getHashValue(node.toString() + i));
		}
	}

	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = getHashValue(key.toString());
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
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
}
