public class Main {
    public static void main(String[] args) {
        try {
            int currNodeUID = Integer.parseInt(args[0]);
            Node currNode = ReadConfig.read(currNodeUID);

            TCPServer server = new TCPServer(currNode);
            server.start();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Node neighbourNode : currNode.getNeighbours()) {
                new TCPClient(currNode, neighbourNode).sendHandshake();
            }

            while (true) {
                long handshakeMessagesCount = currNode
                        .getReceivedMessages()
                        .stream()
                        .filter(msg -> msg.getType() == Message.MessageType.HANDSHAKE)
                        .count();
                int neighboursCount = currNode.getNeighbours().size();

                boolean didAllNeighboursReply = neighboursCount == (int) handshakeMessagesCount;

                if (!didAllNeighboursReply) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    // System.out.println("Handshake received from all neighbours");
                    break;
                }
            }

            new LayeredBFS(currNode).buildTree();

            /*
             * for (TCPClient client : currNode.getNeighbourClients()) {
             * client.closeConnection();
             * }
             * server.interrupt();
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
