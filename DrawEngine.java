import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
// import java.awt.Shape;
// import java.awt.geom.Line2D;
// import java.awt.Color;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class DrawEngine extends JComponent {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //use g2 because better handling, example: with g rotated rect requires 4 lines, g2 requires 1 rect and 1 rotation.
        Graphics2D g2 = (Graphics2D)g;
        
        try {
            for (Asset asset : Global.assetsReadyToBeRendered) {
                if (asset.getKind() == "line"){
                    g2.setStroke(new BasicStroke(4));
                    g2.setColor(asset.getColor());
                    g2.draw(asset.getShape());
                    
                }else{
                    g2.setColor(asset.getColor());
                    //g2.setColor(new Color(255, 0, 0));
                    g2.fill(asset.getShape());
                }
            }
        } catch (ConcurrentModificationException | NoSuchElementException | NullPointerException e) {}
        

    }
}

// import java.awt.Color;
// import java.util.concurrent.TimeUnit;
// import java.awt.image.BufferedImage;