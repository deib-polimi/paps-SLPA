package restserver.partitiondata;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartitionData {

    private final PartitionParameters parameters;
    private final List<Host> hosts;

    @SerializedName("delay-matrix")
    private final DelayMatrix matrix;

    /**
     * Constructor
     * @param parameters
     * @param hosts
     * @param matrix
     */
    public PartitionData(PartitionParameters parameters, List<Host> hosts, DelayMatrix matrix) {
        this.parameters = parameters;
        this.hosts = hosts;
        this.matrix = matrix;
    }

    public PartitionParameters getParameters() {
        return parameters;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public DelayMatrix getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this, PartitionData.class);
    }
}

