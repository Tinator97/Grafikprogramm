package org.Grafikprogramm;

//importieren der benötigten Klassen
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Fenster extends JFrame {
    //Klassenvariablen
        private Panel panel;
        private JMenuBar menuBar;
        private JMenu datei, bearbeiten;
        private JMenuItem neu, laden, speichern, beenden, rueckgaengig;
        private JToolBar toolBar;
        private JButton linie, rechteck, ellipse, radierer;
        private JLabel labelWerkzeuge, labelFarben;

    //Konstruktor
    public Fenster (String fenstertitel) {
        //Erstellen des Fensters
        super(fenstertitel);

        //Größe des Fensters - Ermitteln der Auflösung des Betriebssystems
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int breite = graphicsDevice.getDisplayMode().getWidth();
        int hoehe = graphicsDevice.getDisplayMode().getHeight() - 50;
        this.setSize(breite, hoehe);

        //Layout des Fensters
        //Fenstergröße fixieren
        //Beenden des Programms beim Schließen
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Hinzufügen des Panels - der Zeichenfläche
        panel = new Panel();
        this.add(panel, BorderLayout.CENTER);

        //Erstellen weiterer UI-Elemente
        erstelleMenuBar();
        erstelleSymbolBar();

        //Erstellen der Listener und ActionCommands
        erstelleActions();

        //Sichtbarkeit des Fensters
        this.setVisible(true);
    }

    // Erstellen der MenuBar mit den einzelnen Untermenüs und Items inkl. Symbolen und ShortCuts
    // Mit F10 kann die MenuBar auch mit der Tastatur gesteuert werden
    private void erstelleMenuBar() {
        menuBar = new JMenuBar();
        datei = new JMenu("Datei");
        bearbeiten = new JMenu("Bearbeiten");
        neu = new JMenuItem("Neu");
        laden = new JMenuItem("Laden");
        speichern = new JMenuItem("Speichern");
        beenden = new JMenuItem("Beenden");
        rueckgaengig = new JMenuItem("Rückgängig");

        neu.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/Add16.gif"));
        //laden.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/"));
        speichern.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/save16.gif"));
        //beenden.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/"));
        rueckgaengig.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/redo16.gif"));

        neu.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        laden.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        speichern.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        //beenden.setAccelerator(KeyStroke.getKeyStroke('', InputEvent.CTRL_DOWN_MASK));
        rueckgaengig.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        datei.setMnemonic('D');
        bearbeiten.setMnemonic('B');

        datei.add(neu);
        datei.add(laden);
        datei.add(speichern);
        datei.add(beenden);
        bearbeiten.add(rueckgaengig);

        menuBar.add(datei);
        menuBar.add(bearbeiten);

        setJMenuBar(menuBar);
    }

    // Erstellen der SymbolBar mit den einzelnen Buttons für Werkzeuge inkl. Symbolen und ShortCuts
    private void erstelleSymbolBar() {
        toolBar = new JToolBar();
        labelWerkzeuge = new JLabel("Werkzeuge");
        linie = new JButton();
        rechteck = new JButton();
        ellipse = new JButton();
        radierer = new JButton();
        labelFarben = new JLabel("Farben");

        linie.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/linie64.gif"));
        rechteck.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/rechteck64.gif"));
        ellipse.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/ellipse64.gif"));
        radierer.setIcon(new ImageIcon("bilder/toolbarButtonGraphics/general/linie64.gif"));

        linie.setMnemonic('L');
        rechteck.setMnemonic('R');
        ellipse.setMnemonic('C');
        radierer.setMnemonic('E');

        toolBar.add(labelWerkzeuge);
        toolBar.add(linie);
        toolBar.add(rechteck);
        toolBar.add(ellipse);
        toolBar.add(radierer);
        toolBar.add(labelFarben);

        this.add(toolBar, BorderLayout.NORTH);
    }

    //Setzen von ActionCommands und Listenern
    private void erstelleActions() {
        neu.setActionCommand("neu");
        laden.setActionCommand("laden");
        speichern.setActionCommand("speichern");
        beenden.setActionCommand("beenden");
        linie.setActionCommand("linie");
        rechteck.setActionCommand("rechteck");
        ellipse.setActionCommand("ellipse");
        radierer.setActionCommand("radierer");

        neu.addActionListener(new SchaltflaecheListener());
        laden.addActionListener(new SchaltflaecheListener());
        speichern.addActionListener(new SchaltflaecheListener());
        beenden.addActionListener(new SchaltflaecheListener());
        linie.addActionListener(new SchaltflaecheListener());
        rechteck.addActionListener(new SchaltflaecheListener());
        ellipse.addActionListener(new SchaltflaecheListener());
        radierer.addActionListener(new SchaltflaecheListener());
    }

    //Klasse zum Ausführen von Befehlen nach bestimmten Ereignissen
    class SchaltflaecheListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("beenden")) System.exit(0);
        }
    }
}
