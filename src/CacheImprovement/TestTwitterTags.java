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
		LruCache cache_lru = new LruCache("lru", 5000, 60000);

		CollaborativeCache collaborativeLruCaching = new CollaborativeCache(10, 2,
				5000, 60000);
		LfuCache cache_lfu = new LfuCache("lfu", 5000, 60000);
		FifoCache cache_fifo = new FifoCache("fifo", 5000, 60000);
		SxLruCache cache_sxlru = new SxLruCache("sxlru", 5000, 10, 5);

		// Nikhil Start
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
				JSONArray tagsArray = (JSONArray) jsonObject.get("hashtags");
				for (Object object : tagsArray) {
					String hashTag = (String) object;

					cache_lru.addObject(hashTag, new Integer(1));
					collaborativeLruCaching.addObject(hashTag, new Integer(1));
					cache_lfu.addObject(hashTag, new Integer(1));
					cache_fifo.addObject(hashTag, new Integer(1));
					cache_sxlru.addObject(hashTag, new Integer(1));
				}
				line = br.readLine();
			}
		}

		System.out.println("LRU");
		cache_lru.CacheHitRatio();
		System.out.println("LFU");
		cache_lfu.CacheHitRatio();
		System.out.println("FIFO");
		cache_fifo.CacheHitRatio();
		System.out.println("SxLru");
		cache_sxlru.CacheHitRatio();

	}

}