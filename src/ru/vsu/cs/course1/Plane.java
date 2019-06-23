package ru.vsu.cs.course1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Plane implements Icon {
    private static HashMap<RenderingHints.Key, Object> rh;

    JLabel label;
    JComponent parent;
    Point selected;

    public class Point {
        public int x, y, z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public Plane(JLabel label, JComponent parent) {
        this.label = label;
        this.parent = parent;
        rh = new HashMap<>();
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                selected = getClosest(e.getX(), e.getY());
                if(selected == null && e.getButton() == 1) {
                    selected = addNewPoint(e.getX(), e.getY());
                }
                else if(selected != null && e.getButton() == 3) {
                    points.remove(selected);
                    selected = null;
                }
                label.paintImmediately(0, 0, getIconWidth(), getIconHeight());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selected = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        label.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(selected == null)
                    return;
                selected.x = e.getX();
                selected.y = e.getY();
                selected.z = nextZ();
                label.paintImmediately(0, 0, getIconWidth(), getIconHeight());
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    @Override
    public void paintIcon(Component c, Graphics gg, int x, int y) {
        Graphics2D g = (Graphics2D)gg;
        g.setRenderingHints(rh);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getIconWidth(), getIconHeight());
        for(Point point : points) {
            g.setColor(Color.BLUE);
            g.fillRect(point.x-dx, point.y-dy, 2*dx, 2*dy);
        }
        AtomicReference<List<Point>> ref = new AtomicReference<>();
        String perimeter = String.valueOf(getPerimeter(ref));
        List<Point> list = ref.get();
        if(list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Point p1 = list.get(i);
                Point p2 = list.get((i + 1) % size);
                g.setColor(Color.RED);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        g.setFont(new Font("Segoe UI", Font.BOLD, 32));
        g.setColor(Color.BLUE);
        int width = g.getFontMetrics().stringWidth(perimeter);
        g.drawString(perimeter, getIconWidth()-width-10, getIconHeight()-10);
    }

    @Override
    public int getIconWidth() {
        return parent.getWidth();
    }

    @Override
    public int getIconHeight() {
        return parent.getHeight();
    }
    public ArrayList<Point> points = new ArrayList<>();
    private int z;
    private int dx = 4, dy = 4;

    private int nextZ() {
        return z++;
    }

    public Point getClosest(int x, int y) {
        Point closest = null;
        for(Point point : points) {
            if(Math.abs(x-point.x) < dx && Math.abs(y-point.y) < dy && (closest == null || point.x > closest.z)) {
                closest = point;
            }
        }
        return closest;
    }

    public Point addNewPoint(int x, int y) {
        Point p = new Point(x, y, nextZ());
        points.add(p);
        return p;
    }

    public double getPerimeter(AtomicReference<List<Point>> ref) {
            return PolygoneUtils.getMaxPerimeter(points, ref);
    }

}
