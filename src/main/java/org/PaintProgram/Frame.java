package org.PaintProgram;

//importieren der benötigten Klassen
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import static java.lang.Integer.parseInt;

public class Frame extends JFrame {
    //Klassenvariablen
        private PaintPanel paintPanel;
        private JMenuBar menuBar;
        private JMenu fileMenu, editMenu;
        private JMenuItem newItem, loadItem, saveItem, closeItem, redoItem;
        private JToolBar toolBar;
        private JButton brushButton, lineButton, rectangleButton, ellipseButton, eraserButton, blackButton, redButton, blueButton;
        private JLabel toolsLabel, colorsLabel, strokeLabel;
        private JTextField strokeField;
        private Color lastColor;
        private JFileChooser fileChooser;


    private final String brushString = "brush", lineString = "line", rectangleString = "rectangle", ellipseString = "ellipse", eraserString = "eraser";


    //Konstruktor
    public Frame (String frameTitel) {
        //Erstellen des Fensters
        super(frameTitel);

        //Größe des Fensters - Ermitteln der Auflösung des Betriebssystems
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.setSize(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight() - 50);

        //Layout des Fensters
        //Fenstergröße fixieren
        //Beenden des Programms beim Schließen
        this.setLayout(new BorderLayout());
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Hinzufügen des Panels - der Zeichenfläche und erstellen der MouseListener
        createPanel();

        //Erstellen Menübar und Symbolleiste inkl. interaktiver Elemente
        createMenuBar();
        createSymbolBar();

        //Sichtbarkeit des Fensters
        this.setVisible(true);

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPG","jpg"));
        fileChooser.setCurrentDirectory(new File ("EigeneBilder"));
    }

    // Erstellen der MenuBar mit den einzelnen Untermenüs und Items inkl. Symbolen und ShortCuts
    // Mit F10 kann die MenuBar auch mit der Tastatur gesteuert werden
    private void createMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("Datei");
        fileMenu.setMnemonic('D');
        menuBar.add(fileMenu);

        editMenu = new JMenu("Bearbeiten");
        editMenu.setMnemonic('B');
        menuBar.add(editMenu);

