package restserver.partitiondata;

import java.util.Map;

public class Host {

    private final String name;
    private final Map<String, String> labels;

    public Host(String name, Map<String, String> labels) {
        this.name = name;
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void putLabelsItem(String roleKey, String role) {
        labels.put(roleKey, role);
    }

    public String getLabelsItem(String roleKey) {
        return labels.getOrDefault(roleKey, "");
    }
}
