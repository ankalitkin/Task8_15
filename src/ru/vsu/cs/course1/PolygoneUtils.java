package ru.vsu.cs.course1;

import ru.vsu.cs.course1.Plane.Point;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PolygoneUtils {
    public static boolean areSegmentsCrossing(Point p1, Point p2, Point p3, Point p4) {
        int x1 = Math.min(p1.x, p2.x);
        int x2 = Math.max(p1.x, p2.x);
        int y1 = Math.min(p1.y, p2.y);
        int y2 = Math.max(p1.y, p2.y);
        int x3 = Math.min(p3.x, p4.x);
        int x4 = Math.max(p3.x, p4.x);
        int y3 = Math.min(p3.y, p4.y);
        int y4 = Math.max(p3.y, p4.y);

        if (x2 < x3 || x4 < x1 || y2 < y3 || y4 < y1)
            return false;

        if (mulSignum(p1, p2, p3) == mulSignum(p1, p2, p4))
            return false;

        return mulSignum(p3, p4, p1) != mulSignum(p3, p4, p2);

    }

    private static int mulSignum(Point p1, Point p2, Point p3) {
        // (p3-p1) x (p2-p1)
        int res = (p3.x - p1.x) * (p2.y - p1.y) - (p2.x - p1.x) * (p3.y - p1.y);
        return Integer.compare(res, 0);
    }


    /*private boolean[][][][] getAllCrossings(List<Point> list) {
        int size = list.size();
        boolean[][][][] crossings = new boolean[size][size][size][size];
        for (int y = 0; y < size; y++) {
            Point p1 = list.get(y);
            for (int u = 0; u < size; u++) {
                Point p2 = list.get(u);
                if (p2 == p1)
                    continue;
                for (int v = 0; v < size; v++) {
                    Point p3 = list.get(v);
                    if (p3 == p1 || p3 == p2)
                        continue;
                    for (int w = 0; w < size; w++) {
                        Point p4 = list.get(w);
                        if (p4 == p1 || p4 == p2 || p4 == p3)
                            continue;
                        if (areSegmentsCrossing(p1, p2, p3, p4))
                            crossings[y][u][v][w] = true;
                    }
                }
            }
        }
        return crossings;
    }*/

    private static boolean checkCrossings(List<Point> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Point p1 = list.get(i);
            Point p2 = list.get((i + 1) % size);
            for (int j = 0; j < size; j++) {
                Point p3 = list.get(j);
                Point p4 = list.get((j + 1) % size);
                if (p1 == p3 || p1 == p4 || p2 == p3 || p2 == p4)
                    continue;
                if (areSegmentsCrossing(p1, p2, p3, p4))
                    return false;
            }
        }
        return true;
    }

    private static int fact(int n) {
        int res = 1;
        for (int i = 1; i <= n; i++)
            res *= n;
        return res;
    }

    private static double getLen(List<Point> list) {
        double len = 0;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Point p1 = list.get(i);
            Point p2 = list.get((i + 1) % size);
            len += Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        }
        return len;
    }


    public static double getMaxPerimeter(List<Point> points, AtomicReference<List<Point>> ref) {
        /*HashMap<Point, Integer> hash = new HashMap<>();
        int i = 0;
        for (Point point : points)
            hash.put(point, i++);
        boolean[][][][] crossings = getAllCrossings(points);
         */
        double max = 0;

        /*for (long seed = 0; seed < (long) 1 << points.size(); seed++) {
            long currentseed = seed;
            LinkedList<Point> list = new LinkedList<>();
            for (Point point : points) {
                if (currentseed % 2 > 0)
                    list.add(point);
                currentseed /= 2;
            }*/

        if (points.size() < 3)
            return max;

        for (int i = 0; i < fact(points.size()-1 ); i++) {
            LinkedList<Point> list = new LinkedList<>(points);
            LinkedList<Point> newList = new LinkedList<>();
            newList.addLast(list.getFirst());
            list.removeFirst();
            while (list.size() > 0) {
                int n = i % list.size();
                newList.addLast(list.get(n));
                list.remove(n);
            }
            if (!checkCrossings(newList))
                continue;

            double len = getLen(newList);
            ref.set(newList);
            return len; ///!!!
            /*if (max < len) {
                max = len;
                if (ref != null)
                    ref.set(newList);
            }*/
        }
        //}
        return max;
    }
}
