import java.io.Serializable;

public class Message implements Serializable {
    public enum MessageType {
        HANDSHAKE,
        LAYERED_BFS_NEW_PHASE,
        LAYERED_BFS_NEW_PHASE_COMPLETE,
        LAYERED_BFS_SEARCH,
        LAYERED_BFS_SEARCH_ACK_ACCEPTED,
        LAYERED_BFS_SEARCH_ACK_REJECTED
    }

    private int senderUID = -1;
    private MessageType type;

    private boolean childrenFound = false;
    private int maxDegree = -1;
    private int treeDepth = -1;
    private int treeLevel = -1;

    public Message() {
    }

    public Message(int senderUID, MessageType type) {
        this.senderUID = senderUID;
        this.type = type;
    }

    public Message(int senderUID, MessageType type, int treeDepth) {
        this.senderUID = senderUID;
        this.treeDepth = treeDepth;
        this.type = type;
    }

    public Message(int senderUID, MessageType type, int treeLevel, int maxDegree, boolean childrenFound) {
        this.childrenFound = childrenFound;
        this.maxDegree = maxDegree;
        this.senderUID = senderUID;
        this.treeLevel = treeLevel;
        this.type = type;
    }

    public boolean getChildrenFound() {
        return this.childrenFound;
    }

    public int getMaxDegree() {
        return this.maxDegree;
    }

    public int getSenderUID() {
        return this.senderUID;
    }

    public int getTreeDepth() {
        return this.treeDepth;
    }

    public MessageType getType() {
        return this.type;
    }
}
