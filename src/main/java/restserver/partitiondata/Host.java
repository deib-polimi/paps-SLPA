package restserver.partitiondata;

import java.util.Map;

public class Host {

    private final String name;
    private final float memory;
    private final Map<String, String> labels;

    public Host(String name, float memory, Map<String, String> labels) {
        this.name = name;
        this.memory = memory;
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public float getMemory() {
        return memory;
    }

    public void putLabelsItem(String roleKey, String role) {
        labels.put(roleKey, role);
    }

    public String getLabelsItem(String roleKey) {
        return labels.getOrDefault(roleKey, "");
    }
}
