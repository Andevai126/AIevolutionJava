import java.util.ArrayList;
import java.util.Arrays;

public class App{
    public static void main(String[] args){

        // weights and biases should be able to be negative
        // start with range weights and biases both 0.1
        // improve negative or positive randomizer in network
        // lower turn speed
        // look up normal start weights and biases and changes
        // change death to death by lines of car
        // shorten range of vision, make it a slider
        // add coins?
        // log weights and biases at end of epoch
        // dont log creation of cars and death only when mananger starts doing it
        // change reward to highest speed
        // move global.carsOutput to manager via static field
        // make speed not a constant anymore
        // save 10 best cars without overwriting bias instead of 1
        // lower amount of rays?
        // make saving system to load saved network at start of simulation
        // make network bigger
        // add floor and ceiling / minimum and maximum to what weights and biases can be

        MapEngine mapEngine = new MapEngine(new DrawEngine(), new InputEngine(), new MouseEngine(), new WindowEngine());
        mapEngine.start();

        NetworksManager manager = new NetworksManager(new ArrayList<>(Arrays.asList(6, 4, 2)), 200, 150);
        manager.start();
    }
}