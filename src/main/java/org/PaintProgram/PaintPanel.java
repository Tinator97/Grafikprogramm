package org.PaintProgram;

//importieren der benötigten Klassen
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

public class PaintPanel extends JPanel {
    //Klassenvariablen
    private BufferedImage picture;
    private Graphics2D g2Image, g2;
    private String tool;
    private Color color;

    //Setter für Farbe
    public void setColor(Color color) {
        this.color = color;
        g2Image.fillRect(10, 10, 100, 100);
        repaint();
    }

    //Konstruktor
    public PaintPanel(int width, int height) {
        //Erstellen des BufferedImage mit weißem Hintergrund
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2Image = (Graphics2D) picture.createGraphics();
        g2Image.setColor(Color.WHITE);
        g2Image.fillRect(0, 0, width, height);

        //Initialisierung vom ausgewählten Werkzeug und der Farbe
        color = Color.BLACK;
        tool = "line";
    }

    //Methode zum Zeichnen auf der Zeichenoberfläche
    @Override
    public void paintComponent(Graphics g) {
        //Initialisieren der Zeichenfläche
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.drawImage(picture, 0, 0, null);

        g2Image.setColor(color);
        g2Image.fillRect(10, 10, 100, 100);


    }
}
