package restserver.partitiondata;

import restserver.partition.Community;

import java.util.List;
import java.util.Map;

public class PartitionResult {
    private final List<Community> communities;

    public PartitionResult(List<Community> communities) {
        this.communities = communities;
    }
}
