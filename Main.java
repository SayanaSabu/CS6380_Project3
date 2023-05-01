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

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
