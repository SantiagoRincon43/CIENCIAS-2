package Algorithms;

import java.util.*;

public class GrahamScanConvexHull {

    private static class Vertex {
        private double px, py;

        private Vertex(double px, double py) {
            this.px = px;
            this.py = py;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Vertex v = (Vertex) obj;
            return Double.compare(v.px, px) == 0 && Double.compare(v.py, py) == 0;
        }
    }

    private static int turnDirection(Vertex a, Vertex b, Vertex c) {
        double value = a.px * (b.py - c.py)
                + b.px * (c.py - a.py)
                + c.px * (a.py - b.py);

        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }

    private static double squaredDistance(Vertex a, Vertex b) {
        double deltaX = a.px - b.px;
        double deltaY = a.py - b.py;
        return deltaX * deltaX + deltaY * deltaY;
    }

    public static int[][] buildHull(int[][] inputPoints) {
        int total = inputPoints.length;

        if (total < 3) {
            return new int[][]{{-1}};
        }

        List<Vertex> vertices = new ArrayList<>();
        for (int[] coord : inputPoints) {
            vertices.add(new Vertex(coord[0], coord[1]));
        }

        Vertex pivot = Collections.min(vertices, (v1, v2) -> {
            if (v1.py != v2.py) {
                return Double.compare(v1.py, v2.py);
            }
            return Double.compare(v1.px, v2.px);
        });

        vertices.sort((v1, v2) -> {
            int direction = turnDirection(pivot, v1, v2);
            if (direction == 0) {
                return Double.compare(squaredDistance(pivot, v1), squaredDistance(pivot, v2));
            }
            return (direction < 0) ? -1 : 1;
        });

        Deque<Vertex> stack = new ArrayDeque<>();
        for (Vertex current : vertices) {
            while (stack.size() > 1) {
                Iterator<Vertex> it = stack.iterator();
                Vertex top = it.next();
                Vertex second = it.next();
                if (turnDirection(second, top, current) >= 0) {
                    stack.pop();
                } else {
                    break;
                }
            }
            stack.push(current);
        }

        if (stack.size() < 3) {
            return new int[][]{{-1}};
        }

        int[][] result = new int[stack.size()][2];
        int index = 0;
        Iterator<Vertex> descendingIterator = stack.descendingIterator();
        while (descendingIterator.hasNext()) {
            Vertex v = descendingIterator.next();
            result[index][0] = (int) v.px;
            result[index][1] = (int) v.py;
            index++;
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] samplePoints = {
            {0, 0}, {1, -4}, {-1, -5}, {-5, -3}, {-3, -1},
            {-1, -3}, {-2, -2}, {-1, -1}, {-2, -1}, {-1, 1}
        };

        int[][] hullResult = buildHull(samplePoints);

        if (hullResult.length == 1 && hullResult[0].length == 1) {
            System.out.println(hullResult[0][0]);
        } else {
            for (int[] point : hullResult) {
                System.out.println(point[0] + ", " + point[1]);
            }
        }
    }
}

