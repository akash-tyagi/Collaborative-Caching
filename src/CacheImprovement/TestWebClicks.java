package CacheImprovement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.xml.crypto.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestWebClicks {
	public static void main(String[] args) throws Exception {
		testLru5();
	}

	/**
	 * Method testLru5
	 */
	public static void testLru5() throws Exception {
		int CACHE_SIZE = 1000;

		LruCache cache_lru = new LruCache("lru", 5000, CACHE_SIZE);
		PriorityCache cache_pr = new PriorityCache(CACHE_SIZE);
		LfuCache cache_lfu = new LfuCache("lfu", 5000, CACHE_SIZE);
		FifoCache cache_fifo = new FifoCache("fifo", 5000, CACHE_SIZE);
		SxLruCache cache_sxlru = new SxLruCache("sxlru", 5000, CACHE_SIZE, 5);
		CollaborativeCache collaborativeLruCaching = new CollaborativeCache(10,
				2, 5000, CACHE_SIZE);

		DataAnalysis analysis = new DataAnalysis();

		String target_dir = "/home/akash/workspace/CollaborativeCache/src/test";
		File dir = new File(target_dir);
		File[] jsonFiles = dir.listFiles();

		for (File jsonFile : jsonFiles) {
			if (!jsonFile.isFile())
				continue;

			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			String line = br.readLine();
			JSONParser jsonParser = new JSONParser();

			while (line != null) {
				JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
				String link = (String) jsonObject.get("to");

				cache_lru.addObject(link, new Integer(1));
				cache_lfu.addObject(link, new Integer(1));
				// cache_fifo.addObject(link, new Integer(1));
				// cache_sxlru.addObject(link, new Integer(1));
				cache_pr.addObject(link);
				// collaborativeLruCaching.addObject(link, new Integer(1));
				// analysis.addObject(link, true);

				line = br.readLine();
			}
			br.close();
		}

		System.out.println("LRU");
		cache_lru.CacheHitRatio();
		System.out.println("LFU");
		cache_lfu.CacheHitRatio();
		// System.out.println("FIFO");
		// cache_fifo.CacheHitRatio();
		// System.out.println("SxLru");
		// cache_sxlru.CacheHitRatio();
		System.out.println("Priority Caching");
		cache_pr.CacheHitRatio();
		// System.out.println("Collab Caching");
		// collaborativeLruCaching.CacheHitRatio();
		// analysis.printTopNdata(true);

	}
}
