package ru.vsu.cs.course1;

import ru.vsu.cs.course1.Plane.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class PolygonUtils {
    private static boolean areSegmentsCrossing(Point p1, Point p2, Point p3, Point p4) {
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

        if (mulSign(p1, p2, p3) == mulSign(p1, p2, p4))
            return false;

        return mulSign(p3, p4, p1) != mulSign(p3, p4, p2);

    }

    private static int mulSign(Point p1, Point p2, Point p3) {
        // (p3-p1) x (p2-p1)
        int res = (p3.x - p1.x) * (p2.y - p1.y) - (p2.x - p1.x) * (p3.y - p1.y);
        return Integer.compare(res, 0);
    }

    private static double getLen(List<Point> points, Integer[] way) {
        double len = 0;
        int size = way.length;
        for (int i = 0; i < size; i++) {
            Point p1 = points.get(way[i]);
            Point p2 = points.get(way[(i + 1) % size]);
            len += Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        }
        return len;
    }

    public static class Perimeter {
        Integer[] way;
        double value;

        Perimeter(Integer[] way, double value) {
            this.way = way;
            this.value = value;
        }
    }

    static double getMaxPerimeter(List<Point> points, AtomicReference<Integer[]> ref) {
        if (points.size() < 2)
            return 0;
        LinkedList<Integer> list = new LinkedList<>();
        int size = points.size();
        for (int i = 0; i < size; i++)
            list.add(i);
        Perimeter res = getMaxPerimeter(points, list, new Integer[size], 0);
        if (res == null)
            return 0;
        if (ref != null)
            ref.set(res.way);
        return res.value;
    }

    private static Perimeter getMaxPerimeter(List<Point> points, LinkedList<Integer> list, Integer[] way, int next) {
        if (next > 3) {
            Point p1 = points.get(way[next - 2]);
            Point p2 = points.get(way[next - 1]);
            for (int i = 0; i < next - 3; i++) {
                Point p3 = points.get(way[i]);
                Point p4 = points.get(way[i + 1]);
                if (areSegmentsCrossing(p1, p2, p3, p4))
                    return null;
            }
        }
        if (list.size() == 0) {
            System.out.println(next);
            Point p1 = points.get(way[way.length - 1]);
            Point p2 = points.get(way[0]);
            for (int i = 1; i < way.length - 2; i++) {
                Point p3 = points.get(way[i]);
                Point p4 = points.get(way[i + 1]);
                if (areSegmentsCrossing(p1, p2, p3, p4))
                    return null;
            }
            return new Perimeter(way, getLen(points, way));
        }

        Perimeter maxRes = null;
        for (int i = 0; i < list.size(); i++) {
            Integer[] newWay = new Integer[way.length];
            System.arraycopy(way, 0, newWay, 0, way.length);
            LinkedList<Integer> newList = new LinkedList<>(list);
            newWay[next] = newList.get(i);
            if (next == 1 && newWay[next] > (points.size() + 1) / 2)
                break;
            newList.remove(i);
            Perimeter res = getMaxPerimeter(points, newList, newWay, next + 1);
            if (res != null && (maxRes == null || res.value > maxRes.value))
                maxRes = res;
        }
        return maxRes;
    }


}
