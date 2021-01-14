package useless;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * 统计搜索效率测试结果 取平均数
 *
 */
public class test {
    enum Type {
        bfEasy(0),
        bfsEasy(1),
        bfNorm(2),
        bfsNorm(3),
        bfHard(4),
        bfsHard(5);

        Type(int id) {
            this.id = id;
        }

        public int id;

        public static String stringOf(int id) {
            for (Type value : Type.values()) {
                if (value.id == id) return value.toString();
            }
            throw new NoSuchElementException();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String path = "src/useless/test.txt";
        Scanner scan = new Scanner(new BufferedInputStream(new FileInputStream(path)));
        double[][] data = new double[6][1000];
        int index = 0;
        int j = 0;
        while (scan.hasNextLine()) {
            String s = scan.nextLine();
            if (s.isEmpty()) continue;
            try {
                data[index][j] = Double.parseDouble(s);
                j++;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                index = Type.valueOf(s).id;
                j = 0;
            }
        }

        scan.close();

        double[] avg = new double[6];

        for (int i = 0; i < data.length; i++) {
            double sum = 0;
            int cnt = 0;
            for (int z = 0; z < data[i].length; z++) {
                if(data[i][z] == 0) break;
                sum += data[i][z];
                cnt++;
            }
            avg[i] = sum / cnt;
        }

        for (int i = 0; i < avg.length; i++) {
            System.out.printf(Type.stringOf(i) + " avg: %.2f\n", avg[i]);
        }
    }
}
