package AOC2019;

import java.util.Arrays;

public class Advent2019D2 {

    int calculate1202(int[] A) {

        int n = A.length;

        for (int i = 0; i < n; i += 4) {
            int op = A[i];
            int res = 0;
            if (op == 1) {
                res = A[A[i + 1]] + A[A[i + 2]];
            } else if (op == 2) {
                res = A[A[i + 1]] * A[A[i + 2]];
            } else if (op == 99) {
                break;
            }
            A[A[i + 3]] = res;
        }
//        System.out.println(Arrays.toString(A));
        return A[0];
    }

    private int[] calculateII(int[] A) {
        for (int a = 0; a <= 99; a++) {
            for (int b = 0; b <= 99; b++) {
                int[] B = Arrays.copyOf(A, A.length);
                B[1] = a; B[2] = b;
                int res = calculate1202(B);
                if (res == 19690720) return new int[]{a, b};
            }
        }

        return new int[]{-1, -1};



    }

    public static void main(String[] args) {
        Advent2019D2 a2 = new Advent2019D2();

        int[] A = new int[]{
                1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,9,1,19,1,19,5,23,1,23,6,27,2,9,27,31,1,5,31,35,1,35,10,39,1,39,10,43,2,43,9,47,1,6,47,51,2,51,6,55,1,5,55,59,2,59,10,63,1,9,63,67,1,9,67,71,2,71,6,75,1,5,75,79,1,5,79,83,1,9,83,87,2,87,10,91,2,10,91,95,1,95,9,99,2,99,9,103,2,10,103,107,2,9,107,111,1,111,5,115,1,115,2,119,1,119,6,0,99,2,0,14,0
        };
//        A[1] = 12;
//        A[2] = 2;
//        int res = a2.calculate1202(A);
        int[] res = a2.calculateII(A);
        System.out.println(Arrays.toString(res));
    }
}