        newItem = new JMenuItem("Neu");
        newItem.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/Add16.gif"));
        newItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newItem);
        newItem.setActionCommand("new");
        newItem.addActionListener(new ButtonListener());

        loadItem = new JMenuItem("Laden");
        loadItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(loadItem);
        loadItem.setActionCommand("load");
        loadItem.addActionListener(new ButtonListener());

        saveItem = new JMenuItem("Speichern");
        saveItem.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/save16.gif"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveItem);
        saveItem.setActionCommand("save");
        saveItem.addActionListener(new ButtonListener());

        closeItem = new JMenuItem("Beenden");
        fileMenu.add(closeItem);
        closeItem.setActionCommand("close");
        closeItem.addActionListener(new ButtonListener());

        redoItem = new JMenuItem("Rückgängig");
        redoItem.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/redo16.gif"));
        redoItem.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(redoItem);

        setJMenuBar(menuBar);
    }

    // Erstellen der SymbolBar mit den einzelnen Buttons für Werkzeuge inkl. Symbolen und ShortCuts
    private void createSymbolBar() {
        toolBar = new JToolBar();

        toolsLabel = new JLabel("Werkzeuge");
        toolBar.add(toolsLabel);

        newButtonForSymbolbar(brushButton, "bilder/toolbarButtonGraphics/development/Bean24.gif", '0', brushString);
        newButtonForSymbolbar(lineButton, "bilder/toolbarButtonGraphics/general/linie64.gif", '0', lineString);
        newButtonForSymbolbar(rectangleButton, "bilder/toolbarButtonGraphics/general/rechteck64.gif", '0', rectangleString);
        newButtonForSymbolbar(ellipseButton, "bilder/toolbarButtonGraphics/general/ellipse64.gif", '0', ellipseString);
        newButtonForSymbolbar(eraserButton, "bilder/toolbarButtonGraphics/development/Bean24.gif", '0', eraserString);

        strokeLabel = new JLabel("Strichdicke");
        toolBar.add(strokeLabel);

        strokeField = new JTextField("3");
        strokeField.setMaximumSize(new Dimension(40,30));
        toolBar.add(strokeField);
        strokeField.setActionCommand("stroke");
        strokeField.addActionListener(new ButtonListener());

        colorsLabel = new JLabel("Farben");
        toolBar.add(colorsLabel);

        newButtonForSymbolbar(blackButton, "bilder/toolbarButtonGraphics/development/Bean24.gif", '0', "black");
        newButtonForSymbolbar(redButton, "bilder/toolbarButtonGraphics/development/Bean24.gif", '0', "red");
        newButtonForSymbolbar(blueButton, "bilder/toolbarButtonGraphics/development/Bean24.gif", '0', "blue");

        this.add(toolBar, BorderLayout.NORTH);
    }

    private void newButtonForSymbolbar (JButton button, String imageIconFilename, Character mnemonic, String actionCommand) {
        button = new JButton();
        button.setIcon(new ImageIcon(imageIconFilename));
        button.setMnemonic(mnemonic);
        toolBar.add(button);
        button.setActionCommand(actionCommand);
        button.addActionListener(new ButtonListener());
    }

    private void createPanel () {
        int width = parseInt(JOptionPane.showInputDialog("Breite der Zeichenfläche in Pixeln"));
        int height = parseInt(JOptionPane.showInputDialog("Höhe der Zeichenfläche in Pixeln"));
        paintPanel = new PaintPanel(width, height);
        this.add(paintPanel, BorderLayout.CENTER);
        revalidate();
        paintPanel.addMouseListener(new MouseListener());
        paintPanel.addMouseMotionListener(new MouseMotionListener());
    }

    //Klasse zum Ausführen von Befehlen nach Knopdruck
    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (paintPanel.getTool().equals(eraserString) && !e.getActionCommand().equals(eraserString)) paintPanel.setColor(lastColor);

            if (e.getActionCommand().equals(brushString)) paintPanel.setTool(brushString);
            if (e.getActionCommand().equals(lineString)) paintPanel.setTool(lineString);
            if (e.getActionCommand().equals(rectangleString)) paintPanel.setTool(rectangleString);
            if (e.getActionCommand().equals(ellipseString)) paintPanel.setTool(ellipseString);
            if (e.getActionCommand().equals(eraserString) && !paintPanel.getTool().equals(eraserString)) {
                lastColor = paintPanel.getColor();
                paintPanel.setTool(eraserString);
                paintPanel.setColor(Color.WHITE);
            }

            if (e.getActionCommand().equals("stroke")) paintPanel.setStroke(parseInt(strokeField.getText()));
            if (e.getActionCommand().equals("black")) paintPanel.setColor(Color.BLACK);
            if (e.getActionCommand().equals("red")) paintPanel.setColor(Color.RED);
            if (e.getActionCommand().equals("blue")) paintPanel.setColor(Color.BLUE);

            if (e.getActionCommand().equals("new")) createPanel();
            if (e.getActionCommand().equals("close")) System.exit(0);
            if (e.getActionCommand().equals("save")) {
                fileChooser.showSaveDialog(null);
                File outputFile;
                if (String.valueOf(fileChooser.getSelectedFile()).endsWith(".jpg")) outputFile = new File (String.valueOf(fileChooser.getSelectedFile()));
                else outputFile = new File (fileChooser.getSelectedFile() + ".jpg");
                paintPanel.save(outputFile);
            }
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
            paintPanel.setLastMousePosition(e.getPoint());
            if (paintPanel.getTool().equals(brushString)) paintPanel.brush(e.getPoint());
            if (paintPanel.getTool().equals(eraserString)) paintPanel.erase(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (paintPanel.getTool().equals(brushString)) paintPanel.brush(e.getPoint());
            if (paintPanel.getTool().equals(rectangleString)) paintPanel.rectangle(e.getPoint());
            if (paintPanel.getTool().equals(ellipseString)) paintPanel.ellipse(e.getPoint());
            if (paintPanel.getTool().equals(lineString)) paintPanel.line(e.getPoint());
        }
    }

    //Klasse zum Ausführen von Befehlen beim Ziehen der Maus
    class MouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (paintPanel.getTool().equals(brushString)) paintPanel.brush(e.getPoint());
            if (paintPanel.getTool().equals(eraserString)) paintPanel.erase(e.getPoint());
        }
    }
}