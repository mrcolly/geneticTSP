import processing.core.PApplet;
import processing.core.PVector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MainClass extends PApplet{
    public static void main(String[] args) {
        PApplet.main("MainClass");
    }

    int generationCounter = 0;

    int numCitta;
    int populationSize;
    double mutationRate;
    int calcBeforeShow;

    Ordine best;
    Ordine currentBest;

    ArrayList<PVector> citta = new ArrayList<PVector>();

    ArrayList<Ordine> population = new ArrayList<Ordine>();

    public void settings(){
        size(1080, 720);
        numCitta = 40;
        populationSize = 500;
        mutationRate = 0.025;
        calcBeforeShow = 0;
    }

    public void setup(){

        for(int i =0; i<numCitta;i++){
            citta.add(new PVector(random(width), random(height/2)-5));
        }

        for(int i =0; i<populationSize;i++){
            population.add(randomOrdine());
        }

        calcGenerations(calcBeforeShow);

    }

    public void draw(){
       drawCalc(true);
    }

    public Ordine randomOrdine(){
        ArrayList<PVector> c = (ArrayList<PVector>) citta.clone();
        ArrayList<PVector> ordine = new  ArrayList<PVector>();
        for(int i=0;i<citta.size();i++){
            PVector p = c.get((int)random(c.size()));
            ordine.add(p);
            c.remove(p);
        }
        return new Ordine(ordine);
    }

    public void calcFitness(){
        double bestDistance = 999999999;
        currentBest = null;

        for(Ordine o : population){
            if(currentBest == null || o.getDistance()<bestDistance){
                bestDistance = o.getDistance();
                currentBest = o;
            }

            if(best == null || o.getDistance()<best.getDistance()){
                best = o;
            }

            o.setFitness(1/(Math.pow(o.getDistance(), 8) + 1));

        }

    }


    public void normalizeFitness(){
        double sum = 0;
        for(Ordine o : population){
            sum += o.getFitness();
        }
        for(Ordine o : population){
            o.setFitness(o.getFitness()/sum);
        }
    }


    public void nextGeneration() {
        ArrayList<Ordine> newPopulation = new ArrayList<Ordine>();
        for (int i = 0; i < population.size(); i++) {
              Ordine ordineA = pickOne(population);
              Ordine ordineB = pickOne(population);
              Ordine ordine = crossOver(ordineA, ordineB);
              mutate(ordine, mutationRate);
              ordine.recalcDistance();
              newPopulation.add(ordine);
        }
        population = newPopulation;
        generationCounter++;

    }

    public Ordine pickOne(ArrayList<Ordine> pop){
        double p = Math.random();
        double cumulativeProbability = 0;
        for (Ordine o : pop) {
            cumulativeProbability += o.getFitness();
            if (p <= cumulativeProbability) {
                return o;
            }
        }
        return null;
    }

    public Ordine crossOver(Ordine a, Ordine b){
        int startIndex = (int)random(a.getOrdine().size());
        int endIndex = (int)random(startIndex, a.getOrdine().size());
        Ordine newOrdine = new Ordine(new ArrayList<PVector>(a.getOrdine().subList(startIndex, endIndex)));

        for (PVector p : b.getOrdine()) {
            if (!newOrdine.getOrdine().contains(p)) {
                newOrdine.addVector(p);
            }
        }
        return newOrdine;
    }


    public void mutate(Ordine ordine, double mutationRate) {
        for (int i = 0; i < ordine.getOrdine().size(); i++) {
            if (random(1) < mutationRate) {
                int indexA = floor(random(ordine.getOrdine().size()));
                int indexB = (indexA + 1) % ordine.getOrdine().size();
                Collections.swap(ordine.getOrdine(), indexA, indexB);
            }
        }
    }

    public void calcGenerations(int generations){

        int counter =0;
        System.out.println("calculating 0%");
        for(int i = 0; i<generations;i++){
            calcFitness();
            normalizeFitness();
            nextGeneration();

            if(i!=0 && i%(generations/10) == 0){
                counter++;
                System.out.println("calculating "+counter*10+"%");
            }
        }
        System.out.println("calculated " + generations +" generations");
    }

    public void drawCalc(boolean calc){
        background(255);

        if(calc){
            calcFitness();
            normalizeFitness();
            nextGeneration();
        }else{
            calcFitness();
            normalizeFitness();
            nextGeneration();
            noLoop();
        }

        stroke(0);
        strokeWeight(4);
        noFill();
        beginShape();
        for (PVector p : best.getOrdine()) {
            vertex(p.x, p.y);
            ellipse(p.x, p.y, 16, 16);
        }
        endShape();
        fill(0, 0, 255);
        textSize(20);
        text("Generation = "+generationCounter+"\nBest = "+ new DecimalFormat("#.##").format(best.getDistance()), 10, 30);


        translate(0, height / 2);

        stroke(0);
        strokeWeight(4);
        noFill();
        beginShape();
        for (PVector p : currentBest.getOrdine()) {
            vertex(p.x, p.y);
            ellipse(p.x, p.y, 16, 16);
        }
        endShape();
        fill(0, 0, 255);
        text("Generation best = "+ new DecimalFormat("#.##").format(currentBest.getDistance()), 10, 30);


    }
}
