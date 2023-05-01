import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadConfig {
    public static Node read(String currHostName) throws Exception {
        FileReader fr = new FileReader(
                "./config.txt");
        BufferedReader br = new BufferedReader(fr);

        HashMap<Integer, Node> nodeMap = new HashMap<>();

        try {
            String thisLine = br.readLine();
            thisLine = br.readLine();
            int numberOfNodes = Integer.parseInt(thisLine);

            thisLine = br.readLine();
            thisLine = br.readLine();

            for (int i = 0; i < numberOfNodes; i++) {
                thisLine = br.readLine();
                String[] nodeInfo = thisLine.split(" ");

                int uid = Integer.parseInt(nodeInfo[0]);
                Node newNode = new Node(uid, nodeInfo[1], Integer.parseInt(nodeInfo[2]));

                nodeMap.put(uid, newNode);
            }

            thisLine = br.readLine();
            thisLine = br.readLine();

            thisLine = br.readLine();
            int numberOfEdges = Integer.parseInt(thisLine);

            thisLine = br.readLine();
            thisLine = br.readLine();

            Pattern edgePattern = Pattern.compile("\\((\\d+),(\\d+)\\)\\s(\\d+)");

            for (int i = 0; i < numberOfEdges; i++) {
                thisLine = br.readLine();
                Matcher matcher = edgePattern.matcher(thisLine);

                if (matcher.find()) {
                    int uid1 = Integer.parseInt(matcher.group(1));
                    int uid2 = Integer.parseInt(matcher.group(2));

                    nodeMap.get(uid1).addNeighbour(nodeMap.get(uid2));
                    nodeMap.get(uid2).addNeighbour(nodeMap.get(uid1));
                }
            }

            thisLine = br.readLine();
            thisLine = br.readLine();

            thisLine = br.readLine();
            int leaderUID = Integer.parseInt(thisLine);

            for (Map.Entry<Integer, Node> e : nodeMap.entrySet()) {
                e.getValue().setLeaderUID(leaderUID);
            }
        } finally {
            br.close();
        }

        Node currNode = new Node();
        for (Map.Entry<Integer, Node> e : nodeMap.entrySet()) {
            if (currHostName.equals(e.getValue().getHostName())) {
                currNode = e.getValue();
            }
        }

        return currNode;
    }
}
