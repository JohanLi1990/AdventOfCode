package AOC2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeekExample {



    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 10, 4, 7, 6);

        var res = list.stream()
                .peek(System.out::println)
                .filter(num -> num > 5)
                .findAny().get();

    }
}
