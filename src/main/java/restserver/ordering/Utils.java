package restserver.ordering;


import restserver.partition.Community;
import restserver.partitiondata.Host;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Utils {
    public static List<Community> orderCommunities(List<Community> communities) {
        List<Community> orderedCommunities = new ArrayList<>();
        int id = 0;

        for (Community community : communities) {
            Community newCommunity = new Community("community-" + id);
            List<Host> orderedMembers = new ArrayList<>(community.getMembers());
            orderedMembers.sort(Comparator.comparing(Host::getName));
            newCommunity.setMembers(orderedMembers);
            orderedCommunities.add(id, newCommunity);
            id++;
        }

        return orderedCommunities;
    }
}
