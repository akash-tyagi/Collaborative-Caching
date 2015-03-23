package CacheImprovement;

public class SxLruNode extends LruNode {
	int x;

	public void setCacheNumber(int x) {
		this.x = x;
	}

	public int getCacheNumber() {
		return x;
	}
}
