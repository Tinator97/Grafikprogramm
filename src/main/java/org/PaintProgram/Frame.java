package org.PaintProgram;

//importieren der benötigten Klassen
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.awt.event.KeyEvent.*;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

//Klasse, die das Anwendungsfenster darstellt
public class Frame extends JFrame {
    //Klassenvariablen
    private final PaintPanel paintPanel;
    private final JFileChooser fileChooser;
    private JToolBar toolBar;
    private JPanel colorPanel;
    private JTextField strokeField;
    private ButtonGroup colorGroup, toolGroup;
    private JToggleButton brushButton, lineButton, rectangleButton, ellipseButton, eraserButton, blackButton, redButton, blueButton, yellowButton, whiteButton, cyanButton, greenButton, magentaButton, orangeButton, pinkButton, lightgrayButton, grayButton, darkgrayButton;
    private boolean leftMouseButtonIsPressed;
    private File outputFile;
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
    //Für die Items werden gesetzt: Text, Shortcut, Icon, ActionCommand; ActionListener werden erstellt
    //mit F10 kann die MenuBar auch mit der Tastatur gesteuert werden
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");
        menuBar.add(fileMenu);
        JMenu toolMenu = new JMenu("Werkzeuge");
        menuBar.add(toolMenu);

        JMenuItem newSameSizeItem = new JMenuItem("Neu");
        newSameSizeItem.setIcon(new ImageIcon("icons/menu/Add16.gif"));
        newSameSizeItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newSameSizeItem);
        newSameSizeItem.setActionCommand("newSameSize");
        newSameSizeItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem newOtherSizeItem = new JMenuItem("Neu (Blattgroeße anpassen)");
        newOtherSizeItem.setIcon(new ImageIcon("icons/menu/Add16.gif"));
        newOtherSizeItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(newOtherSizeItem);
        newOtherSizeItem.setActionCommand("newOtherSize");
        newOtherSizeItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem loadItem = new JMenuItem("Laden");
        loadItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(loadItem);
        loadItem.setActionCommand("load");
        loadItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem saveItem = new JMenuItem("Speichern");
        saveItem.setIcon(new ImageIcon("icons/menu/save16.gif"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveItem);
        saveItem.setActionCommand("save");
        saveItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem saveAsItem = new JMenuItem("Speichern unter");
        saveAsItem.setIcon(new ImageIcon("icons/menu/save16.gif"));
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(saveAsItem);
        saveAsItem.setActionCommand("saveAs");
        saveAsItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem closeItem = new JMenuItem("Beenden");
        fileMenu.add(closeItem);
        closeItem.setActionCommand("close");
        closeItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem brushItem = new JMenuItem("Pinsel");
        brushItem.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.ALT_DOWN_MASK));
        toolMenu.add(brushItem);
        brushItem.setActionCommand(brushString);
        brushItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem lineItem = new JMenuItem("Linie");
        lineItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.ALT_DOWN_MASK));
        toolMenu.add(lineItem);
        lineItem.setActionCommand(lineString);
        lineItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem rectangleItem = new JMenuItem("Viereck");
        rectangleItem.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.ALT_DOWN_MASK));
        toolMenu.add(rectangleItem);
        rectangleItem.setActionCommand(rectangleString);
        rectangleItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem ellipseItem = new JMenuItem("Ellipse");
        ellipseItem.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.ALT_DOWN_MASK));
        toolMenu.add(ellipseItem);
        ellipseItem.setActionCommand(ellipseString);
        ellipseItem.addActionListener(new ButtonAndTextFieldListener());

        JMenuItem eraserItem = new JMenuItem("Radierer");
        //Alt+X wurde gewählt, weil Alt+R von Windows abgefangen wird und nicht funktioniert
        eraserItem.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.ALT_DOWN_MASK));
        toolMenu.add(eraserItem);
        eraserItem.setActionCommand(eraserString);
        eraserItem.addActionListener(new ButtonAndTextFieldListener());

        setJMenuBar(menuBar);
    }

    // Erstellen der SymbolBar mit den einzelnen Buttons für Werkzeuge inkl. Symbolen und Hotkeys
    private void createSymbolBar() {
        toolBar = new JToolBar();
        //Erzeugen der Buttons für die Tools. Hotkeys orientieren sich an den deutschen Begriffen. Abstandhalter vor und nach den Tools
        //ButtonGroup erstellt, um dem Anwender das ausgewählte Tool anzuzeigen
        toolBar.addSeparator(new Dimension(20,100));
        toolGroup = new ButtonGroup();
        brushButton = createButton("icons/tools/brush.png", VK_P, brushString, "Pinsel (Alt+P)", false, true);
        lineButton = createButton("icons/tools/linie.gif", VK_L, lineString, "Linie (Alt+L)", false, false);
        rectangleButton = createButton("icons/tools/rechteck.gif", VK_V, rectangleString, "Viereck (Alt+V)", false, false);
        ellipseButton = createButton("icons/tools/ellipse.gif", VK_E, ellipseString, "Ellipse (Alt+E)", false, false);
        eraserButton = createButton("icons/tools/eraser.png", VK_X, eraserString, "Radierer (Alt+X)", false, false);
        toolBar.addSeparator(new Dimension(20,100));

        //Bereich für die Strichstärke, Erzeugen eines neuen Panels mit BorderLayout
        JPanel strokePanel = new JPanel();
        strokePanel.setLayout(new GridLayout(0,1, 0, 2));
        //Festlegen der Größe des Panels
        strokePanel.setMaximumSize(new Dimension(50,90));
        //Erstellen der Überschrift
        JLabel strokeLabel1 = new JLabel("Strich-");
        strokeLabel1.setHorizontalAlignment(JLabel.CENTER);
        JLabel strokeLabel2 = new JLabel("breite");
        strokeLabel2.setHorizontalAlignment(JLabel.CENTER);
        //Erstellen des Textfeldes zum Eingeben der Strichstärke
        strokeField = new JTextField("5,0");
        strokeField.setToolTipText("Strichbreite in Pixeln (Alt+D");
        strokeField.setHorizontalAlignment(JTextField.CENTER);
        //Hinzufügen des ActionCommand und des ActionListener
        strokeField.setActionCommand("stroke");
        strokeField.addActionListener(new ButtonAndTextFieldListener());
        //Standardmäßig ist das Textfeld nicht fokussierbar, um den Cursor im Feld nicht anzuzeigen und die Bearbeitung aus Versehen zu verhindern
        strokeField.setFocusable(false);
        //Anonyme Klasse zum Fokussieren des Textfeldes beim Klick auf das Textfeld
        strokeField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                strokeField.setFocusable(true);
                strokeField.grabFocus();
            }
        });
        //Keybinding um das Textfeld mit der Tastatur aktivieren zu können
        InputMap inputMap = strokeField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = strokeField.getActionMap();
        KeyStroke altD = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK);
        inputMap.put(altD, "focusStrokeField");
        actionMap.put("focusStrokeField", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeField.setFocusable(true);
                strokeField.grabFocus();
            }
        });
        //Bestätigungsbutton, um Strichdicke mit der Maus bestätigen zu können
        JButton confirmStroke = new JButton("OK");
        confirmStroke.setActionCommand("stroke");
        confirmStroke.addActionListener(new ButtonAndTextFieldListener());
        confirmStroke.setFont(new Font("Arial", Font.BOLD, 11));
        confirmStroke.setFocusable(false);
        //Hinzufügen zur Toolbar und Abstandshalter
        strokePanel.add(strokeLabel1);
        strokePanel.add(strokeLabel2);
        strokePanel.add(strokeField);
        strokePanel.add(confirmStroke);
        toolBar.add(strokePanel);
        toolBar.addSeparator(new Dimension(20,100));

        //Bereich für die Farbauswahl
        //ButtonGroup erstellt, um dem Anwender die ausgewählte Farbe anzuzeigen, Erstellen der Buttons
        colorGroup = new ButtonGroup();
        colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(3,5));
        colorPanel.setMaximumSize(new Dimension(180,150));
        blackButton = createButton("icons/colors/black.jpg", VK_1, "black", "schwarz (Alt+1)", true, true);
        redButton = createButton("icons/colors/red.jpg", VK_2, "red", "rot (Alt+2)", true, false);
        blueButton = createButton("icons/colors/blue.jpg", VK_3, "blue", "blau (Alt+3)", true, false);
        yellowButton = createButton("icons/colors/yellow.jpg", VK_4, "yellow", "gelb (Alt+4)", true, false);
        whiteButton = createButton("icons/colors/white.jpg", VK_5, "white", "weiß (Alt+5)", true, false);
        cyanButton = createButton("icons/colors/cyan.jpg", VK_6, "cyan", "cyan (Alt+6)", true, false);
        greenButton = createButton("icons/colors/green.jpg", VK_7, "green", "grün (Alt+7)", true, false);
        magentaButton= createButton("icons/colors/magenta.jpg", VK_8, "magenta", "magenta (Alt+8)", true, false);
        orangeButton= createButton("icons/colors/orange.jpg", VK_9, "orange", "orange (Alt+9)", true, false);
        pinkButton = createButton("icons/colors/pink.jpg", VK_0, "pink", "pink (Alt+0)", true, false);
        lightgrayButton = createButton("icons/colors/lightgray.jpg", VK_NUMPAD1, "lightgray", "hellgrau (Alt+NUMPAD1)", true, false);
        grayButton = createButton("icons/colors/gray.jpg", VK_NUMPAD2, "gray", "grau (Alt+NUMPAD2)", true, false);
        darkgrayButton = createButton("icons/colors/darkgray.jpg", VK_NUMPAD3, "darkgray", "dunkelgrau (Alt+NUMPAD3)", true, false);
        toolBar.add(colorPanel);

        this.add(toolBar, BorderLayout.NORTH);
    }

    //Eigene Methode um Buttons vollständig zu implementieren
    //jeder Button kann bekommen: Icon, Hotkey, Tooltip, ActionCommand, ActionListener
    //Button sind nicht fokussierbar, um das Selektieren (das Hervorheben) mit den Pfeiltasten zu verhindern.
    //setSelected wird verwendet, um die Standardtools zu Beginn auszuwählen
    private JToggleButton createButton(String imageIconFilename, int mnemonic, String actionCommand, String tooltip, boolean isColor, boolean select) {
        JToggleButton button = new JToggleButton();
        button.setIcon(new ImageIcon(imageIconFilename));
        button.setMnemonic(mnemonic);
        button.setToolTipText(tooltip);
        //Wenn der Button eine Farbe ist, kommt er in das entsprechende Panel, ansonsten direkt zur Toolbar.
        if (isColor) {
            colorGroup.add(button);
            colorPanel.add(button);
        }
        else {
            toolGroup.add(button);
            toolBar.add(button);
        }
        button.setFocusable(false);
        button.setSelected(select);
        button.setActionCommand(actionCommand);
        button.addActionListener(new ButtonAndTextFieldListener());
        return button;
    }

    //Klasse zum Ausführen von Befehlen nach Knopfdruck bzw. beim Bearbeiten von Textfeldern
    class ButtonAndTextFieldListener implements ActionListener {
        //Klassenvariable für das Standardverzeichnis für gespeicherte Bilder
        Path savedPictures = Path.of("savedPictures");

        @Override
        public void actionPerformed(ActionEvent e) {
            //Funktion, um beim Zurückwechseln vom Radierer wieder die ursprüngliche Farbe zugeordnet zu bekommen
            if (paintPanel.getTool().equals(eraserString) && (e.getActionCommand().equals(brushString) || e.getActionCommand().equals(lineString) ||
                    e.getActionCommand().equals(rectangleString) || e.getActionCommand().equals(ellipseString))) {
                Color lastColor = paintPanel.getLastColor();
                paintPanel.setColor(lastColor);
                //Anzeige der Farbauswahl nach dem Radieren
                if (lastColor == Color.BLACK) blackButton.setSelected(true);
                if (lastColor == Color.RED) redButton.setSelected(true);
                if (lastColor == Color.BLUE) blueButton.setSelected(true);
                if (lastColor == Color.YELLOW) yellowButton.setSelected(true);
                if (lastColor == Color.WHITE) whiteButton.setSelected(true);
                if (lastColor == Color.CYAN) cyanButton.setSelected(true);
                if (lastColor == Color.GREEN) greenButton.setSelected(true);
                if (lastColor == Color.MAGENTA) magentaButton.setSelected(true);
                if (lastColor == Color.ORANGE) orangeButton.setSelected(true);
                if (lastColor == Color.PINK) pinkButton.setSelected(true);
                if (lastColor == Color.LIGHT_GRAY) lightgrayButton.setSelected(true);
                if (lastColor == Color.GRAY) grayButton.setSelected(true);
                if (lastColor == Color.DARK_GRAY) darkgrayButton.setSelected(true);
            }

            //Funktionen, um das Werkzeug auszuwählen
            if (e.getActionCommand().equals(brushString)) {
                paintPanel.setTool(brushString);
                brushButton.setSelected(true);
            }
            if (e.getActionCommand().equals(lineString)) {
                paintPanel.setTool(lineString);
                lineButton.setSelected(true);
            }
            if (e.getActionCommand().equals(rectangleString)) {
                paintPanel.setTool(rectangleString);
                rectangleButton.setSelected(true);
            }
            if (e.getActionCommand().equals(ellipseString)) {
                paintPanel.setTool(ellipseString);
                ellipseButton.setSelected(true);
            }
            //Radierer darf nur gewählt werden, wenn er noch nicht ausgewählt ist, sonst wird die vorher gewählte Farbe mit weiß überschrieben
            if (e.getActionCommand().equals(eraserString) && !paintPanel.getTool().equals(eraserString)) {
                eraserButton.setSelected(true);
                //Speichern der aktuellen Farbe
                paintPanel.setLastColor(paintPanel.getColor());
                //Setzen der aktuellen Farbe auf Weiß, da der Radierer eigentlich ein weißer Pinsel ist
                paintPanel.setColor(Color.WHITE);
                paintPanel.setTool(eraserString);
                //Aufheben der Farbauswahl beim Radieren
                colorGroup.clearSelection();
            }

            //Funktion zum Setzen der Strichstärke
             if (e.getActionCommand().equals("stroke")) {
                String strokeStringWithPoint;
                String strokeStringWithKomma;
                 try {
                    //Einlesen der Strichstärke
                    strokeStringWithKomma = strokeField.getText();
                    //Überprüfung, ob Strichstärke eine Zahl ist
                    if (!strokeStringWithKomma.matches("^-?(?:\\d+,?\\d*|\\.\\d+)$")) throw new IOException("Ungültige Eingabe: Es muss eine Zahl eingegeben werden!");
                    //tauschen von eingegebenen Komma zu Punkt
                    strokeStringWithPoint = strokeStringWithKomma.replace(',', '.');
                    float stroke = parseFloat(strokeStringWithPoint);
                    //Überprüfung, ob Strichstärke größer als null ist
                    if (stroke <= 0) throw new IOException("Ungültige Eingabe: Der Wert muss größer als Null sein!");
                    //Ändern der Strichstärke
                    paintPanel.setStroke(stroke);
                    //Neuerzeugen des Textes, damit konstant wenigstens eine Nachkommastelle angezeigt wird z. B. Eingabe 8 und Anzeige 8,0
                    strokeStringWithPoint = "" + paintPanel.getStroke();
                    strokeStringWithKomma = strokeStringWithPoint.replace("." , ",");
                    strokeField.setText(strokeStringWithKomma);
                } catch (IOException ex) {
                    //zurücksetzen des Textfeldes auf ursprüngliche Strichdicke
                     strokeStringWithPoint = "" + paintPanel.getStroke();
                     strokeStringWithKomma = strokeStringWithPoint.replace("." , ",");
                     strokeField.setText(strokeStringWithKomma);
                }
                 //Fokussierbarkeit des Textfeldes entfernen, damit nicht aus Versehen weitergeschrieben wird
                 strokeField.setFocusable(false);
            }

            //Funktion zum Setzen der Farbe, nur wenn ein anderes Tool als der Radierer ausgewählt ist, ansonsten Aufheben der Auswahl
            if (!paintPanel.getTool().equals("eraser")){
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
            } else colorGroup.clearSelection();

            //Funktion zum Erstellen eines neuen, weißen Zeichenblatts in der gleichen Größe des aktuellen Blatts mit Bestätigungsdialog
            if (e.getActionCommand().equals("newSameSize")) {
                int confirmation = JOptionPane.showConfirmDialog(paintPanel, "Wollen Sie wirklich ein neues Blatt erstellen? Ungespeicherter Fortschritt geht verloren.", "Neues Blatt", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    paintPanel.newPanel();
                    //Zurücksetzen der Buttons und des Textfeldes
                    strokeField.setText("5,0");
                    brushButton.setSelected(true);
                    blackButton.setSelected(true);
                    //zurücksetzen des Speicherpfades
                    outputFile = null;
                }
            }
            //Funktion zum Erstellen eines neuen, weißen Zeichenblatts in neu zu bestimmender Größe mit Bestätigungsdialog
            if (e.getActionCommand().equals("newOtherSize")) {
                int confirmation = JOptionPane.showConfirmDialog(paintPanel, "Wollen Sie wirklich ein neues Blatt erstellen? Ungespeicherter Fortschritt geht verloren.", "Neues Blatt", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        //Abfrage der gewünschten Größe der Zeichenfläche
                        String widthString = JOptionPane.showInputDialog("Breite der Zeichenfläche in Pixeln");
                        String heightString = JOptionPane.showInputDialog("Höhe der Zeichenfläche in Pixeln");
                        //Überprüfung, ob Eingabe eine Ganzzahl ist
                        if (!widthString.matches("^-?\\d+$") || !heightString.matches("^-?\\d+$")) throw new IOException("Ungültige Eingabe: Es müssen ganze Zahlen eingegeben werden!");
                        int width = parseInt(widthString);
                        int height = parseInt(heightString);
                        //Überprüfung, ob Eingaben positiv sind
                        if (width <= 0 || height <= 0) throw new IOException("Ungültige Eingabe: Die Werte müssen größer als Null sein!");
                        //Erstellen der neuen Zeichenfläche
                        paintPanel.newPanel(width, height);
                        //Zurücksetzen der Buttons und des Textfeldes
                        strokeField.setText("5,0");
                        brushButton.setSelected(true);
                        blackButton.setSelected(true);
                        //zurücksetzen des Speicherpfades
                        outputFile = null;
                    } catch (IOException ex) {
                        //Fehlermeldung, bei falscher Eingabe
                        JOptionPane.showMessageDialog(paintPanel, ex.getMessage());
                    }
                }
            }
            //Funktion zum Beenden des Programms mit Bestätigungsdialog
            if (e.getActionCommand().equals("close")) {
                int confirmation = JOptionPane.showConfirmDialog(paintPanel, "Wollen Sie die Anwendung wirklich beenden? Ungespeicherter Fortschritt geht verloren.", "Beenden", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) System.exit(0);
            }
            //Funktion zum Speichern des Bildes. Nur wenn noch keine Datei vorhanden ist, wird der Speichern-Dialog aufgerufen
            if (e.getActionCommand().equals("save")) {
                //Dialog wird nur angezeigt, wenn noch keine Datei vorhanden ist
                if (outputFile == null) {
                    //Abfrage, ob wirklich gespeichert werden soll
                    if (fileChooser.showSaveDialog(paintPanel) == JFileChooser.APPROVE_OPTION) {
                        //Bei Bedarf erstellen des Standardverzeichnisses für gespeicherte Bilder
                        try {
                            if (!Files.exists(savedPictures)) Files.createDirectory(savedPictures);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(paintPanel, "Standardverzeichnis für gespeicherte Bilder konnte nicht erstellt werden!");
                        }
                        //es wird an einen Dateinamen nur die Endung .jpg angehängt, wenn diese noch nicht vorhanden ist
                        if (String.valueOf(fileChooser.getSelectedFile()).endsWith(".jpg")) outputFile = new File (String.valueOf(fileChooser.getSelectedFile()));
                        else outputFile = new File (fileChooser.getSelectedFile() + ".jpg");
                    }
                }
                if (outputFile != null) paintPanel.save(outputFile);
            }
            //Funktion zum Speichern des Bildes. Immer mit Speichern-Dialog
            if (e.getActionCommand().equals("saveAs")) {
                if (fileChooser.showSaveDialog(paintPanel) == JFileChooser.APPROVE_OPTION) {
                    //Bei Bedarf erstellen des Standardverzeichnisses für gespeicherte Bilder
                    try {
                        if (!Files.exists(savedPictures)) Files.createDirectory(savedPictures);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(paintPanel, "Standardverzeichnis für gespeicherte Bilder konnte nicht erstellt werden!");
                    }
                    //es wird an einen Dateinamen nur die Endung .jpg angehängt, wenn diese noch nicht vorhanden ist
                    if (String.valueOf(fileChooser.getSelectedFile()).endsWith(".jpg")) outputFile = new File (String.valueOf(fileChooser.getSelectedFile()));
                    else outputFile = new File (fileChooser.getSelectedFile() + ".jpg");
                    paintPanel.save(outputFile);
                }
            }
            //Funktion zum Laden eines Bildes mit Bestätigungsdialog
            if (e.getActionCommand().equals("load")) {
                int confirmation = JOptionPane.showConfirmDialog(paintPanel, "Wollen Sie wirklich ein neues Bild laden? Ungespeicherter Fortschritt geht verloren.", "Laden", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    fileChooser.showOpenDialog(paintPanel);
                    File inputFile = new File (String.valueOf(fileChooser.getSelectedFile()));
                    paintPanel.load(inputFile);
                    //anpassen des Speicherpfades
                    outputFile = inputFile;
                }
            }
        }
    }

    //Klasse zum Ausführen von Befehlen nach Mausklick
    class MouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            //Filter nach Aktionen mit der linken Maustaste
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftMouseButtonIsPressed = true;
                //Speichern der Mausposition beim Mausdruck in der Variable lastMousePosition
                paintPanel.setLastMousePosition(e.getPoint());
                //Zurücksetzen der Endposition der Figur vor dem Ziehen, um ungewolltes Verhalten der Vorschau zu vermeiden
                paintPanel.setEndPointOfShape(e.getPoint());
                //Beim Pinsel(Radierer) kann man auch nur mit einem Mausklick zeichnen
                if (paintPanel.getTool().equals(brushString)) paintPanel.brush(e.getPoint());
                if (paintPanel.getTool().equals(eraserString)) paintPanel.erase(e.getPoint());
            }
        }

        //Klasse zum Ausführen von Befehlen beim Loslassen der Maus
        @Override
        public void mouseReleased(MouseEvent e) {
            //Filter nach Aktionen mit der linken Maustaste
            if (e.getButton() == MouseEvent.BUTTON1) {
                //Linien, Rechtecke und Ellipsen werden beim Loslassen der Maus zusammen mit der Mausposition beim Klicken der Maus gezeichnet
                if (paintPanel.getTool().equals(lineString)) paintPanel.line(e.getPoint());
                if (paintPanel.getTool().equals(rectangleString)) paintPanel.rectangle(e.getPoint());
                if (paintPanel.getTool().equals(ellipseString)) paintPanel.ellipse(e.getPoint());
                leftMouseButtonIsPressed = false;
            }
        }
    }

    //Klasse zum Ausführen von Befehlen beim Ziehen der Maus
    class MouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            //Filter nach Aktionen mit der linken Maustaste
            if (leftMouseButtonIsPressed) {
                //Pinsel(Radierer) arbeiten beim Ziehen der Maus
                if (paintPanel.getTool().equals(brushString)) paintPanel.brush(e.getPoint());
                if (paintPanel.getTool().equals(eraserString)) paintPanel.erase(e.getPoint());
                //Positionsübergabe der Endposition von Linie, Rechteck und Ellipse für die Vorschau und Aktualisierung der Vorschau beim Ziehen
                if (paintPanel.getTool().equals(lineString) || paintPanel.getTool().equals(rectangleString) || paintPanel.getTool().equals(ellipseString) ) {
                    paintPanel.setEndPointOfShape(e.getPoint());
                    paintPanel.repaint();
                }
            }
        }
    }
}