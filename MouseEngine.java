import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Color;

public class MouseEngine extends MouseAdapter{
    private double xStart;
    private double yStart;
    private double xEnd;
    private double yEnd;

    @Override
    public void mousePressed(MouseEvent e) {
        // System.out.println("mouse pressed");
        xStart = (Global.mouseX-9-Global.windowWidth/2)*Global.zoom+Global.windowWidth/2+Global.xTranslation;
        yStart = (Global.mouseY-32-Global.windowHeight/2)*Global.zoom+Global.windowHeight/2-Global.yTranslation;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // System.out.println("mouse released");
        xEnd = (Global.mouseX-9-Global.windowWidth/2)*Global.zoom+Global.windowWidth/2+Global.xTranslation;
        yEnd = (Global.mouseY-32-Global.windowHeight/2)*Global.zoom+Global.windowHeight/2-Global.yTranslation;
        
        Asset asset = new Asset();
        asset.setKind("line");
        asset.setColor(new Color(255, 0, 0));
        asset.setCoordinates2d(new ArrayList<Double>(Arrays.asList(xStart, yStart, xEnd, yEnd)));
        Global.drawnAssets.add(asset);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Global.mouseX = (double)e.getX();
        Global.mouseY = (double)e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Global.mouseX = (double)e.getX();
        Global.mouseY = (double)e.getY();
    }    
    
    // MouseEvent
    // MouseWheelEvent
    // MouseListener
    // MouseMotionListener
    // MouseWheelListener
}
