package AOC2019;

public class Aoc2019D4 {

    public boolean isValidPassword(int cur) {
        int len = String.valueOf(cur).length();
        if (len != 6) return false;

        int preD = 10;
        int streak = 1;
        boolean sepDouble = false;
        for (int i = 0; i < 6; i++) {
            int curDigit = cur % 10;
            if (curDigit > preD) return false;
            if (curDigit == preD) {
                streak++;
            } else {
                if (streak == 2) sepDouble = true;
                streak = 1;
            }
            preD = curDigit;
            cur /= 10;
        }
        return sepDouble || streak == 2;
    }

    public int numPass(int s, int e) {
        int res = 0;
        for (int i = s; i <= e; i++) {
            if (isValidPassword(i)){
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Aoc2019D4 aoc = new Aoc2019D4();
//        System.out.println(aoc.isValidPassword(123444));
//        System.out.println(aoc.isValidPassword(112233));
//        System.out.println(aoc.isValidPassword(113399));
        System.out.println(aoc.isValidPassword(556789));
        System.out.println(aoc.numPass(254032, 789860));
    }
}
