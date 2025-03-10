package restserver.partitiondata;

import com.google.gson.annotations.SerializedName;

public class PartitionParameters {
    @SerializedName("community-size")
    private final int nodes;
    private final int iterations;

    @SerializedName("maximum-delay")
    private final int maximumDelay;

    @SerializedName("probability-threshold")
    private final int probabilityThreshold;

    public PartitionParameters(int nodes, int iterations, int delayThreshold, int probabilityThreshold) {
        this.nodes = nodes;
        this.iterations = iterations;
        this.maximumDelay = delayThreshold;
        this.probabilityThreshold = probabilityThreshold;
    }

    public int getNodes() {
        return nodes;
    }

    public int getIterations() {
        return iterations;
    }

    public int getMaximumDelay() {
        return maximumDelay;
    }

    public float getProbabilityThreshold() {
        return (float)probabilityThreshold / 100;
    }
}
