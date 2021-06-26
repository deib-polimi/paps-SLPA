package restserver.partition;

import java.util.*;


public class Memory {

    // attributes
    private final Map<String, Integer> memory;    //String will contain a label and Integer will represent the number of occurrences.
    private int totLabelReceived;   // the number of all the labels received

    // constructor
    public Memory(String nodeID) {
        this.totLabelReceived = 0;
        this.memory = new HashMap<>();
        this.updateMemory(nodeID);
    }


    // getters and setters
    public int getTotLabelReceived() {
        return totLabelReceived;
    }

    private void updateTotLabelReceived() {
        this.totLabelReceived++;
    }

    public Set<String> getDifferentReceivedLabels() {
        return new HashSet<>(memory.keySet());
    }

    public void updateMemory(String label) {
        memory.compute(label, (l, occurences) -> occurences != null ? occurences + 1: 1);
        this.updateTotLabelReceived();
    }

    public int getOccurrences(String label) {
        return memory.get(label);
    }


    // FUNCTIONS

    // this function returns a list of all the labels occurring in the memory
    // sorted by number of occurrences and sorted in ascending order
    public List<String> returnSortedLabels() {
        return memory.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .toList();
    }
}