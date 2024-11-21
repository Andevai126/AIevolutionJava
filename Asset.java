import java.util.List;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Shape;

public class Asset {
    //fields
    private String kind;
    // private List<Integer> colorComponents = new ArrayList<>();
    private Color color;
    private List<Double> coordinates3d = new ArrayList<>();
    
    
    private double distance;
    private List<Double> coordinates2d = new ArrayList<>();
    private Shape shape;
    
    //getters and setters
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getKind() {
        return kind;
    }
    // public void setColorComponents(List<Integer> colorComponents) {
    //     this.colorComponents = colorComponents;
    // }
    // public List<Integer> getColorComponents() {
    //     return colorComponents;
    // }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
    public void setCoordinates3d(List<Double> coordinates3d) {
        this.coordinates3d = coordinates3d;
    }
    public List<Double> getCoordinates3d() {
        return coordinates3d;
    }

    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public List<Double> getCoordinates2d() {
        return coordinates2d;
    }
    public void setCoordinates2d(List<Double> coordinates2d) {
        this.coordinates2d = coordinates2d;
    }
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    public Shape getShape() {
        return shape;
    }
}