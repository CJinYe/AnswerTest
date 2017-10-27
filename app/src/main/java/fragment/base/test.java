package fragment.base;

import java.util.ArrayList;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-8-8 16:28
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class test {
    private static int SUM = 100;
    private static int[] child = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static ArrayList add(int di, String si, ArrayList br) {
        for (int i = 0; i < br.size(); i++) {
            br.set(i, di + si + br.get(i));
        }
        return br;
    }

    static ArrayList<String> calculate(int sum, int number, int index) {
        int di = Math.abs(number % 10);
        //        System.out.println("num="+number);
        if (index >= child.length) {
            if (sum == number) {
                ArrayList result = new ArrayList();
                result.add(Integer.toString(di));
                return result;
            } else {
                return new ArrayList();
            }
        }
        ArrayList br1 = calculate(sum - number, child[index], index + 1);
        ArrayList br2 = calculate(sum - number, -child[index], index + 1);
        int Number = number >= 0 ? 10 * number + child[index] : 10 * number - child[index];
        ArrayList br3 = calculate(sum, Number, index + 1);
        ArrayList results = new ArrayList();
        results.addAll(add(di, "+", br1));
        results.addAll(add(di, "-", br2));
        results.addAll(add(di, "", br3));
        return results;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (String string : calculate(SUM, child[0], 1)) {
            System.out.println(string);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }
}
