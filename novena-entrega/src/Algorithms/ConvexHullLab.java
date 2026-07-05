package Algorithms;

import java.util.*;

public class ConvexHullLab {

    private static final int NUM_POINTS = 1_000_000;
    private static final int COORD_RANGE = 2_000_000;
    private static final long SEED = 20260704L;
    private static final int WARMUP_RUNS = 1;
    private static final int TIMED_RUNS = 3;

    public static void main(String[] args) {
        System.out.println("Laboratorio de Convex Hull");
        System.out.println("Puntos generados: " + NUM_POINTS);
        System.out.println("Rango de coordenadas: [" + (-COORD_RANGE / 2) + ", " + (COORD_RANGE / 2) + "]");
        System.out.println();

        int[][] points = generatePointCloud(NUM_POINTS, COORD_RANGE, SEED);

        int[][] grahamResult = null;
        double grahamTimeMs = 0;
        for (int r = 0; r < WARMUP_RUNS + TIMED_RUNS; r++) {
            long t0 = System.nanoTime();
            int[][] res = GrahamScanConvexHull.buildHull(points);
            long t1 = System.nanoTime();
            if (r >= WARMUP_RUNS) {
                grahamTimeMs += (t1 - t0) / 1_000_000.0;
                grahamResult = res;
            }
        }
        grahamTimeMs /= TIMED_RUNS;

        List<Coordinate> coordPoints = toCoordinateList(points);
        List<Coordinate> monotoneResult = null;
        double monotoneTimeMs = 0;
        for (int r = 0; r < WARMUP_RUNS + TIMED_RUNS; r++) {
            long t0 = System.nanoTime();
            List<Coordinate> res = MonotoneChainConvexHull.computeHull(coordPoints);
            long t1 = System.nanoTime();
            if (r >= WARMUP_RUNS) {
                monotoneTimeMs += (t1 - t0) / 1_000_000.0;
                monotoneResult = res;
            }
        }
        monotoneTimeMs /= TIMED_RUNS;

        List<int[]> dncResult = null;
        double dncTimeMs = 0;
        for (int r = 0; r < WARMUP_RUNS + TIMED_RUNS; r++) {
            long t0 = System.nanoTime();
            List<int[]> res = DivideAndConquerConvexHull.computeHull(points);
            long t1 = System.nanoTime();
            if (r >= WARMUP_RUNS) {
                dncTimeMs += (t1 - t0) / 1_000_000.0;
                dncResult = res;
            }
        }
        dncTimeMs /= TIMED_RUNS;

        System.out.println("Resultados (promedio de " + TIMED_RUNS + " ejecuciones, tras " + WARMUP_RUNS + " de calentamiento):");
        System.out.printf("%-20s %12s %18s%n", "Algoritmo", "Puntos hull", "Tiempo promedio");
        System.out.printf("%-20s %12d %15.2f ms%n", "Graham Scan", grahamResult.length, grahamTimeMs);
        System.out.printf("%-20s %12d %15.2f ms%n", "Monotone Chain", monotoneResult.size(), monotoneTimeMs);
        System.out.printf("%-20s %12d %15.2f ms%n", "Divide and Conquer", dncResult.size(), dncTimeMs);
        System.out.println();

        Set<String> grahamKeys = keysFromIntArray(grahamResult);
        Set<String> monotoneKeys = keysFromCoordinates(monotoneResult);
        Set<String> dncKeys = keysFromIntArray(dncResult.toArray(new int[0][]));

        boolean grahamMatches = grahamKeys.equals(monotoneKeys);
        boolean dncMatches = dncKeys.equals(monotoneKeys);

        System.out.println("Verificacion (tomando Monotone Chain como referencia):");
        System.out.println("  Graham Scan produce el mismo conjunto de vertices: " + (grahamMatches ? "SI" : "NO"));
        System.out.println("  Divide and Conquer produce el mismo conjunto de vertices: " + (dncMatches ? "SI" : "NO"));

        if (!grahamMatches || !dncMatches) {
            System.out.println();
            System.out.println("ADVERTENCIA: se encontraron diferencias entre los algoritmos.");
            if (!grahamMatches) {
                printDiff("Graham Scan", grahamKeys, monotoneKeys);
            }
            if (!dncMatches) {
                printDiff("Divide and Conquer", dncKeys, monotoneKeys);
            }
        }

        System.out.println();
        System.out.println("Fin del laboratorio");
    }

    private static void printDiff(String label, Set<String> a, Set<String> reference) {
        Set<String> onlyInA = new HashSet<>(a);
        onlyInA.removeAll(reference);
        Set<String> onlyInRef = new HashSet<>(reference);
        onlyInRef.removeAll(a);
        System.out.println("  [" + label + "] puntos de mas: " + onlyInA);
        System.out.println("  [" + label + "] puntos faltantes: " + onlyInRef);
    }

    private static int[][] generatePointCloud(int n, int range, long seed) {
        Random rnd = new Random(seed);
        int[][] points = new int[n][2];
        for (int i = 0; i < n; i++) {
            points[i][0] = rnd.nextInt(range) - range / 2;
            points[i][1] = rnd.nextInt(range) - range / 2;
        }
        return points;
    }

    private static List<Coordinate> toCoordinateList(int[][] points) {
        List<Coordinate> list = new ArrayList<>(points.length);
        for (int[] p : points) {
            list.add(new Coordinate(p[0], p[1]));
        }
        return list;
    }

    private static Set<String> keysFromIntArray(int[][] pts) {
        Set<String> set = new HashSet<>();
        for (int[] p : pts) {
            set.add(p[0] + ":" + p[1]);
        }
        return set;
    }

    private static Set<String> keysFromCoordinates(List<Coordinate> pts) {
        Set<String> set = new HashSet<>();
        for (Coordinate c : pts) {
            set.add(c.getX() + ":" + c.getY());
        }
        return set;
    }
}

