import java.util.ArrayList;
import java.lang.Math;
// import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Car extends Thread {
    private int carNumber;
    private int amountOfRays;
    private double xPosition;
    private double yPosition;
    private double xPositionOld;
    private double yPositionOld;
    private double direction;
    private ArrayList<Double> seenDistances = new ArrayList<>();
    private ArrayList<Double> rays = new ArrayList<>();
    private int rayLength = 750;
    private double traveledDistance = 0;
    private double speed = 4;
    private boolean hitLine = false;
    private ArrayList<Double> carLines = new ArrayList<>();
    private Network network;
    private long timeCounterInMillis = 0; 
    
    public Car(int carNumber, int amountOfRays, double xPosition, double yPosition, double direction, Network network){
        this.carNumber = carNumber;
        this.amountOfRays = amountOfRays;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.direction = direction;
        this.network = network;
        // System.out.println("car " + carNumber + " created");
    }

    private void updateTraveledDistance(){
        if (xPosition != xPositionOld || yPosition != yPositionOld){
            traveledDistance += Math.sqrt( Math.pow(xPosition-xPositionOld, 2) + Math.pow(yPosition-yPositionOld, 2));
        }
        xPositionOld = xPosition;
        yPositionOld = yPosition;
    }

    private ArrayList<Double> checkCollision(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        ArrayList<Double> values = new ArrayList<>();

        // calculate the distance to intersection point
        double uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        double uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        // if uA and uB are between 0-1, lines are colliding
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

            // optionally, draw a circle where the lines meet
            double intersectionX = x1 + (uA * (x2 - x1));
            double intersectionY = y1 + (uA * (y2 - y1));
            
            values.add(intersectionX);
            values.add(intersectionY);

            return values;
        }
        return values;
    }

    private void calculateSeenDistances() {
        rays.clear();
        seenDistances.clear();
        for (int i = 0; i < amountOfRays; i++) {
            double angle = direction - Math.PI / 4 + ( ((Math.PI / 2) / (amountOfRays-1)) * i );
            double x = xPosition + Math.cos(angle) * rayLength;
            double y = yPosition +  Math.sin(angle) * rayLength;
            rays.add(x);
            rays.add(y);
            seenDistances.add((double)rayLength);
            for (Asset asset : Global.drawnAssets) {
                ArrayList<Double> intersection = checkCollision(xPosition, yPosition, x, y, asset.getCoordinates2d().get(0), asset.getCoordinates2d().get(1), asset.getCoordinates2d().get(2), asset.getCoordinates2d().get(3));
                if (!(intersection.isEmpty())){
                    double distance = Math.sqrt( Math.pow(xPosition-intersection.get(0), 2) + Math.pow(yPosition-intersection.get(1), 2));
                    if (distance < seenDistances.get(i)){
                        seenDistances.set(i, distance);
                    }
                }
            }
            seenDistances.set(i, seenDistances.get(i)/rayLength);
            // if (seenDistances.get(i) < 0.1) {
            //     hitLine = true;
            // }
        }
    }

    private void processNetworkInput(){
        ArrayList<Double> outputs = network.calculateOutputs(seenDistances); 
        double turnLeft = outputs.get(0);
        double turnRight = outputs.get(1);
        // double increaseSpeed = outputs.get(2);
        // double decreaseSpeed = outputs.get(3);
        
        direction += (turnLeft-turnRight)*(Math.PI/Global.fps);
        // speed += increaseSpeed*0.02-decreaseSpeed*0.01;
        // if (speed < 4) {
        //     speed = 4;
        // }
        speed += 0.01;
    }

    private synchronized void visualize() {
        carLines.clear();
        double x = Math.cos(direction) * 50;
        double y = Math.sin(direction) * 50;
        double x2 = Math.cos(direction + Math.PI) * 50;
        double y2 = Math.sin(direction + Math.PI) * 50;
        Global.addCarAsset(xPosition+x, yPosition+y, xPosition+x2, yPosition+y2);
        carLines.add(xPosition+x);
        carLines.add(yPosition+y);
        carLines.add(xPosition+x2);
        carLines.add(yPosition+y2);
        x = Math.cos(direction + Math.PI) * 50 + Math.cos(direction - Math.PI/2) * 25;
        y = Math.sin(direction + Math.PI) * 50 + Math.sin(direction - Math.PI/2) * 25;
        x2 = Math.cos(direction + Math.PI) * 50 + Math.cos(direction + Math.PI/2) * 25;
        y2 = Math.sin(direction + Math.PI) * 50 + Math.sin(direction + Math.PI/2) * 25;
        Global.addCarAsset(xPosition+x, yPosition+y, xPosition+x2, yPosition+y2);
        carLines.add(xPosition+x);
        carLines.add(yPosition+y);
        carLines.add(xPosition+x2);
        carLines.add(yPosition+y2);
        // for (int i = 0; i < amountOfRays*2; i += 2) {
        //     Global.addCarAsset(xPosition, yPosition,  rays.get(i), rays.get(i + 1));
        // }
    }

    private void checkCarCollision(){
        
        for (Asset asset : Global.drawnAssets) {
            ArrayList<Double> intersection = checkCollision(carLines.get(0), carLines.get(1), carLines.get(2), carLines.get(3), asset.getCoordinates2d().get(0), asset.getCoordinates2d().get(1), asset.getCoordinates2d().get(2), asset.getCoordinates2d().get(3));
            if (!(intersection.isEmpty())){
                hitLine = true;
            }
            ArrayList<Double> intersection2 = checkCollision(carLines.get(4), carLines.get(5), carLines.get(6), carLines.get(7), asset.getCoordinates2d().get(0), asset.getCoordinates2d().get(1), asset.getCoordinates2d().get(2), asset.getCoordinates2d().get(3));
            if (!(intersection2.isEmpty())){
                hitLine = true;
            }
        }
    }

    private synchronized void setCarOutput(){
        HashMap<String, Object> info = new HashMap<>();
        info.put("traveledDistance", traveledDistance);
        info.put("speed", speed);
        info.put("networkWeights", network.retrieveWeights());
        info.put("networkBiases", network.retrieveBiases());
        Global.carsOutput.set(carNumber, info);
        // System.out.println("Car " + carNumber + " stopped");
    }

    @Override
    public void run() {

        // stop car if it has hit a line or has been riding for longer than 2 minutes
        while (hitLine == false && timeCounterInMillis < 120_000) {

            if (speed > 0.01) {
                speed -= 0.01;
            }
            
            xPosition += Math.cos(direction) * speed * 1;
            yPosition += Math.sin(direction) * speed * 1;

            updateTraveledDistance();

            calculateSeenDistances();

            processNetworkInput();

            visualize();

            checkCarCollision();

            // if (Global.keystatus.get(KeyEvent.VK_Y)){
            //     System.out.println(seenDistances);
            // }

            // wait fps
            try {
                Thread.sleep(Math.round(1000 / Global.fps));
                timeCounterInMillis += 1000 / Global.fps;
            } catch (InterruptedException e) {}
            
        }
        setCarOutput();
        
    }
}
