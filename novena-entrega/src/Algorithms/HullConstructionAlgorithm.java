package Algorithms;

import java.util.*;

public class HullConstructionAlgorithm {

    public static List<int[]> computeHullPoints(int[][] pointSet) {
        if (pointSet.length < 3) {
            return Arrays.asList(pointSet);
        }

        Arrays.sort(pointSet, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        List<int[]> topChain = new ArrayList<>();
        List<int[]> bottomChain = new ArrayList<>();

        for (int[] current : pointSet) {
            while (topChain.size() >= 2
                    && isClockwiseOrCollinear(
                            topChain.get(topChain.size() - 2),
                            topChain.get(topChain.size() - 1),
                            current)) {
                topChain.remove(topChain.size() - 1);
            }
            topChain.add(current);
        }

        for (int i = pointSet.length - 1; i >= 0; i--) {
            int[] current = pointSet[i];
            while (bottomChain.size() >= 2
                    && isClockwiseOrCollinear(
                            bottomChain.get(bottomChain.size() - 2),
                            bottomChain.get(bottomChain.size() - 1),
                            current)) {
                bottomChain.remove(bottomChain.size() - 1);
            }
            bottomChain.add(current);
        }

        Set<String> seenKeys = new HashSet<>();
        List<int[]> combinedHull = new ArrayList<>();

        for (int[] point : topChain) {
            String key = point[0] + ":" + point[1];
            if (seenKeys.add(key)) {
                combinedHull.add(point);
            }
        }
        for (int[] point : bottomChain) {
            String key = point[0] + ":" + point[1];
            if (seenKeys.add(key)) {
                combinedHull.add(point);
            }
        }

        return combinedHull;
    }

    private static boolean isClockwiseOrCollinear(int[] first, int[] second, int[] third) {
        long cross = (long) (second[0] - first[0]) * (third[1] - first[1])
                - (long) (second[1] - first[1]) * (third[0] - first[0]);
        return cross <= 0;
    }

    public static void main(String[] args) {
        int[][] samplePoints = {
            { 0, 0 }, { 1, -4 }, { -1, -5 },
            { -5, -3 }, { -3, -1 }, { -1, -3 },
            { -2, -2 }, { -1, -1 }, { -2, -1 },
            { -1, 1 }
        };

        List<int[]> hullPoints = computeHullPoints(samplePoints);

        System.out.println("Hull points:");
        for (int[] point : hullPoints) {
            System.out.println(point[0] + " " + point[1]);
        }
    }
}

