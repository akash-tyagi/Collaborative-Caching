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

		LruCache cache_lru = new LruCache("lru", 5000, 60000);

		ConsistenHashing collaborativeLruCaching = new ConsistenHashing(10, 2,
				5000, 60000);
		LfuCache cache_lfu = new LfuCache("lfu", 5000, 60000);
		FifoCache cache_fifo = new FifoCache("fifo", 5000, 60000);
		SxLruCache cache_sxlru = new SxLruCache("sxlru", 5000, 10, 5);
		System.out.println("1");

		// Nikhil Start
		String target_dir = "/home/akash/workspace/CollaborativeCache/src/test";
		File dir = new File(target_dir);
		File[] jsonFiles = dir.listFiles();
		JSONParser jsonParser = new JSONParser();

		for (File jsonFile : jsonFiles) {
			if (!jsonFile.isFile())
				continue;

			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			String line = br.readLine();

			while (line != null) {
				JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
				String link = (String) jsonObject.get("to");
				System.out.println(link);
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
		System.out.println("Consistent_Hashing");
	}
}