import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClient {
    private Node clientNode;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private Node serverNode;

    // public TCPClient() {
    // }

    public TCPClient(Node clientNode, Node serverNode) {
        this.clientNode = clientNode;
        this.serverNode = serverNode;
    }

    public void connect() {
        try {
            this.clientSocket = new Socket(this.serverNode.getHostName(), this.serverNode.getPort());
            this.outputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());

            new Message(this.clientNode.getUID(), Message.MessageType.HANDSHAKE).send(this.outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            this.outputStream.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public ObjectOutputStream getOutputStream() {
    // return this.outputStream;
    // }

    // public Node getServerNode() {
    // return this.serverNode;
    // }
}
