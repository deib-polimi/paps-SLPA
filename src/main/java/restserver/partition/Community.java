package restserver.partition;

import restserver.partitiondata.Host;

import java.util.LinkedList;
import java.util.List;

public class Community {

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

    public void addLeader(Host node){
        if (members.contains(node)) throw new RuntimeException("This member is already present in the community");
        members.add(node);
    }

    public void addMember(Host member){
        if (members.contains(member)) throw new RuntimeException("This member is already present in the community");
        members.add(member);
    }

    public void setMembers(List<Host> orderedMembers) {
        this.members.clear();
        this.members.addAll(orderedMembers);
    }
}
