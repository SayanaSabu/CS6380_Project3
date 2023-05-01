public class LayeredBFS {
    private Node currNode;

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

    public void handleIncomingMessages() {
        Message currMessage = this.currNode.popLatestReceivedMessage();
        if (currMessage.getSenderUID() == -1)
            return;

        switch (currMessage.getType()) {
            case LAYERED_BFS_SEARCH:
                this.handleSearchMessage(currMessage);
                break;

            default:
                this.currNode.addReceivedMessage(currMessage);
        }
    }

    public void handleSearchMessage(Message msg) {
        Message.MessageType type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_REJECTED;

        if (this.currNode.getParentUID() == -1) {
            this.currNode.setParent(msg.getSenderUID(), msg.getTreeDepth());
            type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_ACCEPTED;
        }

        Message reply = new Message(this.currNode.getUID(), type);
        this.currNode.messageParent(reply);
    }

    public void sendSearch() {
        Message msg = new Message(
                this.currNode.getUID(),
                Message.MessageType.LAYERED_BFS_SEARCH,
                this.currNode.getTreeDepth());

        this.currNode.messageChildren(msg);
    }
}
