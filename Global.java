import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
// import java.util.Map;
// import java.util.HashMap;
// import java.util.TreeMap;
import java.util.List;
// import java.util.Map;
import java.awt.Point;

// import java.util.Collections;



// import org.json.simple.*;
// import org.json.simple.parser.*;

// import java.awt.geom.Area;
// import java.awt.Shape;
// import java.awt.geom.Rectangle2D;
// import java.lang.reflect.Array;
import java.awt.geom.Line2D;
// import java.awt.Polygon;
// import java.awt.Triangle;
import java.awt.Color;
// import java.awt.Toolkit;
// import java.awt.Dimension;





public class Global {

    // list of lines drawn manually
    static ArrayList<Asset> drawnAssets = new ArrayList<>();

    // list of lines that make up a car
    static ArrayList<Asset> CarAssets = new ArrayList<>();

    // method to add lines of car to list
    static void addCarAsset(double x, double y, double x2, double y2){
        Asset line1 = new Asset();
        line1.setKind("line");
        line1.setColor(new Color(0, 255, 0));
        line1.setCoordinates2d( new ArrayList<Double>(Arrays.asList(x, y, x2, y2)));
        synchronized(CarAssets){
            CarAssets.add(line1);
        }
    }

    // list of lines that will be rendered
    static ArrayList<Asset> assetsReadyToBeRendered = new ArrayList<>();

    // method to update lines corresponding with translation and zoom
    static void updateFrame(){
        assetsReadyToBeRendered.clear();
        for (Asset asset : drawnAssets) {
            List<Double> c = asset.getCoordinates2d();
            List<Double> c2 = new ArrayList<Double>(Arrays.asList( (c.get(0)-xTranslation-windowWidth/2)/zoom+windowWidth/2, (c.get(1)+yTranslation-windowHeight/2)/zoom+windowHeight/2, (c.get(2)-xTranslation-windowWidth/2)/zoom+windowWidth/2, (c.get(3)+yTranslation-windowHeight/2)/zoom+windowHeight/2 ));
            asset.setShape(new Line2D.Double((double)c2.get(0), (double)c2.get(1), (double)c2.get(2), (double)c2.get(3)));
            assetsReadyToBeRendered.add(asset);
        }

        synchronized(CarAssets){
            for (Asset asset : CarAssets) {
                List<Double> c = asset.getCoordinates2d();
                List<Double> c2 = new ArrayList<Double>(Arrays.asList( (c.get(0)-xTranslation-windowWidth/2)/zoom+windowWidth/2, (c.get(1)+yTranslation-windowHeight/2)/zoom+windowHeight/2, (c.get(2)-xTranslation-windowWidth/2)/zoom+windowWidth/2, (c.get(3)+yTranslation-windowHeight/2)/zoom+windowHeight/2 ));
                asset.setShape(new Line2D.Double((double)c2.get(0), (double)c2.get(1), (double)c2.get(2), (double)c2.get(3)));
                assetsReadyToBeRendered.add(asset);
            }
            CarAssets.clear();
        }
    }
    
    // field about keyboard
    static ArrayList<Boolean> keystatus = new ArrayList<>();
    static{
        for (int i = 0; i < 65536; i++) {
            keystatus.add(false);
        }
    }

    // fields about mouse
    static double mouseX;
    static double mouseY;

    // fields about window
    static int windowWidth;
    static int windowHeight;
    static Point screenLocation;
    
    static boolean isSizeChanged = false;
    static boolean isMoved = false;

    // fields for initial game settings
    static double xTranslation = 0;
    static double yTranslation = 0;
    static double zoom = 1;

    static int fps = 50;

    static ArrayList<HashMap<String, Object>> carsOutput = new ArrayList<>();
    static{
        for (int i = 0; i < 9999; i++) {
            carsOutput.add(new HashMap<>());
        }
    }

}
