package CacheImprovement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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

		LruCache cache_lru = new LruCache("lru", 5000, 500);
		PriorityCache cache_pr = new PriorityCache(500);
		 LfuCache cache_lfu = new LfuCache("lfu", 5000, 5000);
		 FifoCache cache_fifo = new FifoCache("fifo", 5000, 5000);
		 SxLruCache cache_sxlru = new SxLruCache("sxlru", 5000, 5000, 5);


		String target_dir = "/Users/nishant/Documents/workspace-OS_project/Collaborative_Caching/test_data";
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
//				cache_lfu.addObject(link, new Integer(1));
//				cache_fifo.addObject(link, new Integer(1));
//				cache_sxlru.addObject(link, new Integer(1));				
				//cache_collab.addObject(link, new Integer(1));
				cache_pr.addObject(link);
				line = br.readLine();
			}
			br.close();
		}

		System.out.println("LRU");
		cache_lru.CacheHitRatio();
//		 System.out.println("LFU");
//		 cache_lfu.CacheHitRatio();
//		 System.out.println("FIFO");
//		 cache_fifo.CacheHitRatio();
//		 System.out.println("SxLru");
//		 cache_sxlru.CacheHitRatio();
		System.out.println("Priority Caching");
		//cache_collab.CacheHitRatio();
		cache_pr.CacheHitRatio();		

	}
}

