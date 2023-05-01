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

            for (Node neighbourNode : currNode.getNeighbourNodes()) {
                TCPClient client = new TCPClient(currNode, neighbourNode);
                client.connect();

                currNode.addNeighbourClient(client);
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (TCPClient client : currNode.getNeighbourClients()) {
                client.closeConnection();
            }
            server.stopListening();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
