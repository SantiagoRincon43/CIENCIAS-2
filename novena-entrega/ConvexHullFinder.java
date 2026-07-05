import java.util.*;

class ConvexHullFinder {

    static class GridPoint {
        double coordX, coordY;

        GridPoint(double coordX, double coordY) {
            this.coordX = coordX;
            this.coordY = coordY;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            GridPoint other = (GridPoint) obj;
            return Double.compare(other.coordX, coordX) == 0 && Double.compare(other.coordY, coordY) == 0;
        }
    }

    static int getDirection(GridPoint p1, GridPoint p2, GridPoint p3) {
        double crossProduct = p1.coordX * (p2.coordY - p3.coordY) + 
                             p2.coordX * (p3.coordY - p1.coordY) + 
                             p3.coordX * (p1.coordY - p2.coordY);

        if (crossProduct < 0) return -1;
        if (crossProduct > 0) return 1;
        return 0;
    }

    static double getSquaredDistance(GridPoint origin, GridPoint target) {
        double diffX = origin.coordX - target.coordX;
        double diffY = origin.coordY - target.coordY;
        return (diffX * diffX) + (diffY * diffY);
    }

    static int[][] computeBoundary(int[][] coordinateMatrix) {
        int totalPoints = coordinateMatrix.length;

        if (totalPoints < 3) return new int[][]{{-1}};

        List<GridPoint> pointList = new ArrayList<>();
        for (int[] pair : coordinateMatrix) {
            pointList.add(new GridPoint(pair[0], pair[1]));
        }

        GridPoint pivot = pointList.get(0);
        for (GridPoint current : pointList) {
            if (current.coordY < pivot.coordY || (current.coordY == pivot.coordY && current.coordX < pivot.coordX)) {
                pivot = current;
            }
        }

        final GridPoint basePoint = pivot;
        pointList.sort((a, b) -> {
            int turn = getDirection(basePoint, a, b);
            if (turn == 0) {
                return Double.compare(getSquaredDistance(basePoint, a), getSquaredDistance(basePoint, b));
            }
            return Integer.compare(turn, 0);
        });

        Deque<GridPoint> hullStack = new ArrayDeque<>();
        for (GridPoint node : pointList) {
            while (hullStack.size() >= 2) {
                GridPoint top = hullStack.pollLast();
                GridPoint nextToTop = hullStack.peekLast();
                if (getDirection(nextToTop, top, node) < 0) {
                    hullStack.addLast(top);
                    break;
                }
            }
            hullStack.addLast(node);
        }

        if (hullStack.size() < 3) return new int[][]{{-1}};

        int[][] resultMatrix = new int[hullStack.size()][2];
        int index = 0;
        for (GridPoint p : hullStack) {
            resultMatrix[index][0] = (int) p.coordX;
            resultMatrix[index][1] = (int) p.coordY;
            index++;
        }

        return resultMatrix;
    }

    public static void main(String[] args) {
        int[][] inputData = {
            {0, 0}, {1, -4}, {-1, -5}, {-5, -3}, {-3, -1},
            {-1, -3}, {-2, -2}, {-1, -1}, {-2, -1}, {-1, 1}
        };
        
        int[][] solution = computeBoundary(inputData);

        if (solution.length == 1 && solution[0].length == 1) {
            System.out.println(solution[0][0]);
        } else {
            for (int[] finalPoint : solution) {
                System.out.println(finalPoint[0] + ", " + finalPoint[1]);
            }
        }
    }
}
