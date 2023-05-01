public class LayeredBFS {
    private Node currNode;
    private int searchAckNeighboursCount = 0;

    public LayeredBFS(Node currNode) {
        this.currNode = currNode;
    }

    public void buildTree() {
        System.out.println("LayeredBFS build tree started");

        if (this.currNode.isNodeLeader()) {
            this.currNode.startLayeredBFS();
            this.sendSearch();
        }

        while (true) {
            this.handleIncomingMessages();
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

            default:
                return;
        }
    }

    private void handleSearchAckMessage(Message msg) {
        this.searchAckNeighboursCount += 1;

        if (msg.getType() == Message.MessageType.LAYERED_BFS_SEARCH_ACK_ACCEPTED) {
            this.currNode.addChildNode(msg.getSenderUID());
        }
    }

    private void handleSearchMessage(Message msg) {
        Message.MessageType type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_REJECTED;

        if (this.currNode.getParentUID() == -1) {
            this.currNode.setParent(msg.getSenderUID(), msg.getTreeDepth());
            type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_ACCEPTED;
        }

        Message reply = new Message(this.currNode.getUID(), type);
        this.currNode.messageParent(reply);
    }

    private void sendSearch() {
        Message msg = new Message(
                this.currNode.getUID(),
                Message.MessageType.LAYERED_BFS_SEARCH,
                this.currNode.getTreeDepth());

        this.currNode.messageChildren(msg);
    }
}
