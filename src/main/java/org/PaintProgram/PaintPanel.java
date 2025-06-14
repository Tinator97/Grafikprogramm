package org.PaintProgram;

//importieren der benötigten Klassen
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Klasse, die die Zeichenfläche darstellt
public class PaintPanel extends JPanel {
    //Klassenvariablen
    private BufferedImage picture;
    private Graphics2D g2Image;
    private String tool;
    private Point lastMousePosition;
    private Color lastColor;

    //Getter
    public String getTool() {
        return tool;
    }
    public Color getColor() {
        return g2Image.getColor();
    }
    public Color getLastColor() {
        return lastColor;
    }

    //Setter
    public void setTool(String tool) {
        this.tool = tool;
    }
    public void setColor(Color color) {
        g2Image.setColor(color);
    }
    public void setLastColor(Color color) {
        this.lastColor = color;
    }
    public void setStroke(int stroke) {
        g2Image.setStroke(new BasicStroke(stroke));
    }
    public void setLastMousePosition(Point point) {
        this.lastMousePosition = point;
    }

    //Konstruktor
    public PaintPanel(int width, int height) {
        //Erstellen des BufferedImage mit weißem Hintergrund
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2Image = picture.createGraphics();
        g2Image.setColor(Color.WHITE);
        g2Image.fillRect(0, 0, width, height);

        //Initialisierung vom ausgewählten Werkzeug, der Farbe und der Strichdicke
        tool = "brush";
        g2Image.setColor(Color.BLACK);
        g2Image.setStroke(new BasicStroke(5));
    }

    //Methode paintComponent muss zum Zeichnen auf der Zeichenoberfläche überschrieben werden
    @Override
    public void paintComponent(Graphics g) {
        //Initialisieren der Zeichenfläche
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Zeichnen des Bildes auf die Zeichenfläche
        g2.drawImage(picture, 0, 0, null);
    }

    //Methode zum Arbeiten mit dem Pinsel
    public void brush (Point actualMousePosition) {
        //Es wird eine kleine Linie von der jeweils letzten gespeicherten Mausposition bis zur aktuellen Position gezeichnet
        line(actualMousePosition);
        //Anschließend wird die letzte Mausposition aktualisiert
        lastMousePosition.x = actualMousePosition.x;
        lastMousePosition.y = actualMousePosition.y;
        //So entsteht ein Pinselstrich entlang des Mauspfades
    }

    //Methode zum Zeichnen von Linien
    public void line (Point actualMousePosition) {
        //Zeichnen der Linie
        g2Image.drawLine(lastMousePosition.x, lastMousePosition.y, actualMousePosition.x, actualMousePosition.y);
        repaint();
    }

    //Methode zum Zeichnen von Rechtecken
    public void rectangle (Point actualMousePosition) {
        //Berechnung der Abmaße des Rechtecks
        int width = actualMousePosition.x - lastMousePosition.x;
        int height = actualMousePosition.y - lastMousePosition.y;
        //Überprüfung, in welche Richtung die Maus gezogen wird. Falls ein negativer Wert entsteht, werden Anfangs- und Endpunkt
        //des Rechtecks entsprechend vertauscht
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
        //Zeichnen des Rechtecks
        g2Image.drawRect(lastMousePosition.x, lastMousePosition.y, width, height);
        repaint();
    }

    //Methode zum Zeichnen von Ellipsen
    public void ellipse (Point actualMousePosition) {
        //Berechnung der Abmaße der Ellipse
        int width = actualMousePosition.x - lastMousePosition.x;
        int height = actualMousePosition.y - lastMousePosition.y;
        //Überprüfung, in welche Richtung die Maus gezogen wird. Falls ein negativer Wert entsteht, werden Anfangs- und Endpunkt
        //der Ellipse entsprechend vertauscht
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
        //Zeichnen der Ellipse
        g2Image.drawOval(lastMousePosition.x, lastMousePosition.y, width, height);
        repaint();
    }

    //Methode zum Radieren
    public void erase (Point actualMousePosition) {
        //Farbe Weiß wurde bereits im Frame gesetzt. Der Radierer arbeitet exakt wie der Pinsel und ruft nur diese Funktion auf.
        brush(actualMousePosition);
    }

    //Methode zum Speichern des Bildes
    public void save (File outputFile) {
        //Speichern des Bildes am übergebenen Ort. Falls ein Fehler auftritt, kommt ein Popup mit entsprechender Meldung.
        try {
            ImageIO.write(picture, "jpg", outputFile);
        } catch (IOException exc){
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern");
        }
    }

    //Methode zum Laden eines Bildes
    public void load (File inputFile) {
        //Laden eines Bildes vom übergebenen Ort. Falls ein Fehler auftritt, kommt ein Popup mit entsprechender Meldung.
        try {
            //Laden des Bildes
            picture = ImageIO.read(inputFile);
            //Verknüpfen des Graphics2D zum Bild
            g2Image = picture.createGraphics();
            //Darstellen des geladenen Bildes
            repaint();
            //Auswählen der Standardtools
            g2Image.setColor(Color.BLACK);
            tool = "brush";
            g2Image.setStroke(new BasicStroke(5));
        } catch (IOException exc){
            JOptionPane.showMessageDialog(this, "Fehler beim Laden");
        }
    }

    //Überladene Funktion zum Erzeugen einer neuen Zeichenoberfläche mit gleicher Größe
    public void newPanel() {
        //Zeichenfläche wird weiß übermalt
        g2Image.setColor(Color.WHITE);
        g2Image.fillRect(0, 0, picture.getWidth(), picture.getHeight());
        repaint();
        //Auswählen der Standardtools
        g2Image.setColor(Color.BLACK);
        tool = "brush";
        g2Image.setStroke(new BasicStroke(5));
    }

    //Überladene Funktion zum Erzeugen einer neuen Zeichenoberfläche mit anderer Größe
    public void newPanel(int width, int height) {
        //Setzen der neuen Größe der Zeichenfläche
        setPreferredSize(new Dimension(width, height));
        //Erstellen eines neuen Bildes mit Grafik und anpassen dessen Größe
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2Image = picture.createGraphics();
        setSize(new Dimension(width, height));
        //Ändern der Hintergrundfarbe zu weiß
        g2Image.setColor(Color.WHITE);
        g2Image.fillRect(0, 0, width, height);
        repaint();
        //Auswählen der Standardtools
        g2Image.setColor(Color.BLACK);
        tool = "brush";
        g2Image.setStroke(new BasicStroke(5));
    }
}