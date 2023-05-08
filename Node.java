import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    private String hostName;
    private HashMap<Integer, Node> neighbours = new HashMap<>();
    private int port;
    private List<Message> receivedMessages = Collections.synchronizedList(new ArrayList<Message>());
    private int UID;

    private ArrayList<Node> childNodes = new ArrayList<Node>();
    private int leaderUID;
    private int parentUID = -1;

    // Used only by leader
    private int depth = 0;
    private int maxDegree = -1;

    // Used only by non-leader nodes
    private int treeLevel = -1;

    public Node() {
    }

    public Node(int UID, String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        this.UID = UID;
    }

    public void addChildNode(int childUID) {
        Node childNode = this.neighbours.get(childUID);
        this.childNodes.add(childNode);

        this.setMaxDegree(this.getDegree());

        // String childrenStr = "";
        // for (Node child : this.childNodes) {
        // childrenStr += child.getUID() + " ";
        // }

        // System.out.println("Updated children: " + childrenStr);
    }

    public void addNeighbour(Node neighbour) {
        this.neighbours.put(neighbour.getUID(), neighbour);
    }

    public void addReceivedMessage(Message msg) {
        synchronized (this.receivedMessages) {
            this.receivedMessages.add(msg);
        }
    }

    // public void endLeaderElection(int leaderUID) {
    // this.leaderUID = leaderUID;
    // this.receivedMessages.clear();
    // }

    // public List<Integer> getChildNodes() {
    // return this.childNodes;
    // }

    public int getChildrenCount() {
        return this.childNodes.size();
    }

    public int getDegree() {
        return this.isNodeLeader()
                ? this.childNodes.size()
                : this.childNodes.size() + 1;
    }

    public String getHostName() {
        return this.hostName;
    }

    public ArrayList<Node> getNeighbours() {
        return new ArrayList<>(this.neighbours.values());
    }

    public int getParentUID() {
        return this.parentUID;
    }

    public int getPort() {
        return this.port;
    }

    public List<Message> getReceivedMessages() {
        return this.receivedMessages;
    }

    public int getTreeDepth() {
        return this.depth;
    }

    public int getTreeLevel() {
        return this.treeLevel;
    }

    public int getUID() {
        return this.UID;
    }

    public void increaseTreeDepth() {
        this.depth += 1;
    }

    // public boolean isNodeChild(int nodeUID) {
    // for (int childUID : this.childNodes) {
    // if (nodeUID == childUID)
    // return true;
    // }
    // return false;
    // }

    public boolean isNodeLeader() {
        return this.UID == this.leaderUID;
    }

    // public boolean isNodeNeighbour(int UID) {
    // for (int neighbour : this.neighbours) {
    // if (UID == neighbour)
    // return true;
    // }
    // return false;
    // }

    public void messageAllChildren(Message msg) {
        for (Node child : this.childNodes) {
            new TCPClient(this, child).sendMessage(msg);
        }
    }

    public void messageAllNeighbours(Message msg) {
        for (Map.Entry<Integer, Node> n : this.neighbours.entrySet()) {
            new TCPClient(this, n.getValue()).sendMessage(msg);
        }
    }

    public void messageNeighbour(Message msg, int neighbourUID) {
        Node neighbourNode = this.neighbours.get(neighbourUID);
        new TCPClient(this, neighbourNode).sendMessage(msg);
    }

    public void messageParent(Message msg) {
        Node parentNode = this.neighbours.get(this.parentUID);
        new TCPClient(this, parentNode).sendMessage(msg);
    }

    public Message popLatestReceivedMessage() {
        return this.receivedMessages.size() > 0
                ? this.receivedMessages.remove(0)
                : new Message();
    }

    public void setMaxDegree(int newDegree) {
        this.maxDegree = Math.max(this.maxDegree, newDegree);
    }

    public void setLeader(int leaderUID) {
        this.leaderUID = leaderUID;
    }

    public void setParent(int parentUID, int rootTreeDepth) {
        this.parentUID = parentUID;
        this.treeLevel = rootTreeDepth + 1;

        System.out.println("Updated parent: " + this.parentUID + "\nTree level: " + this.treeLevel);
    }
}
