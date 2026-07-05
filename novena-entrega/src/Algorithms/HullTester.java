package Algorithms;

import java.util.*;

public class HullTester {

    private static long cross(int[] o, int[] a, int[] b) {
        return (long) (a[0] - o[0]) * (b[1] - o[1]) - (long) (a[1] - o[1]) * (b[0] - o[0]);
    }

    private static List<int[]> reference(int[][] points) {
        Set<Long> seen = new HashSet<>();
        List<int[]> uniq = new ArrayList<>();
        for (int[] p : points) {
            long key = (((long) p[0]) << 32) ^ (p[1] & 0xffffffffL);
            if (seen.add(key)) uniq.add(p);
        }
        int[][] pts = uniq.toArray(new int[0][]);
        Arrays.sort(pts, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));
        int n = pts.length;
        if (n < 3) {
            return new ArrayList<>(Arrays.asList(pts));
        }
        int[][] hull = new int[2 * n][];
        int k = 0;
        for (int i = 0; i < n; i++) {
            while (k >= 2 && cross(hull[k - 2], hull[k - 1], pts[i]) <= 0) k--;
            hull[k++] = pts[i];
        }
        int lower = k + 1;
        for (int i = n - 2; i >= 0; i--) {
            while (k >= lower && cross(hull[k - 2], hull[k - 1], pts[i]) <= 0) k--;
            hull[k++] = pts[i];
        }
        List<int[]> result = new ArrayList<>();
        for (int i = 0; i < k - 1; i++) result.add(hull[i]);
        return result;
    }

    private static Set<String> asKeySet(List<int[]> pts) {
        Set<String> s = new HashSet<>();
        for (int[] p : pts) s.add(p[0] + ":" + p[1]);
        return s;
    }

    public static void main(String[] args) {
        Random rnd = new Random(42);
        int trials = 3000;
        int mismatches = 0;
        for (int t = 0; t < trials; t++) {
            int n = 50 + rnd.nextInt(2000);
            int range = (t % 2 == 0) ? 2_000_000 : 60;
            int[][] pts = new int[n][2];
            for (int i = 0; i < n; i++) {
                pts[i][0] = rnd.nextInt(range) - range / 2;
                pts[i][1] = rnd.nextInt(range) - range / 2;
            }
            List<int[]> ref = reference(pts);
            List<int[]> dnc = DivideAndConquerConvexHull.computeHull(pts);

            Set<String> refSet = asKeySet(ref);
            Set<String> dncSet = asKeySet(dnc);

            if (!refSet.equals(dncSet)) {
                mismatches++;
                if (mismatches <= 3) {
                    System.out.println("MISMATCH on trial " + t);
                    System.out.print("points = {");
                    for (int[] p : pts) System.out.print("{" + p[0] + "," + p[1] + "},");
                    System.out.println("}");
                    System.out.println("reference: " + refSet);
                    System.out.println("dnc:       " + dncSet);
                    System.out.println();
                }
            }
        }
        System.out.println("Trials: " + trials + "  Mismatches: " + mismatches);
    }
}

