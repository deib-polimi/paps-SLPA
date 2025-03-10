package restserver.partition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restserver.partitiondata.DelayMatrix;
import restserver.partitiondata.Host;
import restserver.partitiondata.PartitionData;

import java.util.*;

public class SLPA {

    private final PartitionData data;
    private List<SLPA_Node> topologyNodes; // list of all the nodes present in the topology
    private static final Logger logger = LoggerFactory.getLogger(SLPA.class);

    // constructor
    public SLPA(PartitionData data) throws RuntimeException {
        this.data = data;
        createTopologyNodesList(data.getHosts());
        computeTopologyMatrix(data.getMatrix(), data.getParameters().getMaximumDelay());
    }

    private void createTopologyNodesList(List<Host> hosts) {
        topologyNodes = new LinkedList<>();

        for (Host host : hosts) {
            topologyNodes.add(new SLPA_Node(host));
        }
    }

    private void computeTopologyMatrix(DelayMatrix delayMatrix, float maximumDelay) {
        for (int i = 0; i < delayMatrix.getRoutes().length; i++) {
            for (int j = i; j < delayMatrix.getRoutes()[i].length; j++) {
                if (i != j && delayMatrix.getRoutes()[i][j] < maximumDelay) {
                    topologyNodes.get(i).addNearbyNode(topologyNodes.get(j));
                    topologyNodes.get(j).addNearbyNode(topologyNodes.get(i));
                }
            }
        }
    }

    // function used to create and set up Kubernetes communities, returns a list
    // with all the communities created.
    // a community will contain information about members and leader.
    public List<Community> computeCommunities(int numberOfIterations, float probabilityThreshold) {

        List<Community> returnCommunities = new LinkedList<>();
        CommunityBuilder communityBuilder = new CommunityBuilder();

        if (topologyNodes.size() < 2) {
            Community selectedCommunity = communityBuilder.getCommunity("community-0");

            for (SLPA_Node node : topologyNodes) {
                List<String> nodeName = Arrays.stream(node.getHostName().split("-")).toList();
                String nodeId = nodeName.get(nodeName.size() - 1);

                if (nodeId.equals("0")) {
                    selectedCommunity.addLeader(node.getHost());
                } else {
                    selectedCommunity.addMember(node.getHost());
                }
            }
            returnCommunities.add(selectedCommunity);
            return returnCommunities;
        }

        List<SLPA_Node> listenerOrder = topologyNodes;

        // algorithm evolution, spread labels for a given number of iterations, ideal
        // number is 20 iterations
        for (int i = 0; i < numberOfIterations; i++) {

            Collections.shuffle(listenerOrder); // shuffle the list to better spread labels

            for (SLPA_Node listener : listenerOrder) {
                List<SLPA_Node> speakers = listener.getNearbyNodes();
                List<String> receivedLabels = new ArrayList<>(speakers.size());

                if (speakers.isEmpty()) {
                    // TODO: handle when a node is alone. 1) community composed by one node?
                    logger.error("Listener: {} has no nearby nodes, speaker list is empty", listener.getHostName());
                }

                Collections.shuffle(speakers); // shuffle the list to better spread labels

                speakers.forEach(s -> receivedLabels.add(s.speak()));

                listener.addToMemory(SLPA_Node.listen(receivedLabels));
                listener.setLabelToSpread();
            }
        }

        // post processing to set the communities
        for (SLPA_Node node : topologyNodes) {

            // get all the different labels received
            Set<String> communityCandidates = node.getMemory().getDifferentReceivedLabels();

            communityCandidates.removeIf(label -> node.computeOccurrenceProbability(label) < probabilityThreshold);

            List<String> candidates = communityCandidates.stream().toList();

            if (communityCandidates.isEmpty()) {
                // TODO: this should never happen
                logger.error("Node: {} does not belong to any community", node.getHostName());
            }

            String communitySelected = candidates.get(0);
            Community selectedCommunity = communityBuilder.getCommunity(communitySelected);

            List<String> nodeName = Arrays.stream(node.getHostName().split("-")).toList();
            String nodeId = nodeName.get(nodeName.size() - 1);

            List<String> communityName = Arrays.stream(communitySelected.split("-")).toList();
            String communityId = communityName.get(communityName.size() - 1);

            if (nodeId.equals(communityId)) {
                selectedCommunity.addLeader(node.getHost());
            } else {
                selectedCommunity.addMember(node.getHost());
            }

            if (!returnCommunities.contains(selectedCommunity)) {
                returnCommunities.add(selectedCommunity);
            }
        }

        returnCommunities = shrinkCommunities(returnCommunities, data.getParameters().getNodes());

        return returnCommunities;
    }

    private List<Community> shrinkCommunities(List<Community> communities, int maxSize) {
        List<Community> returnCommunities = new LinkedList<>();
        for (Community community : communities) {
            List<Community> decomposed = CommunityBuilder.decomposeCommunity(community, maxSize);
            returnCommunities.addAll(decomposed);
        }

        return returnCommunities;
    }

}