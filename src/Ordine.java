import processing.core.PVector;

import java.util.ArrayList;

public class Ordine {

    private double fitness = 0;
    private double distance = 0;
    private ArrayList<PVector> ordine = new ArrayList<PVector>();

    public Ordine(ArrayList<PVector> ordine) {
        this.ordine = ordine;
        for(int i = 0; i<ordine.size()-1;i++){
            distance += ordine.get(i).dist(ordine.get(i+1));
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<PVector> getOrdine() {
        return ordine;
    }

    public void setOrdine(ArrayList<PVector> ordine) {
        this.ordine = ordine;
    }

    @Override
    public String toString() {
        return "Ordine{" +
                "fitness=" + fitness +
                ", distance=" + distance +
                ", ordine=" + ordine +
                '}';
    }

    public void addVector(PVector p) {
        ordine.add(p);
        distance=0;
        for(int i = 0; i<ordine.size()-1;i++){
            distance += ordine.get(i).dist(ordine.get(i+1));
        }
    }

    public void recalcDistance() {
        distance=0;
        for(int i = 0; i<ordine.size()-1;i++){
            distance += ordine.get(i).dist(ordine.get(i+1));
        }
    }
}
