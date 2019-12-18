import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class Board extends JPanel {

    private SplayTree<Integer> splayTree;
    private int fistX;
    private int fistY;
    private Graphics2D graphics2D;
    private int count = 0;

    Board(int fromX, int fromY) {
        splayTree = new SplayTree<>();
        this.fistX = fromX;
        this.fistY = fromY;
    }

    public void addNode(int data) {
        splayTree.add(data);
        this.repaint();
    }

    public void removeNode(int data) {
        splayTree.remove(data);
        repaint();
    }

    public void clearBoard() {
        splayTree.clear();
        repaint();
    }

    private void paintPath(int x, int y, Graphics2D gr2d, String data) {
        Ellipse2D ellipse2D = new Ellipse2D.Double(x, y, 35.0, 35.0);
        gr2d.draw(ellipse2D);
        gr2d.setColor(new Color(250, 200, 200));
        gr2d.fill(ellipse2D);
        gr2d.setColor(Color.BLACK);
        gr2d.drawString(data, x + 13, y + 22);
    }

    private void paintOver(SplayNode<Integer> current, int x, int y, boolean isLeft) {
        Graphics2D g2 = graphics2D;
        int currentX;
        int currentY;

        if (current == null) {
            return;
        }

        if (current == splayTree.getRoot()) {

            currentX = x;
            currentY = y;
            paintPath(x, y, g2, String.valueOf(current.getValue()));
        } else if (isLeft) {

            currentX = count + x - 150;
            currentY = count + y + 60;

            paintPath(currentX, currentY, g2, String.valueOf(current.getValue()));
            GeneralPath generalPath = new GeneralPath();
            generalPath.moveTo(x + 17.5, y + 35);
            generalPath.lineTo(currentX + 17.5, currentY);
            generalPath.closePath();
            g2.draw(generalPath);
        } else {

            currentX = x + 150 - count;
            currentY = y + 60 + count;

            paintPath(currentX, currentY, g2, String.valueOf(current.getValue()));
            GeneralPath generalPath = new GeneralPath();
            generalPath.moveTo(x + 17.5, y + 35);
            generalPath.lineTo(currentX + 17.5, currentY);
            generalPath.closePath();
            g2.draw(generalPath);
        }
        count += 30;
        paintOver(current.getLeft(), currentX, currentY, true);
        paintOver(current.getRight(), currentX, currentY, false);
        count -= 30;

    }

    protected void paintComponent(Graphics g) {
        if (splayTree.getRoot() == null) return;
        graphics2D = (Graphics2D) g;
        paintOver(splayTree.getRoot(), fistX, fistY, true);
    }
}