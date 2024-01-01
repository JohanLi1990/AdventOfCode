package AOC2023;

import Utils.FileUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D4ScratchCards {

    public int sumPoints(List<String> input) {
        int res = 0;

        for (String line : input) {
            String allCards = line.split(":")[1];
            String[] cardGroup = allCards.split("\\|");
            String[] winning = cardGroup[0].trim().split(" ");
            String[] hands = cardGroup[1].trim().split(" ");

            int points = 0;
            Set<String> winSet = populate(winning);
            Set<String> handSet = populate(hands);

            for (String h : handSet) {
                if (winSet.contains(h)) {
                    if (points == 0) {
                        points = 1;
                    } else {
                        points <<= 1;
                    }
                }
            }
            res += points;
        }
        return res;

    }

    public long getTotalCards(List<String> input) {
        long res = 0;
        // for each card, calculate winning points.
        int[] cards = new int[input.size()];
        int i = 0;
        for (String line : input) {
            String allCards = line.split(":")[1];
            String[] cardGroup = allCards.split("\\|");
            String[] winning = cardGroup[0].trim().split(" ");
            String[] hands = cardGroup[1].trim().split(" ");

            int points = 0;
            Set<String> winSet = populate(winning);
            Set<String> handSet = populate(hands);

            for (String h : handSet) {
                if (winSet.contains(h)) {
                    points++;
                }
            }
            cards[i++] = points;
        }

        int[] cardsNum = new int[input.size()];
        Arrays.fill(cardsNum, 1);
        for (i = 0; i < cards.length; i++) {
            if (cardsNum[i] > 0) {
                res += cardsNum[i];
            }
            if (cards[i] > 0) {
                for (int j = i + 1; j < Math.min(cards.length, i + cards[i] + 1); j++) {
                    cardsNum[j] += cardsNum[i];
                }
            }
        }
        return res;

    }

    private static Set<String> populate(String[] arr) {
        Set<String> set = new HashSet<>();
        for (String a : arr) {
            if (a.isBlank() || a.isEmpty()) continue;
            set.add(a.trim());
        }
        return set;
    }

    public static void main(String[] args) {
        List<String> input = FileUtil.readStringLineByLine("2023Resource/2023D4");

        long start = System.currentTimeMillis();
        D4ScratchCards ans = new D4ScratchCards();
//        int res = ans.getSum(input);
        long res = ans.getTotalCards(input);
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(res);
    }
}
