package CacheImprovement;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class DataAnalysis {

	PriorityQueue<String> freqPrioQueue;
	HashMap<String, Integer> freqHashMap;

	PriorityQueue<String> recPrioQueue;
	HashMap<String, Long> recHashMap;
	long rec;
	long maxRec;

	public DataAnalysis() {
		freqHashMap = new HashMap<String, Integer>();
		freqPrioQueue = new PriorityQueue<String>(10, new Comparator<String>() {
			public int compare(String s1, String s2) {
				int n1 = freqHashMap.get(s1);
				int n2 = freqHashMap.get(s2);
				if (n1 < n2)
					return -1;
				if (n1 > n2)
					return 1;
				return 0;
			}
		});

		rec = 0;
		maxRec = 0;
		recHashMap = new HashMap<String, Long>();
		recPrioQueue = new PriorityQueue<String>(10, new Comparator<String>() {
			public int compare(String s1, String s2) {
				Long n1 = recHashMap.get(s1);
				Long n2 = recHashMap.get(s2);
				if (n1 < n2)
					return -1;
				if (n1 > n2)
					return 1;
				return 0;
			}
		});
	}

	public void addObject(String key, boolean isRec) {
		if (isRec) {
			rec++;
			if (recHashMap.containsKey(key)) {
				if (rec - recHashMap.get(key) > maxRec)
					maxRec = (rec - recHashMap.get(key));
			}
			recHashMap.put(key, rec);
		}

		if (freqHashMap.containsKey(key) == false)
			freqHashMap.put(key, 1);
		else
			freqHashMap.put(key, freqHashMap.get(key) + 1);
	}

	public void printTopNdata(boolean isRec) {
		if (isRec) {
			System.out.println("MaxRec:" + maxRec);
			return;
		}

		HashMap<String, Integer> hm2 = new HashMap<String, Integer>(freqHashMap);
		Iterator it = hm2.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			freqPrioQueue.add((String) pair.getKey());
			it.remove(); // avoids a ConcurrentModificationException
		}
		while (freqPrioQueue.size() > 0) {
			String key = freqPrioQueue.poll();
			System.out.println("Key:" + key + " Freq:" + freqHashMap.get(key));
		}
	}
}
