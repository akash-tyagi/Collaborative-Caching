package CacheImprovement;

public class PriorityCacheNode {
	// String key;
	int frequency;
	public long recency;
	double priority;
	public static double max_prio = 0;
	public static double max_rec = 0;
	public static double max_freq = 0;

	public final double alpha = 1;// 700000;
	public final double beta = 10;// 25;

	public PriorityCacheNode() {
		frequency = 0;
		recency = 0;
		priority = 0.0;
	}

	public PriorityCacheNode(int f, long r, double p) {
		frequency = f;
		recency = r;
		priority = p;
	}

	public void setValue(int freq, long rec) {
		this.frequency = this.frequency + 1;

		double freq_factor = alpha * (Math.log(this.frequency)/Math.log(2));

		double rec_factor = 0;
		if (rec - this.recency > 1)
			rec_factor = beta * (1 / Math.log10(((double) rec - this.recency)));

		this.priority = freq_factor + rec_factor;
		if (max_prio < this.priority) {
			max_prio = this.priority;
			max_freq = this.frequency;
			max_rec = rec - this.recency;
		}

		System.out.println("rec:" + (rec - this.recency) + " recf:"
				+ rec_factor + " freq:" + this.frequency + " freqf:"
				+ freq_factor + " priority :" + this.priority);

		this.recency = rec;
	}
}
