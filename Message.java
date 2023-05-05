import java.io.Serializable;

public class Message implements Serializable {
    public enum MessageType {
        HANDSHAKE,
        LAYERED_BFS_NEW_PHASE,
        LAYERED_BFS_SEARCH,
        LAYERED_BFS_SEARCH_ACK_ACCEPTED,
        LAYERED_BFS_SEARCH_ACK_REJECTED
    }

    private int senderUID = -1;
    private MessageType type;

    private int treeDepth = -1;

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
