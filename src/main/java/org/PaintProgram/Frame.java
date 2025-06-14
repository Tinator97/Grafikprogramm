package org.PaintProgram;

//importieren der benötigten Klassen
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import static java.awt.event.KeyEvent.*;
import static java.lang.Integer.parseInt;

//Klasse, die das Anwendungsfenster darstellt
public class Frame extends JFrame {
    //Klassenvariablen
    private final PaintPanel paintPanel;
    private final JFileChooser fileChooser;
    private JToolBar toolBar;

    private JTextField strokeField;

    //Konstanten für Werkzeuge, um Tippfehler z.B. bei den Action-commands zu vermeiden
    private final String brushString = "brush", lineString = "line", rectangleString = "rectangle", ellipseString = "ellipse", eraserString = "eraser";


    //Konstruktor
    public Frame (String frameTitel) {
        //Erstellen des Fensters
        super(frameTitel);

        //Größe des Fensters - Ermitteln der Auflösung des Betriebssystems
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        //Setzen der Standard-Fenstergröße auf die Hälfte der Bildschirmgröße
        this.setSize(graphicsDevice.getDisplayMode().getWidth()/2, graphicsDevice.getDisplayMode().getHeight()/2);
        //Maximiert starten
        this.setExtendedState(MAXIMIZED_BOTH);

        //Layout des Fensters
        this.setLayout(new BorderLayout());
        //Beenden des Programms beim Schließen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Erzeugen der Zeichenfläche mit Standardgröße
        paintPanel = new PaintPanel(1600, 900);
        //PreferredSize muss gesetzt werden, damit das Panel auch bei kleinerem Fenster seine Größe behält
        paintPanel.setPreferredSize(new Dimension(1600, 900));
        //Hinzufügen von Scrollbars
        JScrollPane scrollPane = new JScrollPane(paintPanel);
        //Hinzufügen des Panels zum Fenster
        this.add(scrollPane, BorderLayout.CENTER);
        //erstellen der MouseListener
        paintPanel.addMouseListener(new MouseListener());
        paintPanel.addMouseMotionListener(new MouseMotionListener());

        //Methoden zum Erstellen der Menübar und der Symbolleiste inkl. interaktiver Elemente
        createMenuBar();
        createSymbolBar();

        //Sichtbarkeit des Fensters
        this.setVisible(true);

        //Erstellen des FileChoosers
        fileChooser = new JFileChooser();
        //Standardfilter auf JPG-Dateien
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPG","jpg"));
        //Standardverzeichnis beim Speichern/Laden im Projektverzeichnis im Ordner "savedPictures"
        fileChooser.setCurrentDirectory(new File ("savedPictures"));
    }

    //Erstellen der MenuBar mit den einzelnen Untermenüs und Items inkl. Symbolen und ShortCuts mit lokalen Variablen
    //Für die Items werden gesetzt: Text, Shortcut, Bild, ActionCommand; ActionListener werden erstellt
    //mit F10 kann die MenuBar auch mit der Tastatur gesteuert werden
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Datei");
        menuBar.add(fileMenu);

        JMenu toolMenu = new JMenu("Werkzeuge");
        menuBar.add(toolMenu);

