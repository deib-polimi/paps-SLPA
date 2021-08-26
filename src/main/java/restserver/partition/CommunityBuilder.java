package restserver.partition;

import restserver.partitiondata.Host;

import java.util.*;

public class CommunityBuilder {

    private final Map<String, Community> map;

    public CommunityBuilder(){ this.map = new HashMap<>(); }

    // If a community with that label already exists, return that community. Otherwise create a new community.
    public Community getCommunity(String label) {
        Community value = map.putIfAbsent(label, new Community("community-" + map.size()));
        return value == null ? map.get(label) : value;
    }

    /**
     * Given a community, returns a list of community of given sizes. This method DOES NOT update the map of this community
     * builder
     * @param community community that has to be decomposed
     * @param maxSize max size that a community can have
     * @return communities with members from the original community, with size < maxSize
     */
    public static List<Community> decomposeCommunity(Community community, int maxSize){
        List<Host> allMembers = community.getMembers();
        //if i have 12 members, and the max capacity is 5, the number of subCommunities is 12/5 + 1 = 2 + 1 = 3
        int nOfCommunities = allMembers.size() / maxSize;
        if(allMembers.size()%maxSize != 0){
            nOfCommunities += 1;
        }

        List<Community> communities = new ArrayList<>(nOfCommunities);
        //create nOfCommunities communities
        for(int i = 0; i < nOfCommunities; i++){
            Community newCommunity = new Community("");
            communities.add(newCommunity);
        }

        // ROUND ROBIN
        for(int i = 0; i < allMembers.size(); i++){
            Community selected = communities.get(i%nOfCommunities);
            if ( i < nOfCommunities){
                Host leader = allMembers.get(i);
                List<String> leaderName =  Arrays.stream(leader.getName().split("-")).toList();
                String leaderId = leaderName.get(leaderName.size() - 1);
                selected.setName("community-" + leaderId);
                addLeader(selected,  leader);
            }
            else{
                addMember(selected, allMembers.get(i));
            }
        }

        return communities;
    }

    private static void addLeader(Community community, Host leader){
        community.addLeader(leader);
    }

    private static void addMember(Community community, Host member){
        community.addMember(member);
    }
}
