import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    // private ArrayList<Node> allNodes;
    private String hostName;
    private int leaderUID;
    private ArrayList<Node> neighbours = new ArrayList<Node>();
    // private List<TCPClient> neighbourClients = Collections.synchronizedList(new
    // ArrayList<TCPClient>());
    private int port;
    private int UID;
    // private List<Message> receivedMessages = Collections.synchronizedList(new
    // ArrayList<Message>());

    // // variables for building BFS tree
    // private List<Integer> childNodes = new ArrayList<Integer>();
    // private int parentUID;
    // private boolean visited = false;

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
        this.neighbours.add(neighbour);
    }

    // public void addNeighbourClient(TCPClient client) {
    // synchronized (this.neighbourClients) {
    // this.neighbourClients.add(client);
    // }
    // }

    // public void addReceivedMessage(Message msg) {
    // synchronized (this.receivedMessages) {
    // this.receivedMessages.add(msg);
    // }
    // }

    // public boolean areAllNeighboursOnline() {
    // return this.neighbours.size() == this.neighbourClients.size();
    // }

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

    public ArrayList<Node> getNeighbourNodes() {
        return this.neighbours;
    }

    // public int getParentUID() {
    // return this.parentUID;
    // }

    public int getPort() {
        return this.port;
    }

    // public List<Message> getReceivedMessages() {
    // return this.receivedMessages;
    // }

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

    // public boolean isNodeLeader() {
    // return this.UID == this.leaderUID;
    // }

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

    // public Message popLatestReceivedMessage() {
    // return this.receivedMessages.size() > 0
    // ? this.receivedMessages.remove(0)
    // : new Message();
    // }

    // public void setAllNodes(ArrayList<Node> allNodes) {
    // this.allNodes = allNodes;
    // }

    public void setLeaderUID(int leaderUID) {
        this.leaderUID = leaderUID;
    }

    // public void setParent(int parentUID) {
    // this.parentUID = parentUID;
    // }

    // public void setVisited(boolean visited) {
    // this.visited = visited;
    // }

    // public void startBFSBuild() {
    // this.childNodes.clear();
    // this.parentUID = -1;
    // this.visited = false;
    // }

    // public void startLeaderElection() {
    // this.leaderUID = -1;
    // }
}