package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    private static final String resoucefolder =  "D:\\Git\\AdventOfCode\\src\\main\\resources\\";
    public static List<Integer> readIntegerLineByLine(String fileName) {
        String path = resoucefolder + fileName;
        var res = new ArrayList<Integer>();
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String line = bf.readLine();
            while(line != null ){
                res.add(Integer.valueOf(line));
                line = bf.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;


    }

    public static List<List<String>> readCsvLineByLine(String fileName) {

        List<List<String>> res = new ArrayList<>();
        String path = resoucefolder + fileName;
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String line = bf.readLine();
            // comma sep
            while (line != null) {
                String[] inputs = line.split(",");
                List<String> cur = new ArrayList<>(Arrays.asList(inputs));

                res.add(cur);
                line = bf.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;

    }

    public static List<String> readStringLineByLine(String fileName) {
        List<String> res = new ArrayList<>();
        String path = resoucefolder + fileName;
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String line = bf.readLine();
            // comma sep
            while (line != null) {

                res.add(line);
                line = bf.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
