public class LayeredBFS {
    private Node currNode;
    private int searchAckNeighboursCount = 0;

    public LayeredBFS(Node currNode) {
        this.currNode = currNode;
    }

    public void buildTree() {
        System.out.println("LayeredBFS started");

        if (this.currNode.isNodeLeader()) {
            Message msg = new Message(
                    this.currNode.getUID(),
                    Message.MessageType.LAYERED_BFS_SEARCH,
                    this.currNode.getTreeDepth());

            this.currNode.messageAllNeighbours(msg);
        }

        while (true) {
            this.handleIncomingMessages();
        }
    }

    private boolean didAllNeighboursReplyAck() {
        int neighboursCount = this.currNode.getNeighbours().size();

        if (this.currNode.isNodeLeader()) {
            return searchAckNeighboursCount == neighboursCount;
        } else {
            return searchAckNeighboursCount + 1 == neighboursCount;
        }
    }

    private void handleIncomingMessages() {
        Message currMessage = this.currNode.popLatestReceivedMessage();
        if (currMessage.getSenderUID() == -1)
            return;

        switch (currMessage.getType()) {
            case LAYERED_BFS_SEARCH:
                this.handleSearchMessage(currMessage);
                break;

            case LAYERED_BFS_SEARCH_ACK_ACCEPTED:
            case LAYERED_BFS_SEARCH_ACK_REJECTED:
                this.handleSearchAckMessage(currMessage);
                break;

            case LAYERED_BFS_NEW_PHASE:
                this.handleNewPhaseMessage(currMessage);
                break;

            default:
                this.currNode.addReceivedMessage(currMessage);
        }
    }

    private void handleNewPhaseMessage(Message msg) {
        if (msg.getTreeDepth() == this.currNode.getTreeLevel()) {
            Message newMsg = new Message(
                    this.currNode.getUID(),
                    Message.MessageType.LAYERED_BFS_SEARCH,
                    msg.getTreeDepth());

            this.currNode.messageAllNeighbours(newMsg);
        } else {
            Message newMsg = new Message(
                    this.currNode.getUID(),
                    Message.MessageType.LAYERED_BFS_NEW_PHASE,
                    msg.getTreeDepth());

            this.currNode.messageAllChildren(newMsg);
        }
    }

    private void handleSearchAckMessage(Message msg) {
        this.searchAckNeighboursCount += 1;

        if (msg.getType() == Message.MessageType.LAYERED_BFS_SEARCH_ACK_ACCEPTED) {
            this.currNode.addChildNode(msg.getSenderUID());
        }

        if (this.didAllNeighboursReplyAck()) {
            if (this.currNode.isNodeLeader()) {
                this.currNode.increaseTreeDepth();

                System.out.println("Layer 1 complete");
                System.out.println("Layer 2 started");

                Message newMsg = new Message(
                        this.currNode.getUID(),
                        Message.MessageType.LAYERED_BFS_NEW_PHASE,
                        this.currNode.getTreeDepth());

                this.currNode.messageAllChildren(newMsg);
            } else {
                System.out.println("All neighbours ack");
            }
        }
    }

    private void handleSearchMessage(Message msg) {
        Message.MessageType type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_REJECTED;

        if (this.currNode.getParentUID() == -1 && !this.currNode.isNodeLeader()) {
            this.currNode.setParent(msg.getSenderUID(), msg.getTreeDepth());
            type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_ACCEPTED;
        }

        Message reply = new Message(this.currNode.getUID(), type);
        this.currNode.messageNeighbour(reply, msg.getSenderUID());
    }
}
