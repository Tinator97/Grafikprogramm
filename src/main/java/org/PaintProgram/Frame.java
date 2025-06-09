package org.PaintProgram;

//importieren der benötigten Klassen
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame {
    //Klassenvariablen
        private PaintPanel paintPanel;
        private JMenuBar menuBar;
        private JMenu fileMenu, editMenu;
        private JMenuItem newItem, loadItem, saveItem, closeItem, redoItem;
        private JToolBar toolBar;
        private JButton brushButton, lineButton, rectangleButton, ellipseButton, eraserButton, blackButton, redButton, blueButton;
        private JLabel toolsLabel, colorsLabel;

    //Konstruktor
    public Frame(String frameTitel) {
        //Erstellen des Fensters
        super(frameTitel);

        //Größe des Fensters - Ermitteln der Auflösung des Betriebssystems
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = graphicsDevice.getDisplayMode().getWidth();
        int height = graphicsDevice.getDisplayMode().getHeight() - 50;
        this.setSize(width, height);

        //Layout des Fensters
        //Fenstergröße fixieren
        //Beenden des Programms beim Schließen
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Hinzufügen des Panels - der Zeichenfläche und erstellen der MouseListener
        paintPanel = new PaintPanel(width, height);
        this.add(paintPanel, BorderLayout.CENTER);
        paintPanel.addMouseListener(new MouseListener());
        paintPanel.addMouseMotionListener(new MouseMotionListener());

        //Erstellen weiterer UI-Elemente
        createMenuBar();
        createSymbolBar();

        //Erstellen der Listener und ActionCommands
        createActions();

        //Sichtbarkeit des Fensters
        this.setVisible(true);
    }

    // Erstellen der MenuBar mit den einzelnen Untermenüs und Items inkl. Symbolen und ShortCuts
    // Mit F10 kann die MenuBar auch mit der Tastatur gesteuert werden
    private void createMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Datei");
        editMenu = new JMenu("Bearbeiten");
        newItem = new JMenuItem("Neu");
        loadItem = new JMenuItem("Laden");
        saveItem = new JMenuItem("Speichern");
        closeItem = new JMenuItem("Beenden");
        redoItem = new JMenuItem("Rückgängig");

        newItem.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/Add16.gif"));
        //laden.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/"));
        saveItem.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/save16.gif"));
        //beenden.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/"));
        redoItem.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/redo16.gif"));

        newItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        loadItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        //beenden.setAccelerator(KeyStroke.getKeyStroke('', InputEvent.CTRL_DOWN_MASK));
        redoItem.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        fileMenu.setMnemonic('D');
        editMenu.setMnemonic('B');

        fileMenu.add(newItem);
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(closeItem);
        editMenu.add(redoItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    // Erstellen der SymbolBar mit den einzelnen Buttons für Werkzeuge inkl. Symbolen und ShortCuts
    private void createSymbolBar() {
        toolBar = new JToolBar();
        toolsLabel = new JLabel("Werkzeuge");
        brushButton = new JButton();
        lineButton = new JButton();
        rectangleButton = new JButton();
        ellipseButton = new JButton();
        eraserButton = new JButton();
        colorsLabel = new JLabel("Farben");
        blackButton = new JButton();
        redButton = new JButton();
        blueButton = new JButton();

        brushButton.setText("Pinsel");
        lineButton.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/linie64.gif"));
        rectangleButton.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/rechteck64.gif"));
        ellipseButton.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/ellipse64.gif"));
        eraserButton.setText("Radierer");
        blackButton.setText("schwarz");
        redButton.setText("rot");
        blueButton.setText("blau");

        lineButton.setMnemonic('L');
        rectangleButton.setMnemonic('R');
        ellipseButton.setMnemonic('C');
        eraserButton.setMnemonic('E');

        toolBar.add(toolsLabel);
        toolBar.add(brushButton);
        toolBar.add(lineButton);
        toolBar.add(rectangleButton);
        toolBar.add(ellipseButton);
        toolBar.add(eraserButton);
        toolBar.add(colorsLabel);
        toolBar.add(blackButton);
        toolBar.add(redButton);
        toolBar.add(blueButton);

        this.add(toolBar, BorderLayout.NORTH);
    }

    //Setzen von ActionCommands und Listenern
    private void createActions() {
        newItem.setActionCommand("new");
        loadItem.setActionCommand("load");
        saveItem.setActionCommand("save");
        closeItem.setActionCommand("close");
        brushButton.setActionCommand("brush");
        lineButton.setActionCommand("line");
        rectangleButton.setActionCommand("rect");
        ellipseButton.setActionCommand("ellipse");
        eraserButton.setActionCommand("eraser");
        blackButton.setActionCommand("black");
        redButton.setActionCommand("red");
        blueButton.setActionCommand("blue");

        newItem.addActionListener(new ButtonListener());
        loadItem.addActionListener(new ButtonListener());
        saveItem.addActionListener(new ButtonListener());
        closeItem.addActionListener(new ButtonListener());
        brushButton.addActionListener(new ButtonListener());
        lineButton.addActionListener(new ButtonListener());
        rectangleButton.addActionListener(new ButtonListener());
        ellipseButton.addActionListener(new ButtonListener());
        eraserButton.addActionListener(new ButtonListener());
        blackButton.addActionListener(new ButtonListener());
        redButton.addActionListener(new ButtonListener());
        blueButton.addActionListener(new ButtonListener());
    }

    //Klasse zum Ausführen von Befehlen nach Knopdruck
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("close")) System.exit(0);
            if (e.getActionCommand().equals("eraser")) paintPanel.setColor(Color.WHITE);
            if (e.getActionCommand().equals("black")) paintPanel.setColor(Color.BLACK);
            if (e.getActionCommand().equals("red")) paintPanel.setColor(Color.RED);
            if (e.getActionCommand().equals("blue")) paintPanel.setColor(Color.BLUE);
        }
    }

    //Klasse zum Ausführen von Befehlen nach Mausklick
    class MouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            paintPanel.setLastMousePosition(e.getPoint());
            paintPanel.paint(e.getPoint());
        }
    }

    //Klasse zum Ausführen von Befehlen beim Ziehen der Maus
    class MouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            paintPanel.paint(e.getPoint());
        }
    }
}
