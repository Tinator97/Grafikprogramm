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
    public Panel () {
        //Größe und Farbe des Panels

        this.setBackground(Color.BLUE);

        bild = new BufferedImage(13, 13, BufferedImage.TYPE_INT_RGB);
        graphics2D = (Graphics2D) bild.getGraphics();
    }
}
