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
    private JMenu fileMenu, toolMenu;
    private JMenuItem newStandardItem, newCustomItem, loadItem, saveItem, closeItem, brushItem, lineItem, rectangleItem, ellipseItem, eraserItem;
    private JToolBar toolBar;
    private JButton brushButton, lineButton, rectangleButton, ellipseButton, eraserButton, blackButton, redButton, blueButton, whiteButton, yellowButton, cyanButton,
            greenButton, magentaButton, orangeButton, pinkButton, lightgrayButton, grayButton, darkgrayButton;
    private JLabel toolsLabel, colorsLabel, strokeLabel;
    private JTextField strokeField;
    private Color lastColor;
    private JFileChooser fileChooser;
    private JScrollPane scrollPane;


    private final String brushString = "brush", lineString = "line", rectangleString = "rectangle", ellipseString = "ellipse", eraserString = "eraser";


    //Konstruktor
    public Frame (String frameTitel) {
        //Erstellen des Fensters
        super(frameTitel);

        //Größe des Fensters - Ermitteln der Auflösung des Betriebssystems
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.setSize(graphicsDevice.getDisplayMode().getWidth()/2, graphicsDevice.getDisplayMode().getHeight()/2);
        this.setExtendedState(MAXIMIZED_BOTH);

        //Layout des Fensters
        //Beenden des Programms beim Schließen
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Hinzufügen des Panels - der Zeichenfläche und erstellen der MouseListener
        paintPanel = new PaintPanel(1600, 900);
        paintPanel.setPreferredSize(new Dimension(1600, 900));
        scrollPane = new JScrollPane(paintPanel);
        this.add(scrollPane, BorderLayout.CENTER);
        paintPanel.addMouseListener(new MouseListener());
        paintPanel.addMouseMotionListener(new MouseMotionListener());

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

        toolMenu = new JMenu("Werkzeuge");
        toolMenu.setMnemonic('B');
        menuBar.add(toolMenu);

        newStandardItem = new JMenuItem("Neu");
        newStandardItem.setIcon(new ImageIcon("icons/toolbarButtonGraphics/general/Add16.gif"));
        newStandardItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newStandardItem);
        newStandardItem.setActionCommand("newStandard");
        newStandardItem.addActionListener(new ButtonListener());

        newCustomItem = new JMenuItem("Neu (Blattgröße anpassen)");
        newCustomItem.setIcon(new ImageIcon("icons/toolbarButtonGraphics/general/Add16.gif"));
        newCustomItem.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newCustomItem);
        newCustomItem.setActionCommand("newCustom");
        newCustomItem.addActionListener(new ButtonListener());

        loadItem = new JMenuItem("Laden");
        loadItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(loadItem);
        loadItem.setActionCommand("load");
        loadItem.addActionListener(new ButtonListener());

        saveItem = new JMenuItem("Speichern");
        saveItem.setIcon(new ImageIcon("icons/toolbarButtonGraphics/general/save16.gif"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveItem);
        saveItem.setActionCommand("save");
        saveItem.addActionListener(new ButtonListener());

        closeItem = new JMenuItem("Beenden");
        fileMenu.add(closeItem);
        closeItem.setActionCommand("close");
        closeItem.addActionListener(new ButtonListener());

        brushItem = new JMenuItem("Pinsel");
        toolMenu.add(brushItem);
        brushItem.setActionCommand(brushString);
        brushItem.addActionListener(new ButtonListener());

        lineItem = new JMenuItem("Linie");
        toolMenu.add(lineItem);
        lineItem.setActionCommand(lineString);
        lineItem.addActionListener(new ButtonListener());

        rectangleItem = new JMenuItem("Rechteck");
        toolMenu.add(rectangleItem);
        rectangleItem.setActionCommand(rectangleString);
        rectangleItem.addActionListener(new ButtonListener());

        ellipseItem = new JMenuItem("Ellipse");
        toolMenu.add(ellipseItem);
        ellipseItem.setActionCommand(ellipseString);
        ellipseItem.addActionListener(new ButtonListener());

        eraserItem = new JMenuItem("Radierer");
        toolMenu.add(eraserItem);
        eraserItem.setActionCommand(eraserString);
        eraserItem.addActionListener(new ButtonListener());

        setJMenuBar(menuBar);
    }

    // Erstellen der SymbolBar mit den einzelnen Buttons für Werkzeuge inkl. Symbolen und ShortCuts
    private void createSymbolBar() {
        toolBar = new JToolBar();

        toolsLabel = new JLabel("Werkzeuge");
        toolBar.add(toolsLabel);

        newButtonForSymbolbar(brushButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', brushString, "Pinsel");
        newButtonForSymbolbar(lineButton, "icons/toolbarButtonGraphics/general/linie64.gif", '0', lineString, "Linie");
        newButtonForSymbolbar(rectangleButton, "icons/toolbarButtonGraphics/general/rechteck64.gif", '0', rectangleString, "Rechteck");
        newButtonForSymbolbar(ellipseButton, "icons/toolbarButtonGraphics/general/ellipse64.gif", '0', ellipseString, "Ellipse");
        newButtonForSymbolbar(eraserButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', eraserString, "Radierer");

        strokeLabel = new JLabel("Strichdicke");
        toolBar.add(strokeLabel);

        strokeField = new JTextField("3");
        strokeField.setMaximumSize(new Dimension(40,30));
        strokeField.setToolTipText("Strichdicke in Pixeln");
        strokeField.setHorizontalAlignment(JTextField.CENTER);
        toolBar.add(strokeField);
        strokeField.setActionCommand("stroke");
        strokeField.addActionListener(new ButtonListener());

        colorsLabel = new JLabel("Farben");
        toolBar.add(colorsLabel);

        newButtonForSymbolbar(blackButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "black", "schwarz");
        newButtonForSymbolbar(redButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "red", "rot");
        newButtonForSymbolbar(blueButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "blue", "blau");
        newButtonForSymbolbar(yellowButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "yellow", "gelb");
        newButtonForSymbolbar(whiteButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "white", "weiß");
        newButtonForSymbolbar(cyanButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "cyan", "cyan");
        newButtonForSymbolbar(greenButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "green", "grün");
        newButtonForSymbolbar(magentaButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "magenta", "magenta");
        newButtonForSymbolbar(orangeButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "orange", "orange");
        newButtonForSymbolbar(pinkButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "pink", "pink");
        newButtonForSymbolbar(lightgrayButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "lightgray", "hellgrau");
        newButtonForSymbolbar(grayButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "gray", "grau");
        newButtonForSymbolbar(darkgrayButton, "icons/toolbarButtonGraphics/development/Bean24.gif", '0', "darkgray", "dunkelgrau");

        this.add(toolBar, BorderLayout.NORTH);
    }

    private void newButtonForSymbolbar (JButton button, String imageIconFilename, Character mnemonic, String actionCommand, String tooltip) {
        button = new JButton();
        button.setIcon(new ImageIcon(imageIconFilename));
        button.setMnemonic(mnemonic);
        button.setToolTipText(tooltip);
        toolBar.add(button);
        button.setActionCommand(actionCommand);
        button.addActionListener(new ButtonListener());
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

            if (e.getActionCommand().equals("newStandard")) paintPanel.reset();;
            if (e.getActionCommand().equals("newCustom")) {
                int width = parseInt(JOptionPane.showInputDialog("Breite der Zeichenfläche in Pixeln"));
                int height = parseInt(JOptionPane.showInputDialog("Höhe der Zeichenfläche in Pixeln"));
                paintPanel.reset(width, height);
            }
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