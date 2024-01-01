package AOC2019;

import Utils.FileUtil;

import java.util.*;

public class Aoc2019D6 {


    public int findOrbits(List<String> input) {

        Map<String, Node> Nodes = new HashMap<>();
        createGraph(input, Nodes);
        Queue<Node> q = new ArrayDeque<>(); // use BFS level by level
        for (Map.Entry<String, Node> cur : Nodes.entrySet()) {
            Node curNode = cur.getValue();
            if (curNode.parent == 0) q.offer(curNode);
        }

        // one can only have one parents
        int level = 0;
        int num = 0;
        while(!q.isEmpty()) {

            int size = q.size();
            for (int i = 0; i < size; i++) {
                Node cur = q.poll();
                num += level;
                for (Node next: cur.next) {
                    q.offer(next);
                }
            }
            level++;
        }

        return num;

    }

    private void createGraph(List<String> input, Map<String, Node> nodes) {

        for (String edge : input) {
            String nodeA = edge.split("\\)")[0];
            String nodeB = edge.split("\\)")[1];

//            nodes.computeIfAbsent(nodeA, k -> new AOC2019.Node(nodeA));
            Node B = nodes.computeIfAbsent(nodeB, k -> new Node(nodeB));
            Node A = nodes.computeIfAbsent(nodeA, k -> new Node(nodeA));
//            A.child++;
            A.next.add(B);
            B.parent++;
        }
    }

    public static void main(String[] args) {

        List<String> input = FileUtil.readStringLineByLine("2019D6_1");
        System.out.println(input.get(4));

    }

}

class Node {
    String name;
    List<Node> next;
//    int child;
    int parent;

    public Node(String name) {
        parent  = 0;
        this.next = new ArrayList<>();
        this.name = name;
    }
}
