package edu.BarSU.AI.Algorithms;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created on 15.05.2017.
 */
public class AntColony {
    private static ArrayList<Integer> antMemory;
    private static int сoastWay;

    private static ArrayList<Integer> antMemoryFinal;
    private static int сoastWayFinal;

    private static double[][] coastMatrix;
    private static double[][] pheromoneMatrix;

    private static double Q;
    private static double p;
    private static double A;
    private static double B;

    // edit area
    public static ArrayList<Integer> StartMethod(double[][] SrcCoastMatrix, double Src_Q, double Src_p, double Src_A, double Src_B) {
        if (SrcCoastMatrix.length < 2) {
            System.out.println("Matrix size is smile!");
            return null;
        }

        coastMatrix = SrcCoastMatrix.clone();

        pheromoneMatrix = new double[coastMatrix.length][coastMatrix.length];

        for (int i = 0; i < pheromoneMatrix.length; ++i)
            for (int j = 0; j < pheromoneMatrix.length; ++j)
                pheromoneMatrix[i][j] = 1;

        сoastWay = 0;

        antMemory = new ArrayList<Integer>();

        Q = Src_Q;
        p = Src_p;
        A = Src_A;
        B = Src_B;

        int i = 0;
        do {
            prepareIteration();

            antMoving();

            ++i;

            updatePheromoneMatrix();
        } while (сoastWayFinal > 2500);

        antMemoryFinal.add(сoastWayFinal);

        antMemory.add(i);

        return antMemoryFinal;
    }

    private static void prepareIteration() {
        if(сoastWay > сoastWayFinal) {
            сoastWayFinal = сoastWay;
            antMemoryFinal = (ArrayList<Integer>) antMemory.clone();
        }

        сoastWay = 0;
        antMemory.clear();
    }

    private static ArrayList<Double> getProbability(int fromCity, ArrayList<Integer> inCity) {
        ArrayList<Double> probabilityArray = new ArrayList<>(inCity.size());
        double summProbability = 0;

        for (int inTemp: inCity) {
            probabilityArray.add(getInPointProbability(fromCity, inTemp));
            summProbability += probabilityArray.get(probabilityArray.size() - 1);

            double tempProbability = probabilityArray.get(probabilityArray.size() - 1);
        }

        double minProbability, maxProbability;

        minProbability = maxProbability = 100 * (probabilityArray.get(0) / summProbability);

        for (int i = 0; i < probabilityArray.size(); ++i) {
            double tempProbability = probabilityArray.get(i);
            tempProbability = 100 * (tempProbability / summProbability);
            probabilityArray.set(i, tempProbability);

            if (tempProbability > maxProbability)
                maxProbability = tempProbability;

            if (tempProbability < minProbability)
                minProbability = tempProbability;
        }

        for (int i = 0; i < probabilityArray.size(); ++i) {
            double tempProbability = probabilityArray.get(i);
            tempProbability -= minProbability;
            probabilityArray.set(i, tempProbability);
        }

        for (int i = 0; i < probabilityArray.size(); ++i) {
            double tempProbability = probabilityArray.get(i);
            tempProbability = (tempProbability * 100) / maxProbability;
            probabilityArray.set(i, tempProbability);
        }

        return probabilityArray;
    }

    private static double getInPointProbability(int from, int in) {
        return Math.pow(1.0 / coastMatrix[from - 1][in - 1], A) * Math.pow(pheromoneMatrix[from - 1][in - 1], B);
    }

    private static void antMoving() {
        antMemory.add(1);

        ArrayList<Integer> unvisitedCity = new ArrayList<Integer>(coastMatrix.length);

        for (int i = 2; i <= coastMatrix.length; ++i)
            unvisitedCity.add(i);

        //temp object
        Random rnd = new Random(System.nanoTime());

        do {
            ArrayList<Double> cityProbability = getProbability(antMemory.get(antMemory.size() - 1), unvisitedCity);

            // TODO change selector
            int tempCity = (unvisitedCity.size() != 1)?
                    unvisitedCity.get(rnd.nextInt(unvisitedCity.size() - 1)):
                    unvisitedCity.get(0);

            antMemory.add(tempCity);
            unvisitedCity.remove((Object) tempCity);

            сoastWay += coastMatrix[antMemory.get(antMemory.size() - 2) - 1][antMemory.get(antMemory.size() - 1) - 1];
        } while (!unvisitedCity.isEmpty());

        сoastWayFinal = сoastWay;
        antMemoryFinal = antMemory;
    }

    private static void updatePheromoneMatrix() {
        for (int i = 0, IndOld = 0; i < antMemory.size(); IndOld = ++i)
            if (pheromoneMatrix[IndOld][i] < 100) {
                pheromoneMatrix[IndOld][i] = getPheromoneAddition(сoastWay, pheromoneMatrix[IndOld][i]);

                if (antMemory.size() - 1 == i)
                    pheromoneMatrix[i][0] = getPheromoneAddition(сoastWay, pheromoneMatrix[i][0]);
            }

        for (int i = 0; i < pheromoneMatrix.length; ++i)
            for (int j = 0; j < pheromoneMatrix.length; ++j)
                pheromoneMatrix[i][j] = getPheromoneEvaporation(pheromoneMatrix[i][j]);
    }

    private static double getPheromoneDelta(double L) {
        return Q/L;
    }

    private static double getPheromoneEvaporation(double MatrixT) {
        return (1 - p) * MatrixT;
    }
    //
    private static double getPheromoneAddition(double L, double MatrixT) {
        return getPheromoneDelta(L) + (MatrixT * p);
    }
}

