package CacheImprovement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestTwitterTags {

	public static void main(String[] args) throws Exception {
		int CACHE_SIZE = 500;

		LruCache cache_lru = new LruCache("lru", 5000, CACHE_SIZE);

		// CollaborativeCache collaborativeLruCaching = new
		// CollaborativeCache(10,
		// 2, 5000, CACHE_SIZE);
		// LfuCache cache_lfu = new LfuCache("lfu", 5000, CACHE_SIZE);
		// FifoCache cache_fifo = new FifoCache("fifo", 5000, CACHE_SIZE);
		SxLruCache cache_sxlru = new SxLruCache("sxlru", 5000, CACHE_SIZE, 2);
		PriorityCache cache_pr = new PriorityCache(CACHE_SIZE);

		// Nikhil Start
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
				JSONArray tagsArray = (JSONArray) jsonObject.get("hashtags");
				for (Object object : tagsArray) {
					String hashTag = (String) object;

					cache_lru.addObject(hashTag, new Integer(1));
					// // //collaborativeLruCaching.addObject(hashTag, new
					// Integer(1));
					// cache_lfu.addObject(hashTag, new Integer(1));
					// cache_fifo.addObject(hashTag, new Integer(1));
					cache_sxlru.addObject(hashTag, new Integer(1));
					cache_pr.addObject(hashTag);
				}
				line = br.readLine();
			}
		}

		System.out.println("LRU");
		cache_lru.CacheHitRatio();
		// System.out.println("LFU");
		// cache_lfu.CacheHitRatio();
		// System.out.println("FIFO");
		// cache_fifo.CacheHitRatio();
		System.out.println("SxLru");
		cache_sxlru.CacheHitRatio();
		System.out.println("PriorityCache");
		cache_pr.CacheHitRatio();

	}

}
