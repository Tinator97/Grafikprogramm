package org.Grafikprogramm;

//importieren der benötigten Klassen
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {
    //Klassenvariablen
    private BufferedImage bild;
    private Graphics2D g2Image;

    //Konstruktor
    public Panel (int breite, int hoehe) {
        //Erstellen des BufferedImage mit weißem Hintergrund
        bild = new BufferedImage(breite, hoehe, BufferedImage.TYPE_INT_RGB);
        g2Image = (Graphics2D) bild.createGraphics();
        g2Image.setColor(Color.WHITE);
        g2Image.fillRect(0,0,breite,hoehe);
    }

    //Methode zum Zeichnen auf der Zeichenoberfläche
    @Override
    public void paintComponent (Graphics g) {
        //Initialisieren der Zeichenfläche
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(bild, 0, 0, null);

        g2Image.setColor(Color.GREEN);
        g2Image.fillOval(100,100,200,200);
    }
}
