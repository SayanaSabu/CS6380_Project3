public class Main {
    public static void main(String[] args) {
        try {
            int currNodeUID = Integer.parseInt(args[0]);
            Node currNode = ReadConfig.read(currNodeUID);

            TCPServer server = new TCPServer(currNode);
            Runnable servRunnable = new Runnable() {
                public void run() {
                    server.startListening();
                }
            };
            Thread servThread = new Thread(servRunnable);
            servThread.start();

            for (Node neighbourNode : currNode.getNeighbourNodes()) {
                Runnable cliRunnable = new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        TCPClient client = new TCPClient(currNode, neighbourNode);
                        client.connect();

                        currNode.addNeighbourClient(client);
                    }
                };
                Thread cliThread = new Thread(cliRunnable);
                cliThread.start();
            }

            while (!currNode.areAllNeighboursOnline()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Connected to all neighbours");

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
