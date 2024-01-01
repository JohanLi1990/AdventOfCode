package AOC2023;

import Utils.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D15LensLibrary {
    Box[] boxes = new Box[256];
    List<String> list = new ArrayList<>();
    public D15LensLibrary(List<String> input) {
        // single line
        String cur = input.get(0);
        for (String each : cur.split(",")) {
            if (each.isEmpty() || each.isBlank()) {
                continue;
            }
            list.add(each);
        }

        initAllBoxes(list);
    }

    private void initAllBoxes(List<String> input) {
        for (int i = 0; i < 256; i++) {
            boxes[i] = new Box();
        }

        for (String cur : input) {
            // either =, or -
            int indexOfMinus = cur.indexOf("-");
            if (indexOfMinus != -1) {
                String lenName = cur.substring(0, indexOfMinus);
                int boxNum = hashAlgo(lenName);
                boxes[boxNum].remove(lenName);
            } else {
                int indexOfEq = cur.indexOf("=");
                String lenName = cur.substring(0, indexOfEq);
                int val = Integer.parseInt(cur.substring(indexOfEq + 1));
                int boxNum = hashAlgo(lenName);
                boxes[boxNum].add(new Node(val, lenName));
            }
        }
    }

    public long getSumBoxes() {
        long res =0;
        for (int i = 0; i < boxes.length; i++) {
            res += boxes[i].getBoxPower(i);
        }
        return res;
    }

    public long getSumHash(List<String> list) {
        long res = 0;
        for (String cur : list) {
            res += hashAlgo(cur);
        }
        return res;
    }

    private int hashAlgo(String cur) {

        int res = 0;
        for (char c : cur.toCharArray()) {
            res += (int)c;
            res *= 17;
            res %= 256;
        }
        return res;
    }





    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D15");

        long start = System.currentTimeMillis();
        D15LensLibrary ans = new D15LensLibrary(input);

        long res = ans.getSumBoxes();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class Box {

    // remove will cause all the others to shift
    Map<String, Node> map = new HashMap<>();

    Node head, tail;

    public Box() {
        head = new Node(-1, "head");
        tail = new Node(-1, "tail");
        head.next = tail;
        tail.pre = head;
    }

    public void add(Node newNode) {
        if (map.containsKey(newNode.name)) {
            map.get(newNode.name).focallength = newNode.focallength;
            return;
        }
        Node lastOne = tail.pre;
        lastOne.next = newNode;

        newNode.pre = lastOne;
        newNode.next = tail;

        tail.pre = newNode;
        map.put(newNode.name, newNode);
    }

    public void remove(String name) {
        if (!map.containsKey(name)) return;

        Node cur = map.get(name);
        Node pre = cur.pre;
        Node next = cur.next;

        pre.next = next;
        next.pre = pre;
        map.remove(name);
    }

    public int getBoxPower(int boxLabel) {
        Node cur = head.next;
        int res = 0;
        int pos = 1;
        while(cur != tail) {
           res += (boxLabel + 1) * (pos++) * cur.focallength;
           cur = cur.next;
        }
        return res;
    }


}

class Node {
    Node pre, next;
    int focallength;
    String name;

    Node(int val, String name) {
        pre = null;
        next = null;
        focallength = val;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Node[" + name + "]=" + focallength;
    }
}

