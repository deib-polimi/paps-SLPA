package restserver.partitiondata;

import restserver.partition.Community;

import java.util.List;

public class PartitionResult {
    private final List<Community> communities;

    public PartitionResult(List<Community> communities) {
        this.communities = communities;
    }
}
