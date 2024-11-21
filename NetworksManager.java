import java.util.ArrayList;
import java.util.HashMap;
// import java.util.stream.Collectors;
import java.awt.event.KeyEvent;

public class NetworksManager extends Thread {
    private ArrayList<Car> cars = new ArrayList<>();
    private ArrayList<Integer> networkDimensions;
    private int batchSize;
    private int nEpochs;
    private int activeEpoch;
    private boolean start = false;
    private boolean end = false;
    private double bestEpochTraveledDistance = 0;
    private double bestAllTimeTraveledDistance = 0;
    private HashMap<String, Object> bestEpochCarInfo;
    private HashMap<String, Object> bestAllTimeCarInfo;
    // private int nDrawnLines = Global.drawnAssets.size();

    public NetworksManager(ArrayList<Integer> networkDimensions, int batchSize, int nEpochs) {
        this.networkDimensions = networkDimensions;
        this.batchSize = batchSize;
        this.nEpochs = nEpochs;
    }

    private ArrayList<ArrayList<ArrayList<Double>>> deepCopyWeights(ArrayList<ArrayList<ArrayList<Double>>> network) {
        ArrayList<ArrayList<ArrayList<Double>>> networkCopy = new ArrayList<>();
        for (ArrayList<ArrayList<Double>> column : network) {
            ArrayList<ArrayList<Double>> columnCopy = new ArrayList<>();
            for (ArrayList<Double> node : column) {
                ArrayList<Double> nodeCopy = new ArrayList<>();
                for (double weight : node) {
                    double weightCopy = weight;
                    nodeCopy.add(weightCopy);
                }
                columnCopy.add(nodeCopy);
            }
            networkCopy.add(columnCopy);
        }
        return networkCopy;
    }

    private ArrayList<ArrayList<Double>> deepCopyBiases(ArrayList<ArrayList<Double>> network) {
        ArrayList<ArrayList<Double>> networkCopy = new ArrayList<>();
        for (ArrayList<Double> column : network) {
            ArrayList<Double> columnCopy = new ArrayList<>();
            for (double bias : column) {
                double biasCopy = bias;
                columnCopy.add(biasCopy);
            }
            networkCopy.add(columnCopy);
        }
        return networkCopy;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> deepCopyCarInfo(HashMap<String, Object> info) {
        HashMap<String, Object> infoCopy = new HashMap<>();
        infoCopy.put("traveledDistance", info.get("traveledDistance"));
        infoCopy.put("speed", info.get("speed"));
        infoCopy.put("networkWeights",
                deepCopyWeights((ArrayList<ArrayList<ArrayList<Double>>>) info.get("networkWeights")));
        infoCopy.put("networkBiases", deepCopyBiases((ArrayList<ArrayList<Double>>) info.get("networkBiases")));
        return infoCopy;
    }

    // ignore casting error: object to ArrayList<ArrayList<ArrayList<Double>>> and
    // arrayList<ArrayList<Double>>
    @SuppressWarnings("unchecked")
    @Override
    public void run() {

        // wait until key t is pressed, than start
        while (start == false) {
            if (Global.keystatus.get(KeyEvent.VK_T)) {
                start = true;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }

        // simulate all epochs
        while (activeEpoch < nEpochs) {

            System.out.println("################# Epoch " + activeEpoch + " #################");

            // create networks, if not first epoch set weights and biases except fifty,
            // create cars
            cars.clear();
            for (int i = 0; i < batchSize; i++) {
                Network network = new Network(networkDimensions);
                if (activeEpoch != 0) {
                    network.overwriteWeights(
                            (ArrayList<ArrayList<ArrayList<Double>>>) deepCopyCarInfo(bestAllTimeCarInfo)
                                    .get("networkWeights"));
                    network.overwriteBiases(
                            (ArrayList<ArrayList<Double>>) deepCopyCarInfo(bestAllTimeCarInfo).get("networkBiases"));
                    // if (i != 0){ // uncomment to see alltime best car every epoch
                    network.adjustWeightsRandom(3, 0.4, true);
                    network.adjustBiasesRandom(1, 0.4, true);
                    // }
                }
                cars.add(new Car(i, networkDimensions.get(0), 0, 0, 0, network));
            }

            // start cars
            System.out.println("Starting all cars");
            for (Car car : cars) {
                car.start();
            }

            // wait for all cars to have stopped
            System.out.println("Waiting for all cars to have stopped");
            for (Car car : cars) {
                try {
                    car.join();
                } catch (InterruptedException e) {
                    System.out.println("an exception occurred whilst trying to join cars");
                }
            }

            // find best car by finding the one that has traveled the most distance
            // and save its neural network
            bestEpochTraveledDistance = 0;
            for (int i = 0; i < batchSize; i++) {
                double traveledDistance = (double) Global.carsOutput.get(i).get("traveledDistance");
                if (bestEpochTraveledDistance < traveledDistance) {
                    bestEpochTraveledDistance = traveledDistance;
                    bestEpochCarInfo = Global.carsOutput.get(i);
                }
            }

            if (bestAllTimeTraveledDistance < bestEpochTraveledDistance) {
                bestAllTimeTraveledDistance = bestEpochTraveledDistance;
                bestAllTimeCarInfo = deepCopyCarInfo(bestEpochCarInfo);
                System.out.println("Result: BEST CAR NETWORK CHANGED!!!");
            } else {
                System.out.println("Result: No improvement");
            }

            // print result of best car
            System.out.println("About this Epoch best Car: ----------------");
            System.out.println("bestCarTraveledDistance: " + bestEpochTraveledDistance);
            System.out.println("weights of network: " + bestEpochCarInfo.get("networkWeights"));
            System.out.println("biases of network: " + bestEpochCarInfo.get("networkBiases"));
            System.out.println("About the Alltime best Car: ---------------");
            System.out.println("bestCarTraveledDistance: " + bestAllTimeTraveledDistance);
            System.out.println("weights of network: " + bestAllTimeCarInfo.get("networkWeights"));
            System.out.println("biases of network: " + bestAllTimeCarInfo.get("networkBiases"));

            // repeat number of epochs
            activeEpoch++;
        }

        // wait until key t is pressed, than run best car for last time
        while (end == false) {
            if (Global.keystatus.get(KeyEvent.VK_T)) {
                end = true;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }

        cars.clear();
        Network network = new Network(networkDimensions);
        network.overwriteWeights(
                (ArrayList<ArrayList<ArrayList<Double>>>) deepCopyCarInfo(bestAllTimeCarInfo).get("networkWeights"));
        network.overwriteBiases(
                (ArrayList<ArrayList<Double>>) deepCopyCarInfo(bestAllTimeCarInfo).get("networkBiases"));
        cars.add(new Car(0, networkDimensions.get(0), 0, 0, 0, network));
        cars.get(0).start();
        try {
            cars.get(0).join();
        } catch (InterruptedException e) {
            System.out.println("an exception occurred whilst trying to join the car");
        }
    }
}