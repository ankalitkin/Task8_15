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

    private static boolean checkCrossings(List<Point> points, List<Integer> list) {
        int size = list.size();
        int counter = 0;
        for (int i = 0; i < size; i++) {
            Point p1 = points.get(list.get(i));
            Point p2 = points.get(list.get((i + 1) % size));
            for (int j = 0; j < size; j++) {
                Point p3 = points.get(list.get(j));
                Point p4 = points.get(list.get((j + 1) % size));
                if (p1 == p3 || p1 == p4 || p2 == p3 || p2 == p4)
                    continue;
                if (areSegmentsCrossing(p1, p2, p3, p4)) {
                    System.out.println(counter);
                    return false;
                }
                counter++;
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

    private static double getLen(List<Point> points, List<Integer> list) {
        double len = 0;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Point p1 = points.get(list.get(i));
            Point p2 = points.get(list.get((i + 1) % size));
            len += Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        }
        return len;
    }


    public static double getMaxPerimeter(List<Point> points, AtomicReference<List<Integer>> ref) {
        double max = 0;

        if (points.size() < 3)
            return max;

        long millis = System.currentTimeMillis();
        for (int i = 0; i < fact(points.size()-1)/2; i++) {
            LinkedList<Integer> list = new LinkedList<>();
            for (int j = 1; j < points.size(); j++)
                list.add(j);
            LinkedList<Integer> newList = new LinkedList<>();
            newList.add(0);
            while (list.size() > 0) {
                int n = (i / fact(list.size()-1)) % list.size();
                newList.addLast(list.get(n));
                list.remove(n);
            }
            if (!checkCrossings(points, newList))
                continue;

            double len = getLen(points, newList);

            if (max < len) {
                max = len;
                if (ref != null)
                    ref.set(newList);
            }
        }
        System.out.println(System.currentTimeMillis() - millis+" ms");
        return max;
    }
}
