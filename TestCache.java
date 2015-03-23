package CacheImprovement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.codehaus.jackson.map.*;
import org.codehaus.jackson.*;

public class TestCache {

	public static void main(String[] args) throws Exception {
		testLru5();
	}

	/**
	 * Method testLru5
	 */
	public static void testLru5() throws Exception {

		LruCache cache = new LruCache("lru", 5000, 60000);
		LfuCache cache_lfu = new LfuCache("lfu", 5000, 60000);
		FifoCache cache_fifo = new FifoCache("fifo", 5000, 60000);
		SxLruCache cache_sxlru = new SxLruCache("sxlru", 5000, 60000, 1);

		// Nikhil Start
		String target_dir = "/home/akash/workspace/CollaborativeCache/src/test";
		File dir = new File(target_dir);
		File[] jsonFiles = dir.listFiles();

		JsonFactory f = new MappingJsonFactory();

		for (File jsonFile : jsonFiles) {
			if (!jsonFile.isFile())
				continue;

			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			String entry = br.readLine();

			while (entry != null) {
				JsonParser jp = f.createJsonParser(entry);

				JsonToken current;

				current = jp.nextToken();
				if (current != JsonToken.START_OBJECT) {
					System.out
							.println("Error: root should be object: quiting.");
					return;
				}

				while (jp.nextToken() != JsonToken.END_OBJECT) {
					String fieldName = jp.getCurrentName();
					// move from field name to field value
					current = jp.nextToken();
					if (fieldName.equals("hashtags")) {
						if (current == JsonToken.START_ARRAY) {
							// For each of the records in the array
							while (jp.nextToken() != JsonToken.END_ARRAY) {
								// read the record into a tree model,
								// this moves the parsing position to the end of
								// it
								JsonNode node = jp.readValueAsTree();
								// And now we have random access to everything
								// in the object
								// System.out.println(node.getValueAsText());
								cache.addObject(node.getValueAsText(),
										new Integer(1));
								cache_lfu.addObject(node.getValueAsText(),
										new Integer(1));
								cache_fifo.addObject(node.getValueAsText(),
										new Integer(1));
								cache_sxlru.addObject(node.getValueAsText(),
										new Integer(1));
							}
						} else {
							System.out
									.println("Error: records should be an array: skipping.");
							jp.skipChildren();
						}
					} else {
						jp.skipChildren();
					}
				}

				entry = br.readLine();
			}

		}

		System.out.println("LRU");
		cache.CacheHitRatio();
		System.out.println("LFU");
		cache_lfu.CacheHitRatio();
		System.out.println("FIFO");
		cache_fifo.CacheHitRatio();
		System.out.println("SxLru");
		cache_sxlru.CacheHitRatio();

		// Nikhil End

		// cache.addObject("A", new Integer(123456789));
		// cache.addObject("B", "123456789");
		// cache.addObject("C", new Long(123456789));
		// cache.addObject("D", Boolean.TRUE);
		// cache.addObject("E", new Float(1.2345));
		// System.out.println(cache.dumpFifoKeys());
		// assertEquals("EDCBA", cache.dumpFifoKeys());
		// assertEquals("EDCBA", cache.dumpLruKeys());
		// assertNotNull(cache.getObject("C"));
		// assertEquals("EDCBA", cache.dumpFifoKeys());
		// assertEquals("CEDBA", cache.dumpLruKeys());
		// cache.addObject("F", new Object());
		// assertEquals("FEDCB", cache.dumpFifoKeys());
		// assertEquals("FCEDB", cache.dumpLruKeys());
	}

	/**
	 * Method testReValue
	 */
	/*
	 * public void testLru10() {
	 * 
	 * LruCache cache = new LruCache("lru", 5000, 10);
	 * 
	 * cache.addObject("A", "A"); //1 assertEquals("A", cache.dumpLruKeys());
	 * assertEquals("A", cache.dumpFifoKeys()); cache.addObject("B", "B"); //2
	 * assertEquals("BA", cache.dumpLruKeys()); assertEquals("BA",
	 * cache.dumpFifoKeys()); cache.addObject("C", "C"); //3 assertEquals("CBA",
	 * cache.dumpLruKeys()); assertEquals("CBA", cache.dumpFifoKeys());
	 * cache.addObject("D", "D"); //4 assertEquals("DCBA", cache.dumpLruKeys());
	 * assertEquals("DCBA", cache.dumpFifoKeys()); cache.addObject("E", "E");
	 * //5 assertEquals("EDCBA", cache.dumpLruKeys()); assertEquals("EDCBA",
	 * cache.dumpFifoKeys()); cache.addObject("F", "F"); //6
	 * assertEquals("FEDCBA", cache.dumpLruKeys()); assertEquals("FEDCBA",
	 * cache.dumpFifoKeys()); cache.addObject("G", "G"); //7
	 * assertEquals("GFEDCBA", cache.dumpLruKeys()); assertEquals("GFEDCBA",
	 * cache.dumpFifoKeys()); cache.addObject("H", "H"); //8
	 * assertEquals("HGFEDCBA", cache.dumpLruKeys()); assertEquals("HGFEDCBA",
	 * cache.dumpFifoKeys()); cache.addObject("I", "I"); //9
	 * assertEquals("IHGFEDCBA", cache.dumpLruKeys()); assertEquals("IHGFEDCBA",
	 * cache.dumpFifoKeys()); cache.addObject("J", "J"); //10
	 * assertEquals("JIHGFEDCBA", cache.dumpLruKeys());
	 * assertEquals("JIHGFEDCBA", cache.dumpFifoKeys());
	 * 
	 * // this should bump out A cache.addObject("K", "K"); //11
	 * assertEquals("KJIHGFEDCB", cache.dumpLruKeys());
	 * assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * 
	 * // observe the effect of getObject assertNotNull(cache.getObject("E"));
	 * assertEquals("EKJIHGFDCB", cache.dumpLruKeys());
	 * assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("G")); assertEquals("GEKJIHFDCB",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("C")); assertEquals("CGEKJIHFDB",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("E")); assertEquals("ECGKJIHFDB",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("J")); assertEquals("JECGKIHFDB",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("J")); assertEquals("JECGKIHFDB",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("E")); assertEquals("EJCGKIHFDB",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("B")); assertEquals("BEJCGKIHFD",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys());
	 * assertNotNull(cache.getObject("F")); assertEquals("FBEJCGKIHD",
	 * cache.dumpLruKeys()); assertEquals("KJIHGFEDCB", cache.dumpFifoKeys()); }
	 */

}