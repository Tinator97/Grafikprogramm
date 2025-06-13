package org.PaintProgram;

//importieren der benötigten Klassen
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PaintPanel extends JPanel {
    //Klassenvariablen
    private BufferedImage picture;
    private Graphics2D g2Image, g2;
    private String tool;
    private Point lastMousePosition;

    //Getter für Werkzeug
    public String getTool() {
        return tool;
    }

    //Getter für Farbe
    public Color getColor() {
        return g2Image.getColor();
    }

    //Setter für Werkzeug
    public void setTool(String tool) {
        this.tool = tool;
    }

    //Setter für Farbe
    public void setColor(Color color) {
        g2Image.setColor(color);
    }

    //Setter für Strichdicke
    public void setStroke(int stroke) {
        g2Image.setStroke(new BasicStroke(stroke));
    }

    //Setter für lastMousePosition
    public void setLastMousePosition(Point point) {
        this.lastMousePosition = point;
    }

    //Konstruktor
    public PaintPanel(int width, int height) {
        //Erstellen des BufferedImage mit weißem Hintergrund
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2Image = (Graphics2D) picture.createGraphics();
        g2Image.setColor(Color.WHITE);
        g2Image.fillRect(0, 0, width, height);

        //Initialisierung vom ausgewählten Werkzeug, der Farbe und der Strichdicke
        g2Image.setColor(Color.BLACK);
        tool = "brush";
        g2Image.setStroke(new BasicStroke(5));
    }

    //Methode zum Zeichnen auf der Zeichenoberfläche
    @Override
    public void paintComponent(Graphics g) {
        //Initialisieren der Zeichenfläche
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.drawImage(picture, 0, 0, null);
    }



    public void brush (Point actualMousePosition) {
        g2Image.drawLine(lastMousePosition.x, lastMousePosition.y, actualMousePosition.x, actualMousePosition.y);
        lastMousePosition.x = actualMousePosition.x;
        lastMousePosition.y = actualMousePosition.y;
        repaint();
    }

    public void line (Point actualMousePosition) {
        g2Image.drawLine(lastMousePosition.x, lastMousePosition.y, actualMousePosition.x, actualMousePosition.y);
        repaint();
    }

    public void rectangle (Point actualMousePosition) {
        int width = actualMousePosition.x - lastMousePosition.x;
        int height = actualMousePosition.y - lastMousePosition.y;
        int temp;
        if (width < 0) {
            temp = actualMousePosition.x;
            actualMousePosition.x = lastMousePosition.x;
            lastMousePosition.x = temp;
            width = -width;
        }
        if (height < 0) {
            temp = actualMousePosition.y;
            actualMousePosition.y = lastMousePosition.y;
            lastMousePosition.y = temp;
            height = -height;
        }
        g2Image.drawRect(lastMousePosition.x, lastMousePosition.y, width, height);
        repaint();
    }

    public void ellipse (Point actualMousePosition) {
        int width = actualMousePosition.x - lastMousePosition.x;
        int height = actualMousePosition.y - lastMousePosition.y;
        int temp;
        if (width < 0) {
            temp = actualMousePosition.x;
            actualMousePosition.x = lastMousePosition.x;
            lastMousePosition.x = temp;
            width = -width;
        }
        if (height < 0) {
            temp = actualMousePosition.y;
            actualMousePosition.y = lastMousePosition.y;
            lastMousePosition.y = temp;
            height = -height;
        }
        g2Image.drawOval(lastMousePosition.x, lastMousePosition.y, width, height);
        repaint();
    }

    public void erase (Point actualMousePosition) {
        brush(actualMousePosition);
    }

    public void save (File outputFile) {
        try {
            ImageIO.write(picture, "jpg", outputFile);
        } catch (IOException exc){
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern");
        }
    }

    public void load (File inputFile) {
        try {
            picture = ImageIO.read(inputFile);
            g2Image = (Graphics2D) picture.createGraphics();
            repaint();
            g2Image.setColor(Color.BLACK);
            tool = "brush";
            g2Image.setStroke(new BasicStroke(5));
        } catch (IOException exc){
            JOptionPane.showMessageDialog(this, "Fehler beim Laden");
        }
    }
}