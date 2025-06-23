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
    private Point endPointOfShape;

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
    public float getStroke() {
        BasicStroke stroke = (BasicStroke) g2Image.getStroke();
        return stroke.getLineWidth();
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
    public void setStroke(float stroke) {
        g2Image.setStroke(new BasicStroke(stroke));
    }
    public void setLastMousePosition(Point point) {
        this.lastMousePosition = point;
    }
    public void setEndPointOfShape(Point point) {this.endPointOfShape = point;}

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
        g2Image.setStroke(new BasicStroke(5.0F));

        //Initialisierung weiterer Variablen
        lastMousePosition = new Point(0,0);
        endPointOfShape = new Point(0,0);
    }

    //Methode paintComponent muss zum Zeichnen auf der Zeichenoberfläche überschrieben werden
    @Override
    public void paintComponent(Graphics g) {
        //Initialisieren der Zeichenfläche
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Zeichnen des Bildes auf die Zeichenfläche
        g2.drawImage(picture, 0, 0, null);

        //Farbe und Strichdicke der Vorschau beim Ziehen an Auswahl anpassen
        g2.setColor(getColor());
        g2.setStroke(g2Image.getStroke());
        //Zeichnen der Vorschau beim Ziehen für Linien
        if (tool.equals("line")) g2.drawLine(lastMousePosition.x,lastMousePosition.y, endPointOfShape.x, endPointOfShape.y);
        //Zeichnen der Vorschau beim Ziehen für Rechtecke
        if (tool.equals("rectangle")) {
            int [] rectangle = checkOrientation(lastMousePosition, endPointOfShape);
            g2.drawRect(rectangle[0], rectangle[1], rectangle[2], rectangle[3]);
        }
        //Zeichnen der Vorschau beim Ziehen für Ellipsen
        if (tool.equals("ellipse")) {
            int [] ellipse = checkOrientation(lastMousePosition, endPointOfShape);
            g2.drawOval(ellipse[0], ellipse[1], ellipse[2], ellipse[3]);
        }
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
        int [] rectangle = checkOrientation(lastMousePosition, actualMousePosition);
        //Zeichnen des Rechtecks
        g2Image.drawRect(rectangle[0], rectangle[1], rectangle[2], rectangle[3]);
        repaint();
    }

    //Methode zum Zeichnen von Ellipsen
    public void ellipse (Point actualMousePosition) {
        //Berechnung der Abmaße der Ellipse
        int [] ellipse = checkOrientation(lastMousePosition, actualMousePosition);
        //Zeichnen der Ellipse
        g2Image.drawOval(ellipse[0], ellipse[1], ellipse[2], ellipse[3]);
        repaint();
    }

    //Methode zum Radieren
    public void erase (Point actualMousePosition) {
        //Farbe Weiß wurde bereits im Frame gesetzt. Der Radierer arbeitet exakt wie der Pinsel und ruft nur diese Funktion auf.
        brush(actualMousePosition);
    }

    //Methode zum Überprüfen, in welche Richtung Figuren gezogen werden (Behandlung von negativen Werten)
    public int [] checkOrientation(Point startPoint, Point endPoint) {
        //Lokale Variablen, um Sie bei Bedarf vertauschen zu können
        int x1 = startPoint.x, y1 = startPoint.y, x2 = endPoint.x, y2 = endPoint.y;
        //Berechnung der Abmaße der Figur
        int width = x2 - x1;
        int height = y2 - y1;
        //Überprüfung, in welche Richtung die Maus gezogen wird. Falls ein negativer Wert entsteht, wird der Anfangspunkt
        //auf den ursprünglichen Endpunkt gesetzt
        if (width < 0) {
            x1 = x2;
            width = -width;
        }
        if (height < 0) {
            y1 = y2;
            height = -height;
        }
        return new int [] {x1, y1, width, height};
    }

    //Methode zum Speichern des Bildes
    public void save (File outputFile) {
        //Speichern des Bildes am übergebenen Ort. Falls ein Fehler auftritt, kommt ein Popup mit entsprechender Meldung.
        try {
            ImageIO.write(picture, "jpg", outputFile);
        } catch (IOException exc){
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern des Bildes!");
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
            g2Image.setStroke(new BasicStroke(5.0F));
        } catch (IOException exc){
            JOptionPane.showMessageDialog(this, "Fehler beim Laden des Bildes!");
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
        g2Image.setStroke(new BasicStroke(5.0F));
    }

    //Überladene Funktion zum Erzeugen einer neuen Zeichenoberfläche mit anderer Größe
    public void newPanel(int width, int height) {
        //Setzen der neuen Größe der Zeichenfläche
        setPreferredSize(new Dimension(width, height));
        //Erstellen eines neuen Bildes mit Grafik und anpassen dessen Größe
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2Image = picture.createGraphics();
        setSize(new Dimension(width, height));
        //Aufruf der überlagerten Funktion
        newPanel();
    }
}