package Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Coordinate implements Comparable<Coordinate> {

    private final long xPos;
    private final long yPos;

    public Coordinate(long xPos, long yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public long getX() {
        return xPos;
    }

    public long getY() {
        return yPos;
    }

    @Override
    public int compareTo(Coordinate other) {
        if (this.xPos != other.xPos) {
            return Long.compare(this.xPos, other.xPos);
        }
        return Long.compare(this.yPos, other.yPos);
    }
}

public class MonotoneChainConvexHull {

    private static long orientation(Coordinate origin, Coordinate first, Coordinate second) {
        long dx1 = first.getX() - origin.getX();
        long dy1 = first.getY() - origin.getY();
        long dx2 = second.getX() - origin.getX();
        long dy2 = second.getY() - origin.getY();
        return dx1 * dy2 - dy1 * dx2;
    }

    public static List<Coordinate> computeHull(List<Coordinate> inputPoints) {
        int total = inputPoints.size();

        if (total <= 3) {
            return inputPoints;
        }

        List<Coordinate> sorted = new ArrayList<>(inputPoints);
        Collections.sort(sorted);

        List<Coordinate> hull = new ArrayList<>(2 * total);
        int size = 0;

        for (int i = 0; i < total; i++) {
            while (size >= 2 && orientation(hull.get(size - 2), hull.get(size - 1), sorted.get(i)) <= 0) {
                hull.remove(--size);
            }
            hull.add(sorted.get(i));
            size++;
        }

        int lowerSize = size;
        for (int i = total - 2; i >= 0; i--) {
            while (size > lowerSize && orientation(hull.get(size - 2), hull.get(size - 1), sorted.get(i)) <= 0) {
                hull.remove(--size);
            }
            hull.add(sorted.get(i));
            size++;
        }

        hull.remove(hull.size() - 1);
        return hull;
    }

public static void main(String[] args) {
        List<Coordinate> samplePoints = new ArrayList<>();
        samplePoints.add(new Coordinate(0, 3));
        samplePoints.add(new Coordinate(2, 2));
        samplePoints.add(new Coordinate(1, 1));
        samplePoints.add(new Coordinate(2, 1));
        samplePoints.add(new Coordinate(3, 0));
        samplePoints.add(new Coordinate(0, 0));
        samplePoints.add(new Coordinate(3, 3));

        List<Coordinate> result = computeHull(samplePoints);

        for (Coordinate point : result) {
            System.out.println("(" + point.getX() + ", " + point.getY() + ")");
        }
    }
}

