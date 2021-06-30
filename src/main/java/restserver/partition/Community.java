package restserver.partition;

import restserver.partitiondata.Host;

import java.util.LinkedList;
import java.util.List;

public class Community {

    private static final String ROLE_KEY= "edgeautoscaler.polimi.it/role";

    private static final String COMMUNITY_KEY= "community";

    private String name;

    private final List<Host> members;

    public Community(String name){
        this.name = name;
        members = new LinkedList<>();
    }

    public List<Host> getMembers() {
        return new LinkedList<>(members);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addLeader(Host node){
        if (members.contains(node)) throw new RuntimeException("This member is already present in the community");
        members.add(node);
        setRoleLabel(node, Role.LEADER);
//        setCommunityLabel(node);
    }

    public void addMember(Host member){
        if (members.contains(member)) throw new RuntimeException("This member is already present in the community");
        members.add(member);
        setRoleLabel(member, Role.MEMBER);
//        setCommunityLabel(member);
    }

    private void setRoleLabel(Host node, Role role){
        node.putLabelsItem(ROLE_KEY, role.toString());
    }

    private void setCommunityLabel(Host node){
        node.putLabelsItem(COMMUNITY_KEY, this.name);
    }
}
