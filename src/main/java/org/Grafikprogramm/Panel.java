package org.Grafikprogramm;

//importieren der benötigten Klassen
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {
    //Klassenvariablen
    private BufferedImage bild;
    private Graphics2D graphics2D;

    //Konstruktor
    public Panel (int breite, int hoehe) {
        //Größe und Farbe des Panels
        this.setPreferredSize(new Dimension(breite, hoehe));
        this.setBackground(Color.GRAY);

        bild = new BufferedImage(breite, hoehe, BufferedImage.TYPE_INT_RGB);
        graphics2D = (Graphics2D) bild.getGraphics();
    }

}
