import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClient {
    private Node clientNode;
    private Node serverNode;

    public TCPClient(Node clientNode, Node serverNode) {
        this.clientNode = clientNode;
        this.serverNode = serverNode;
    }

    public void sendHandshake() {
        Message msg = new Message(this.clientNode.getUID(), Message.MessageType.HANDSHAKE);
        this.sendMessage(msg);
    }

    public void sendMessage(Message msg) {
        try {
            Socket clientSocket = new Socket(this.serverNode.getHostName(), this.serverNode.getPort());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            outputStream.writeObject(msg);
            outputStream.flush();

            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
