package Algorithms;

import java.util.*;

public class DivideAndConquerConvexHull {

    private static long cross(int[] o, int[] a, int[] b) {
        return (long) (a[0] - o[0]) * (b[1] - o[1]) - (long) (a[1] - o[1]) * (b[0] - o[0]);
    }

    public static List<int[]> computeHull(int[][] pointSet) {
        if (pointSet.length == 0) {
            return new ArrayList<>();
        }

        Set<Long> seen = new HashSet<>();
        List<int[]> unique = new ArrayList<>();
        for (int[] p : pointSet) {
            long key = (((long) p[0]) << 32) ^ (p[1] & 0xffffffffL);
            if (seen.add(key)) {
                unique.add(p);
            }
        }

        int[][] pts = unique.toArray(new int[0][]);
        Arrays.sort(pts, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

        return dropCollinear(solve(pts, 0, pts.length));
    }

    private static List<int[]> dropCollinear(List<int[]> hull) {
        if (hull.size() < 3) {
            return hull;
        }
        List<int[]> current = hull;
        boolean changed = true;
        while (changed && current.size() >= 3) {
            changed = false;
            List<int[]> next = new ArrayList<>();
            int n = current.size();
            for (int i = 0; i < n; i++) {
                int[] prev = current.get((i - 1 + n) % n);
                int[] cur = current.get(i);
                int[] nxt = current.get((i + 1) % n);
                if (cross(prev, cur, nxt) == 0) {
                    changed = true;
                } else {
                    next.add(cur);
                }
            }
            current = next;
        }
        return current;
    }

    private static List<int[]> solve(int[][] pts, int lo, int hi) {
        int n = hi - lo;
        if (n <= 3) {
            return baseHull(pts, lo, hi);
        }
        int mid = lo + n / 2;
        List<int[]> left = solve(pts, lo, mid);
        List<int[]> right = solve(pts, mid, hi);
        return merge(left, right);
    }

    private static List<int[]> baseHull(int[][] pts, int lo, int hi) {
        List<int[]> chunk = new ArrayList<>();
        for (int i = lo; i < hi; i++) {
            chunk.add(pts[i]);
        }
        if (chunk.size() <= 2) {
            return chunk;
        }
        int[] a = chunk.get(0), b = chunk.get(1), c = chunk.get(2);
        long cr = cross(a, b, c);
        List<int[]> res = new ArrayList<>();
        if (cr == 0) {

            res.add(a);
            res.add(c);
        } else if (cr > 0) {
            res.add(a);
            res.add(b);
            res.add(c);
        } else {
            res.add(a);
            res.add(c);
            res.add(b);
        }
        return res;
    }

    private static List<int[]> bruteMerge(List<int[]> left, List<int[]> right) {
        List<int[]> all = new ArrayList<>(left);
        all.addAll(right);
        return baseHullAny(all);
    }

    private static List<int[]> baseHullAny(List<int[]> pts) {
        int n = pts.size();
        if (n <= 2) {
            return pts;
        }

        boolean allCollinear = true;
        for (int c = 2; c < n; c++) {
            if (cross(pts.get(0), pts.get(1), pts.get(c)) != 0) {
                allCollinear = false;
                break;
            }
        }
        if (allCollinear) {
            int[] a = pts.get(0), b = pts.get(1);
            int[] lo = a, hi = a;
            for (int[] p : pts) {
                if (p[0] < lo[0] || (p[0] == lo[0] && p[1] < lo[1])) lo = p;
                if (p[0] > hi[0] || (p[0] == hi[0] && p[1] > hi[1])) hi = p;
            }
            List<int[]> res = new ArrayList<>();
            res.add(lo);
            if (hi[0] != lo[0] || hi[1] != lo[1]) res.add(hi);
            return res;
        }

        boolean[] onHull = new boolean[n];
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < n; b++) {
                if (a == b) continue;
                boolean allNonNeg = true, allNonPos = true;
                for (int c = 0; c < n; c++) {
                    if (c == a || c == b) continue;
                    long cr = cross(pts.get(a), pts.get(b), pts.get(c));
                    if (cr < 0) allNonNeg = false;
                    if (cr > 0) allNonPos = false;
                }
                if (allNonNeg || allNonPos) {
                    onHull[a] = true;
                    onHull[b] = true;
                }
            }
        }
        List<int[]> candidates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (onHull[i]) candidates.add(pts.get(i));
        }
        if (candidates.size() <= 2) {
            return candidates;
        }

        double cx = 0, cy = 0;
        for (int[] p : candidates) { cx += p[0]; cy += p[1]; }
        cx /= candidates.size();
        cy /= candidates.size();
        final double fcx = cx, fcy = cy;
        candidates.sort((p, q) -> Double.compare(Math.atan2(p[1] - fcy, p[0] - fcx), Math.atan2(q[1] - fcy, q[0] - fcx)));
        return dropCollinear(candidates);
    }

    private static int nextIdx(int i, int n) {
        return (i + 1) % n;
    }

    private static int prevIdx(int i, int n) {
        return (i - 1 + n) % n;
    }

    private static int rightmostIndex(List<int[]> hull) {
        int best = 0;
        for (int i = 1; i < hull.size(); i++) {
            int[] p = hull.get(i), q = hull.get(best);
            if (p[0] > q[0] || (p[0] == q[0] && p[1] > q[1])) {
                best = i;
            }
        }
        return best;
    }

    private static int leftmostIndex(List<int[]> hull) {
        int best = 0;
        for (int i = 1; i < hull.size(); i++) {
            int[] p = hull.get(i), q = hull.get(best);
            if (p[0] < q[0] || (p[0] == q[0] && p[1] < q[1])) {
                best = i;
            }
        }
        return best;
    }

    private static final int BRUTE_MERGE_THRESHOLD = 8;

    private static List<int[]> merge(List<int[]> left, List<int[]> right) {
        int nL = left.size(), nR = right.size();

        if (nL <= BRUTE_MERGE_THRESHOLD && nR <= BRUTE_MERGE_THRESHOLD) {
            return bruteMerge(left, right);
        }

        int ui = rightmostIndex(left);
        int uj = leftmostIndex(right);
        boolean moved = true;
        while (moved) {
            moved = false;
            while (cross(left.get(ui), right.get(uj), left.get(nextIdx(ui, nL))) > 0) {
                ui = nextIdx(ui, nL);
                moved = true;
            }
            while (cross(right.get(uj), left.get(ui), right.get(prevIdx(uj, nR))) < 0) {
                uj = prevIdx(uj, nR);
                moved = true;
            }
        }

        int li = rightmostIndex(left);
        int lj = leftmostIndex(right);
        moved = true;
        while (moved) {
            moved = false;
            while (cross(left.get(li), right.get(lj), left.get(prevIdx(li, nL))) < 0) {
                li = prevIdx(li, nL);
                moved = true;
            }
            while (cross(right.get(lj), left.get(li), right.get(nextIdx(lj, nR))) > 0) {
                lj = nextIdx(lj, nR);
                moved = true;
            }
        }

        List<int[]> result = new ArrayList<>();

        int idx = ui;
        result.add(left.get(idx));
        while (idx != li) {
            idx = nextIdx(idx, nL);
            result.add(left.get(idx));
        }

        idx = lj;
        result.add(right.get(idx));
        while (idx != uj) {
            idx = nextIdx(idx, nR);
            result.add(right.get(idx));
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] samplePoints = {
            { 0, 0 }, { 1, -4 }, { -1, -5 },
            { -5, -3 }, { -3, -1 }, { -1, -3 },
            { -2, -2 }, { -1, -1 }, { -2, -1 },
            { -1, 1 }
        };

        List<int[]> hullPoints = computeHull(samplePoints);

        System.out.println("Hull points:");
        for (int[] point : hullPoints) {
            System.out.println(point[0] + " " + point[1]);
        }
    }
}

