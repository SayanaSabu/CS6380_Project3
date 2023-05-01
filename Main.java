public class Main {
    public static void main(String[] args) {
        try {
            int currNodeUID = Integer.parseInt(args[0]);
            Node currNode = ReadConfig.read(currNodeUID);

            System.out.println("UID: " + currNode.getUID());
            System.out.println("Hostname: " + currNode.getHostName());
            System.out.println("Port: " + currNode.getPort());

            for (Node n : currNode.getNeighbourNodes()) {
                System.out.println("Neighbour: " + n.getUID());
            }

            // TCPServer server = new TCPServer(currNode);
            // Runnable servRunnable = new Runnable() {
            // public void run() {
            // server.startListening();
            // }
            // };
            // Thread servThread = new Thread(servRunnable);
            // servThread.start();

            // for (Node neighbourNode : currNode.getAllNodes()) {
            // if (currNode.isNodeNeighbour(neighbourNode.getUID())) {
            // Runnable cliRunnable = new Runnable() {
            // public void run() {
            // try {
            // Thread.sleep(5000);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }

            // TCPClient client = new TCPClient(currNode, neighbourNode);
            // client.connect();

            // currNode.addNeighbourClient(client);
            // }
            // };
            // Thread cliThread = new Thread(cliRunnable);
            // cliThread.start();
            // }
            // }

            // while (!currNode.areAllNeighboursOnline()) {
            // try {
            // Thread.sleep(10000);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }

            // System.out.println("Waiting for all neighbours to come online");
            // }

            // new PelegsLeaderElection(server).startElection();

            // try {
            // Thread.sleep(10000);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }

            // new BFSTree(currNode).buildTree();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
