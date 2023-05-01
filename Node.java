import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Node {
    private String hostName;
    private HashMap<Integer, Node> neighbours = new HashMap<>();
    private int port;
    private List<Message> receivedMessages = Collections.synchronizedList(new ArrayList<Message>());
    private int UID;

    private int leaderUID;

    private ArrayList<Node> childNodes = new ArrayList<Node>();
    private int depth = 0;
    private int parentUID = -1;
    private int treeLevel = -1;

    public Node() {
    }

    public Node(int UID, String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        this.UID = UID;
    }

    // public void addChildNode(int childUID) {
    // this.childNodes.add(childUID);
    // }

    public void addNeighbour(Node neighbour) {
        this.neighbours.put(neighbour.getUID(), neighbour);
    }

    // public void addNeighbourClient(TCPClient client) {
    // this.neighbourClients.put(client.getServerNode().getUID(), client);
    // }

    public void addReceivedMessage(Message msg) {
        synchronized (this.receivedMessages) {
            this.receivedMessages.add(msg);
        }
    }

    // public void endLeaderElection(int leaderUID) {
    // this.leaderUID = leaderUID;
    // this.receivedMessages.clear();
    // }

    // public ArrayList<Node> getAllNodes() {
    // return this.allNodes;
    // }

    // public List<Integer> getChildNodes() {
    // return this.childNodes;
    // }

    // public TCPClient getClientConnection(int clientUID) {
    // for (TCPClient client : this.neighbourClients) {
    // if (clientUID == client.getServerNode().getUID())
    // return client;
    // }
    // return new TCPClient();
    // }

    // public int getDegree() {
    // return this.isNodeLeader()
    // ? this.childNodes.size()
    // : this.childNodes.size() + 1;
    // }

    public String getHostName() {
        return this.hostName;
    }

    // public List<TCPClient> getNeighbourClients() {
    // return this.neighbourClients;
    // }

    public ArrayList<Node> getNeighbours() {
        return new ArrayList<>(this.neighbours.values());
    }

    public int getParentUID() {
        return this.parentUID;
    }

    public int getPort() {
        return this.port;
    }

    // public List<Message> getReceivedMessages() {
    // return this.receivedMessages;
    // }

    public int getTreeDepth() {
        return this.depth;
    }

    public int getUID() {
        return this.UID;
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

    // public boolean isNodeVisited() {
    // return this.visited;
    // }

    public void messageChildren(Message msg) {
        for (Node child : this.childNodes) {
            new TCPClient(this, child).sendMessage(msg);
        }
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

    // public void setAllNodes(ArrayList<Node> allNodes) {
    // this.allNodes = allNodes;
    // }

    public void setLeader(int leaderUID) {
        this.leaderUID = leaderUID;
    }

    public void setParent(int parentUID, int rootTreeDepth) {
        this.parentUID = parentUID;
        this.treeLevel = rootTreeDepth + 1;
    }

    // public void setVisited(boolean visited) {
    // this.visited = visited;
    // }

    // public void startBFSBuild() {
    // this.childNodes.clear();
    // this.parentUID = -1;
    // this.visited = false;
    // }

    public void startLayeredBFS() {
        this.childNodes.clear();
        this.depth = 0;
        this.parentUID = -1;
    }

    // public void startLeaderElection() {
    // this.leaderUID = -1;
    // }
}
