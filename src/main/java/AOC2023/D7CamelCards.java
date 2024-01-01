package AOC2023;

import Utils.FileUtil;

import java.util.*;

public class D7CamelCards {

    Map<String, Long> map = new HashMap<>();
    PriorityQueue<Hand> hands = new PriorityQueue<>(Hand::compareTo);
    public D7CamelCards(List<String> input) {
        // hand, amount,
        for (String line : input) {
            String[] curPars = line.split(" ");
            String curHand = curPars[0].trim();
            hands.offer(new Hand(curHand));
            map.put(curHand, Long.parseLong(curPars[1].trim()));
        }
    }

    public long totalWinning(){
        long res = 0;
        long rank = 1L;
        while(!hands.isEmpty()) {
            Hand cur = hands.poll();
            res += map.getOrDefault(cur.origin, 0L) * (rank++);
        }
        return res;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D7");

        long start = System.currentTimeMillis();
        D7CamelCards ans = new D7CamelCards(input);
        long res = ans.totalWinning();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}

class Hand {
    String origin;
    char[] handsChar;

    public final static Map<Character, Integer> strength = new HashMap<>();

    static {
        //A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2
        strength.put('J', 0);
        strength.put('2', 1);
        strength.put('3', 2);
        strength.put('4', 3);
        strength.put('5', 4);
        strength.put('6', 5);
        strength.put('7', 6);
        strength.put('8', 7);
        strength.put('9', 8);
        strength.put('T', 9);
        strength.put('Q', 11);
        strength.put('K', 12);
        strength.put('A', 13);
    }
    HandType type;

    public Hand(String cur) {
        origin = cur;
        handsChar = cur.toCharArray();
        // we can do it because only 5
//        type = getType(handsChar);
        type = getTypeII(handsChar);
    }

    public int compareTo(Hand b) {
        if (type != b.type) {
            return type.getVal() - b.type.getVal();
        }
        for (int i = 0; i < 5; i++) {
            if (handsChar[i] == b.handsChar[i]) continue;
            return strength.get(handsChar[i]) - strength.get(b.handsChar[i]);
        }
        return 0;
    }

    private static HandType getType(char[] arr) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
        }
        return getHandTypeFromMap(map);
    }

    private static HandType getHandTypeFromMap(Map<Character, Integer> map) {
        if (map.size() == 5) {
            return HandType.HIGHCARD;
        } else if (map.size() == 1) {
            return HandType.FIVEOFKIND;
        } else if (map.size() == 4) {
            return HandType.ONEPAIR;
        } else if (map.size() == 3) {
            for (Map.Entry<Character, Integer> ent : map.entrySet()) {
                if (ent.getValue() == 3 ) return HandType.THREEOFKIND;
                if (ent.getValue() == 2) return HandType.TWOPAIR;
            }
        }

        for (Map.Entry<Character, Integer> ent : map.entrySet()) {
            if (ent.getValue() == 4 || ent.getValue() == 1) return HandType.FOUROFKIND;
            if (ent.getValue() == 3 || ent.getValue() == 2) return HandType.FULLHOUSE;
        }

        System.out.println("Cannot find the correct TYpe");
        return null;
    }

    private static HandType getTypeII(char[] arr) {
        Map<Character, Integer> map = new HashMap<>();

        int  numJ = 0;
        char maxCard = '*';
        int maxCardFreq = 0;
        for (int i = 0; i < 5; i++) {
            if (arr[i] == 'J') {
                numJ++;
                continue;
            }
            map.put(arr[i], map.getOrDefault(arr[i], 0) + 1);
            if(map.get(arr[i]) > maxCardFreq) {
                maxCard = arr[i];
                maxCardFreq = map.get(arr[i]);
            }
        }

        if (maxCard == '*') {
            // all J
            return HandType.FIVEOFKIND;
        }
        map.put(maxCard, map.get(maxCard) + numJ);
        return getHandTypeFromMap(map);

    }


    enum HandType {
        FIVEOFKIND(6), FOUROFKIND(5), FULLHOUSE(4), THREEOFKIND(3),
        TWOPAIR(2), ONEPAIR(1), HIGHCARD(0);

        private final int val;
        HandType(int val) {
            this.val = val;
        }

        public int getVal(){return val;}

    }


}

