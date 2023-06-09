public class LayeredBFS {
    private Node currNode;

    private boolean bfsComplete = false;
    private boolean childrenFound = false;
    private int maxDegree = -1;
    private int phaseCompleteChildrenCount = 0;
    private int searchAcceptNeighboursCount = 0;
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

        while (!this.bfsComplete) {
            this.handleIncomingMessages();
        }

        System.out.println("LayeredBFS complete");
    }

    private boolean didAllChildrenPhaseComplete() {
        int childrenCount = this.currNode.getChildrenCount();
        return this.phaseCompleteChildrenCount == childrenCount;
    }

    private boolean didAllNeighboursAck() {
        int neighboursCount = this.currNode.getNeighbours().size();
        return this.searchAckNeighboursCount == neighboursCount;
    }

    private void handleIncomingMessages() {
        Message currMessage = this.currNode.popLatestReceivedMessage();
        if (currMessage.getSenderUID() == -1)
            return;

        // System.out.println("Received " + currMessage.getType() + " from " +
        // currMessage.getSenderUID());

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

            case LAYERED_BFS_NEW_PHASE_COMPLETE:
                this.handleNewPhaseCompleteMessage(currMessage);
                break;

            case LAYERED_BFS_COMPLETE:
                this.handleBFSCompleteMessage(currMessage);
                break;

            default:
                return;
        }
    }

    private void handleBFSCompleteMessage(Message msg) {
        System.out.println("\nFinal connections"
                + "\nParent: " + this.currNode.getParentUID()
                + "\nChildren: " + this.currNode.getChildrenStr()
                + "\nDegree: " + this.currNode.getDegree()
                + "\nTree Level: " + this.currNode.getTreeLevel());

        if (this.currNode.getChildrenCount() > 0) {
            Message newMsg = new Message(this.currNode.getUID(), Message.MessageType.LAYERED_BFS_COMPLETE);
            this.currNode.messageAllChildren(newMsg);
        }

        this.bfsComplete = true;
    }

    private void handleNewPhaseCompleteMessage(Message msg) {
        this.phaseCompleteChildrenCount += 1;

        this.childrenFound = (this.childrenFound || msg.getChildrenFound());
        this.maxDegree = Math.max(this.maxDegree, msg.getMaxDegree());

        if (this.didAllChildrenPhaseComplete()) {
            if (!this.currNode.isNodeLeader()) {
                Message newMsg = new Message(
                        this.currNode.getUID(),
                        Message.MessageType.LAYERED_BFS_NEW_PHASE_COMPLETE,
                        msg.getTreeDepth(),
                        this.maxDegree,
                        this.childrenFound);

                this.currNode.messageParent(newMsg);
            } else if (this.childrenFound) {
                this.currNode.increaseTreeDepth();
                this.currNode.setMaxDegree(this.maxDegree);

                Message newMsg = new Message(
                        this.currNode.getUID(),
                        Message.MessageType.LAYERED_BFS_NEW_PHASE,
                        this.currNode.getTreeDepth());

                System.out.println("Layer " + this.currNode.getTreeDepth() + " complete\nStarting next layer");

                this.childrenFound = false;
                this.maxDegree = -1;
                this.phaseCompleteChildrenCount = 0;
                this.searchAcceptNeighboursCount = 0;
                this.searchAckNeighboursCount = 0;

                this.currNode.messageAllChildren(newMsg);
            } else {
                System.out.println("BFS complete");

                System.out.println("\nFinal connections"
                        + "\nParent: " + this.currNode.getParentUID()
                        + "\nChildren: " + this.currNode.getChildrenStr()
                        + "\nDegree: " + this.currNode.getDegree()
                        + "\nTree Level: " + this.currNode.getTreeLevel()
                        + "\nMax Degree: " + this.currNode.getMaxDegree());

                Message newMsg = new Message(this.currNode.getUID(), Message.MessageType.LAYERED_BFS_COMPLETE);
                this.currNode.messageAllChildren(newMsg);

                this.bfsComplete = true;
            }
        }
    }

    private void handleNewPhaseMessage(Message msg) {
        this.childrenFound = false;
        this.maxDegree = -1;
        this.phaseCompleteChildrenCount = 0;
        this.searchAcceptNeighboursCount = 0;
        this.searchAckNeighboursCount = 0;

        if (msg.getTreeDepth() == this.currNode.getTreeLevel()) {
            Message newMsg = new Message(
                    this.currNode.getUID(),
                    Message.MessageType.LAYERED_BFS_SEARCH,
                    msg.getTreeDepth());

            this.currNode.messageAllNeighbours(newMsg);
        } else if (this.currNode.getChildrenCount() > 0) {
            Message newMsg = new Message(
                    this.currNode.getUID(),
                    Message.MessageType.LAYERED_BFS_NEW_PHASE,
                    msg.getTreeDepth());

            this.currNode.messageAllChildren(newMsg);
        } else {
            Message newMsg = new Message(
                    this.currNode.getUID(),
                    Message.MessageType.LAYERED_BFS_NEW_PHASE_COMPLETE,
                    this.currNode.getTreeLevel(),
                    this.currNode.getDegree(),
                    false);

            this.currNode.messageParent(newMsg);
        }
    }

    private void handleSearchAckMessage(Message msg) {
        this.searchAckNeighboursCount += 1;

        if (msg.getType() == Message.MessageType.LAYERED_BFS_SEARCH_ACK_ACCEPTED) {
            this.searchAcceptNeighboursCount += 1;
            this.currNode.addChildNode(msg.getSenderUID());
        }

        if (this.didAllNeighboursAck()) {
            if (this.currNode.isNodeLeader()) {
                this.currNode.increaseTreeDepth();

                System.out.println("Layer 1 complete\nStarting next layer");

                Message newMsg = new Message(
                        this.currNode.getUID(),
                        Message.MessageType.LAYERED_BFS_NEW_PHASE,
                        this.currNode.getTreeDepth());

                this.currNode.messageAllChildren(newMsg);
            } else {
                Message newMsg = new Message(
                        this.currNode.getUID(),
                        Message.MessageType.LAYERED_BFS_NEW_PHASE_COMPLETE,
                        this.currNode.getTreeLevel(),
                        this.currNode.getDegree(),
                        this.searchAcceptNeighboursCount > 0);

                this.currNode.messageParent(newMsg);
            }
        }
    }

    private void handleSearchMessage(Message msg) {
        Message.MessageType type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_REJECTED;

        if (this.currNode.getParentUID() == -1 && !this.currNode.isNodeLeader()) {
            this.currNode.setParent(msg.getSenderUID(), msg.getTreeDepth());
            type = Message.MessageType.LAYERED_BFS_SEARCH_ACK_ACCEPTED;
        }

        Message ackMsg = new Message(this.currNode.getUID(), type);
        this.currNode.messageNeighbour(ackMsg, msg.getSenderUID());
    }
}
