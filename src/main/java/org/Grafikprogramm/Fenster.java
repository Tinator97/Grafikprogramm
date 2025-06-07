package org.Grafikprogramm;

//importieren der benötigten Klassen
import javax.swing.*;
import java.awt.*;

public class Fenster extends JFrame {
    //Klassenvariablen
        private Panel panel;

    //Konstruktor
    public Fenster (String fenstertitel) {
        //Erstellen des Fensters
        super(fenstertitel);

        //Größe des Fensters - Ermitteln der Auflösung des Betriebssystems
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int breite = graphicsDevice.getDisplayMode().getWidth();
        int hoehe = graphicsDevice.getDisplayMode().getHeight() - 50;
        this.setSize(breite, hoehe);

        //Hinzufügen des Panels - der Zeichenfläche
        panel = new Panel(breite, hoehe);
        this.add(panel);

        //Layout des Fensters
        //Fenstergröße fixieren
        //Sichtbarkeit und Beenden des Programms beim Schließen
        this.setLayout(new FlowLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
