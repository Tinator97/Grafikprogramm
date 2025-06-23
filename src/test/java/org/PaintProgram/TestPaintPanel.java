package org.PaintProgram;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class TestPaintPanel {
    PaintPanel paintPanel;

    //Initialisieren der Klassen vor dem Testen
    @Before
    public void before() {
        paintPanel = new PaintPanel(1200, 600);
    }

    //Löschen der Zuweisung nach dem Testen
    @After
    public void after() {
        paintPanel = null;
    }

    //Testen der Getter- und Setter-Methode für Tools in einem Test
    @Test
    @Parameters({
            "",
            "brush",
            "line",
            "rectangle",
    })
    public void testSetAndGetTool (String expectedTool) {
        paintPanel.setTool(expectedTool);
        String actualTool = paintPanel.getTool();
        assertEquals(expectedTool, actualTool);
    }

    //Testen der Getter- und Setter-Methode für Farben in einem Test
    @Test
    @Parameters({
            "",
            "yellow",
            "red",
            "blue",
    })
    public void testSetAndGetColor (String colorString) {
        Color expectedColor = Color.BLACK;
        if (colorString.equals("yellow")) expectedColor = Color.YELLOW;
        if (colorString.equals("red")) expectedColor = Color.RED;
        if (colorString.equals("blue")) expectedColor = Color.BLUE;
        paintPanel.setColor(expectedColor);
        Color actualColor = paintPanel.getColor();
        assertEquals(expectedColor, actualColor);
    }

    //Testen der Getter- und Setter-Methode für die letzte gespeicherte Farbe in einem Test
    @Test
    @Parameters({
            "",
            "yellow",
            "red",
            "blue"
    })
    public void testSetAndGetLastColor (String colorString) {
        Color expectedColor = Color.BLACK;
        if (colorString.equals("yellow")) expectedColor = Color.YELLOW;
        if (colorString.equals("red")) expectedColor = Color.RED;
        if (colorString.equals("blue")) expectedColor = Color.BLUE;
        paintPanel.setLastColor(expectedColor);
        Color actualColor = paintPanel.getLastColor();
        assertEquals(expectedColor, actualColor);
    }

    //Testen der Getter- und Setter-Methode für die Strichdicke in einem Test
    @Test
    @Parameters({
            //"",
            //"-5",
            //"0",
            "0.1",
            "1",
            "2",
            "1000",
            "5.5"
    })
    public void testSetAndGetStroke (float expectedStroke) {
        paintPanel.setStroke(expectedStroke);
        float actualStroke = paintPanel.getStroke();
        assertEquals(expectedStroke, actualStroke, 0.0);
    }

    //Testen der Methode checkOrientation, Kommentare zu den einzelnen Tests sind in der Dokumentation
    @Test
    @Parameters({
            "100    ,    100     ,    500    ,   500    ,   100-100-400-400",
            "500    ,    500     ,    100    ,   100    ,   100-100-400-400",
            "100    ,    500     ,    500    ,   100    ,   100-100-400-400",
            "500    ,    100     ,    100    ,   500    ,   100-100-400-400",
            "000    ,    000     ,    100    ,   100    ,   000-000-100-100",
            "100    ,    100     ,    000    ,   000    ,   000-000-100-100",
            //"-100   ,    -100    ,    -50    ,   -50    ,                  ",
            "100    ,    100     ,    100    ,   500    ,   100-100-000-400",
            "100    ,    100     ,    500    ,   100    ,   100-100-400-000",
            "500    ,    500     ,    500    ,   500    ,   500-500-000-000",
    })
    public void testCheckOrientation (int startPointX, int startPointY, int endPointX, int endPointY, String expectedString) {
        Point startPoint = new Point(startPointX, startPointY);
        Point endPoint = new Point(endPointX, endPointY);
        int [] expectedArray = Arrays.stream(expectedString.split("-")).map(Integer::valueOf).mapToInt(Integer::intValue).toArray();
        int [] actualArray = paintPanel.checkOrientation(startPoint, endPoint);
        assertArrayEquals(expectedArray, actualArray);
    }
}
