import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private Node serverNode;

    public TCPServer(Node serverNode) {
        this.serverNode = serverNode;
    }

    // public Node getServerNode() {
    // return this.serverNode;
    // }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.serverNode.getPort());

            System.out.println("Server online with UID: " + this.serverNode.getUID());

            while (!Thread.currentThread().isInterrupted()) {
                Socket connectionSocket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(connectionSocket.getInputStream());

                try {
                    Message message = (Message) ois.readObject();

                    if (message.getType() != Message.MessageType.HANDSHAKE) {
                        this.serverNode.addReceivedMessage(message);
                    } else {
                        System.out.println("Received HANDSHAKE from " + message.getSenderUID());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                ois.close();
                connectionSocket.close();
            }

            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
