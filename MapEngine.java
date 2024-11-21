// import javax.swing.SwingUtilities;
// import java.awt.Graphics;
// import java.awt.Color;
// import javax.swing.JPanel;

// import java.util.Map;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Arrays;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// import java.awt.Toolkit;
// import java.awt.Cursor;
// import java.awt.image.BufferedImage;
// import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Arrays;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentAdapter;

public class MapEngine extends Thread{
    private JFrame myJFrame;
    private JComponent jcomp;
    // private Cursor defaultCursor;
    // private Cursor noCursor;

    public MapEngine (JComponent jcomp, KeyListener keyl, MouseAdapter mousea, ComponentAdapter compa){
        this.myJFrame = new JFrame("AI - V0");
        this.myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.myJFrame.setPreferredSize(new Dimension(400, 300));
        Global.windowWidth = this.myJFrame.getWidth();
        Global.windowHeight = this.myJFrame.getHeight()-39;

        this.myJFrame.add(jcomp);
        this.myJFrame.addKeyListener(keyl);
        this.myJFrame.addMouseMotionListener(mousea);
        this.myJFrame.addMouseListener(mousea);
        this.myJFrame.addComponentListener(compa);

        this.myJFrame.pack();
        this.myJFrame.setVisible(true);

        this.jcomp = jcomp;
        
        Global.screenLocation = this.myJFrame.getLocationOnScreen();
    }

    private void checkWindow(){
        if (Global.isSizeChanged == true){
            Global.windowWidth = this.myJFrame.getWidth();
            Global.windowHeight = this.myJFrame.getHeight()-39;
            Global.isSizeChanged = false;
        }
        if (Global.isMoved == true) {
            Global.screenLocation = this.myJFrame.getLocationOnScreen();
            Global.isMoved = false;
        }
    }

    private void handleInput(){
        if (Global.keystatus.get(KeyEvent.VK_LEFT) || Global.keystatus.get(KeyEvent.VK_A)){
            Global.xTranslation -= 10*Global.zoom;
            // System.out.println("left");
        }
        if (Global.keystatus.get(KeyEvent.VK_RIGHT) || Global.keystatus.get(KeyEvent.VK_D)){
            Global.xTranslation += 10*Global.zoom;
            // System.out.println("right");
        }
        if (Global.keystatus.get(KeyEvent.VK_UP) || Global.keystatus.get(KeyEvent.VK_W)){
            Global.yTranslation += 10*Global.zoom;
            // System.out.println("up");
        }
        if (Global.keystatus.get(KeyEvent.VK_DOWN) || Global.keystatus.get(KeyEvent.VK_S)){
            Global.yTranslation -= 10*Global.zoom;
            // System.out.println("down");
        }

        if (Global.keystatus.get(KeyEvent.VK_SPACE) || Global.keystatus.get(KeyEvent.VK_Q)){
            Global.zoom += 1;
            // System.out.println("zoom out");
        }
        if (Global.keystatus.get(KeyEvent.VK_SHIFT) || Global.keystatus.get(KeyEvent.VK_E)){
            if (Global.zoom > 1){
                Global.zoom -= 1;
            }
            // System.out.println("zoom in");
        }
    }

    @Override
    public void run(){

        // add first lines on screen
        Asset asset = new Asset();
        asset.setKind("line");
        asset.setColor(new Color(255, 0, 0));
        asset.setCoordinates2d(new ArrayList<Double>(Arrays.asList((double)-500, (double)170, (double)500, (double)170)));
        Global.drawnAssets.add(asset);
        Asset asset2 = new Asset();
        asset2.setKind("line");
        asset2.setColor(new Color(255, 0, 0));
        asset2.setCoordinates2d(new ArrayList<Double>(Arrays.asList((double)-500, (double)-170, (double)500, (double)-170)));
        Global.drawnAssets.add(asset2);
    
        while (true){
            checkWindow();
            handleInput();

            // refresh screen
            Global.updateFrame();
            jcomp.repaint();

            // wait fps
            try{
                Thread.sleep(Math.round(1000/Global.fps));
            }catch (InterruptedException e) {}

        }
    }
}