        JMenuItem newSameSizeItem = new JMenuItem("Neu");
        newSameSizeItem.setIcon(new ImageIcon("icons/toolbarButtonGraphics/general/Add16.gif"));
        newSameSizeItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newSameSizeItem);
        newSameSizeItem.setActionCommand("newSameSize");
        newSameSizeItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem newOtherSizeItem = new JMenuItem("Neu (Blattgröße anpassen)");
        newOtherSizeItem.setIcon(new ImageIcon("icons/toolbarButtonGraphics/general/Add16.gif"));
        newOtherSizeItem.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newOtherSizeItem);
        newOtherSizeItem.setActionCommand("newOtherSize");
        newOtherSizeItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem loadItem = new JMenuItem("Laden");
        loadItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(loadItem);
        loadItem.setActionCommand("load");
        loadItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem saveItem = new JMenuItem("Speichern");
        saveItem.setIcon(new ImageIcon("icons/toolbarButtonGraphics/general/save16.gif"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveItem);
        saveItem.setActionCommand("save");
        saveItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem closeItem = new JMenuItem("Beenden");
        fileMenu.add(closeItem);
        closeItem.setActionCommand("close");
        closeItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem brushItem = new JMenuItem("Pinsel");
        toolMenu.add(brushItem);
        brushItem.setActionCommand(brushString);
        brushItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem lineItem = new JMenuItem("Linie");
        toolMenu.add(lineItem);
        lineItem.setActionCommand(lineString);
        lineItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem rectangleItem = new JMenuItem("Viereck");
        toolMenu.add(rectangleItem);
        rectangleItem.setActionCommand(rectangleString);
        rectangleItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem ellipseItem = new JMenuItem("Ellipse");
        toolMenu.add(ellipseItem);
        ellipseItem.setActionCommand(ellipseString);
        ellipseItem.addActionListener(new ButtonTextFieldListener());

        JMenuItem eraserItem = new JMenuItem("Radierer");
        toolMenu.add(eraserItem);
        eraserItem.setActionCommand(eraserString);
        eraserItem.addActionListener(new ButtonTextFieldListener());

        setJMenuBar(menuBar);
    }

    // Erstellen der SymbolBar mit den einzelnen Buttons für Werkzeuge inkl. Symbolen und ShortCuts
    private void createSymbolBar() {
        toolBar = new JToolBar();

        //Erzeugen der Buttons für die Tools. Shortcuts orientieren sich an den deutschen Begriffen.
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", VK_P, brushString, "Pinsel");
        createButton("icons/toolbarButtonGraphics/general/linie64.gif", VK_L, lineString, "Linie");
        createButton("icons/toolbarButtonGraphics/general/rechteck64.gif", VK_V, rectangleString, "Viereck");
        createButton("icons/toolbarButtonGraphics/general/ellipse64.gif", VK_E, ellipseString, "Ellipse");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", VK_R, eraserString, "Radierer");

        //Bereich für die Strichdicke
        JLabel strokeLabel = new JLabel("Strichdicke");
        toolBar.add(strokeLabel);
        strokeField = new JTextField("3");
        strokeField.setMaximumSize(new Dimension(40,30));
        strokeField.setToolTipText("Strichdicke in Pixeln");
        strokeField.setHorizontalAlignment(JTextField.CENTER);
        toolBar.add(strokeField);
        strokeField.setActionCommand("stroke");
        strokeField.addActionListener(new ButtonTextFieldListener());

        //Bereich für die Farbauswahl
        //Erzeugen der benötigten Buttons
        JLabel colorsLabel = new JLabel("Farben");
        toolBar.add(colorsLabel);
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "black", "schwarz");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "red", "rot");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "blue", "blau");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "yellow", "gelb");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "white", "weiß");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "cyan", "cyan");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "green", "grün");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "magenta", "magenta");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "orange", "orange");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "pink", "pink");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "lightgray", "hellgrau");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "gray", "grau");
        createButton("icons/toolbarButtonGraphics/development/Bean24.gif", 0, "darkgray", "dunkelgrau");

        this.add(toolBar, BorderLayout.NORTH);
    }

    //Eigene Methode um Buttons vollständig zu implementieren
    //jeder Button kann bekommen: Bild, Shortcut, Tooltip, ActionCommand, Action´Listener
    private void createButton(String imageIconFilename, int mnemonic, String actionCommand, String tooltip) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(imageIconFilename));
        button.setMnemonic(mnemonic);
        button.setToolTipText(tooltip);
        toolBar.add(button);
        button.setActionCommand(actionCommand);
        button.addActionListener(new ButtonTextFieldListener());
    }

    //Klasse zum Ausführen von Befehlen nach Knopfdruck bzw. beim Bearbeiten von Textfeldern
    class ButtonTextFieldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Funktion, um beim Zurückwechseln vom Radierer wieder die ursprüngliche Farbe zugeordnet zu bekommen
            if (paintPanel.getTool().equals(eraserString) && !e.getActionCommand().equals(eraserString)) paintPanel.setColor(paintPanel.getLastColor());

            //Funktionen, um das Werkzeug auszuwählen
            if (e.getActionCommand().equals(brushString)) paintPanel.setTool(brushString);
            if (e.getActionCommand().equals(lineString)) paintPanel.setTool(lineString);
            if (e.getActionCommand().equals(rectangleString)) paintPanel.setTool(rectangleString);
            if (e.getActionCommand().equals(ellipseString)) paintPanel.setTool(ellipseString);
            //Radierer darf nur gewählt werden, wenn er noch nicht ausgewählt ist, sonst wird die vorher gewählte Farbe mit weiß überschrieben
            if (e.getActionCommand().equals(eraserString) && !paintPanel.getTool().equals(eraserString)) {
                //Speichern der aktuellen Farbe
                paintPanel.setLastColor(paintPanel.getColor());
                //Setzen der aktuellen Farbe auf Weiß, da der Radierer eigentlich ein weißer Pinsel ist
                paintPanel.setColor(Color.WHITE);
                paintPanel.setTool(eraserString);
            }

            //Funktion zum Setzen der Strichdicke
            if (e.getActionCommand().equals("stroke")) paintPanel.setStroke(parseInt(strokeField.getText()));

            //Funktion zum Setzen der Farbe
            if (e.getActionCommand().equals("black")) paintPanel.setColor(Color.BLACK);
            if (e.getActionCommand().equals("red")) paintPanel.setColor(Color.RED);
            if (e.getActionCommand().equals("blue")) paintPanel.setColor(Color.BLUE);
            if (e.getActionCommand().equals("yellow")) paintPanel.setColor(Color.YELLOW);
            if (e.getActionCommand().equals("white")) paintPanel.setColor(Color.WHITE);
            if (e.getActionCommand().equals("cyan")) paintPanel.setColor(Color.CYAN);
            if (e.getActionCommand().equals("green")) paintPanel.setColor(Color.GREEN);
            if (e.getActionCommand().equals("magenta")) paintPanel.setColor(Color.MAGENTA);
            if (e.getActionCommand().equals("orange")) paintPanel.setColor(Color.ORANGE);
            if (e.getActionCommand().equals("pink")) paintPanel.setColor(Color.PINK);
            if (e.getActionCommand().equals("lightgray")) paintPanel.setColor(Color.LIGHT_GRAY);
            if (e.getActionCommand().equals("gray")) paintPanel.setColor(Color.GRAY);
            if (e.getActionCommand().equals("darkgray")) paintPanel.setColor(Color.DARK_GRAY);

            //Funktion zum Erstellen eines neuen, weißen Zeichenblatts in der gleichen Größe des aktuellen Blatts
            if (e.getActionCommand().equals("newSameSize")) paintPanel.newPanel();
            //Funktion zum Erstellen eines neuen, weißen Zeichenblatts in neu zu bestimmender Größe
            if (e.getActionCommand().equals("newOtherSize")) {
                //Abfrage der gewünschten Größe der Zeichenfläche
                int width = parseInt(JOptionPane.showInputDialog("Breite der Zeichenfläche in Pixeln"));
                int height = parseInt(JOptionPane.showInputDialog("Höhe der Zeichenfläche in Pixeln"));
                paintPanel.newPanel(width, height);
            }
            //Funktion zum Beenden des Programms
            if (e.getActionCommand().equals("close")) System.exit(0);
            //Funktion zum Speichern des Bildes
            if (e.getActionCommand().equals("save")) {
                fileChooser.showSaveDialog(null);
                File outputFile;
                //es wird an einen Dateinamen nur die Endung .jpg angehängt, wenn diese noch nicht vorhanden ist
                if (String.valueOf(fileChooser.getSelectedFile()).endsWith(".jpg")) outputFile = new File (String.valueOf(fileChooser.getSelectedFile()));
                else outputFile = new File (fileChooser.getSelectedFile() + ".jpg");
                paintPanel.save(outputFile);
            }
            //Funktion zum Laden eines Bildes
            if (e.getActionCommand().equals("load")) {
                fileChooser.showOpenDialog(paintPanel);
                File inputFile = new File (String.valueOf(fileChooser.getSelectedFile()));
                paintPanel.load(inputFile);
            }
        }
    }

    //Klasse zum Ausführen von Befehlen nach Mausklick
    class MouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            //Setzen der aktuellen Mausposition an der Stelle, wo die Maustaste gedrückt wurde
            paintPanel.setLastMousePosition(e.getPoint());
            //Beim Pinsel(Radierer) kann man auch nur mit einem Mausklick zeichnen
            if (paintPanel.getTool().equals(brushString)) paintPanel.brush(e.getPoint());
            if (paintPanel.getTool().equals(eraserString)) paintPanel.erase(e.getPoint());
        }

        //Klasse zum Ausführen von Befehlen beim Loslassen der Maus
        @Override
        public void mouseReleased(MouseEvent e) {
            //Linien, Rechtecke und Ellipsen werden beim Loslassen der Maus zusammen mit der Mausposition beim Klicken der Maus gezeichnet
            if (paintPanel.getTool().equals(lineString)) paintPanel.line(e.getPoint());
            if (paintPanel.getTool().equals(rectangleString)) paintPanel.rectangle(e.getPoint());
            if (paintPanel.getTool().equals(ellipseString)) paintPanel.ellipse(e.getPoint());
        }
    }

    //Klasse zum Ausführen von Befehlen beim Ziehen der Maus
    class MouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            //Pinsel(Radierer) arbeiten beim Ziehen der Maus
            if (paintPanel.getTool().equals(brushString)) paintPanel.brush(e.getPoint());
            if (paintPanel.getTool().equals(eraserString)) paintPanel.erase(e.getPoint());
        }
    }
}