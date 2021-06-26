package restserver.partitiondata;

import com.google.gson.annotations.SerializedName;

public class PartitionParameters {
    private final int nodes;
    private final int iterations;

    @SerializedName("delay-threshold")
    private final float delayThreshold;

    @SerializedName("probability-threshold")
    private final float probabilityThreshold;

    public PartitionParameters(int nodes, int iterations, float delayThreshold, int probabilityThreshold) {
        this.nodes = nodes;
        this.iterations = iterations;
        this.delayThreshold = delayThreshold;
        this.probabilityThreshold = probabilityThreshold;
    }

    public int getNodes() {
        return nodes;
    }

    public int getIterations() {
        return iterations;
    }

    public float getDelayThreshold() {
        return delayThreshold;
    }

    public float getProbabilityThreshold() {
        return probabilityThreshold;
    }
}
