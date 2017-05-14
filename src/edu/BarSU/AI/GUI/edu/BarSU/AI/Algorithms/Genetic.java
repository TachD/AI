package edu.BarSU.AI.GUI.edu.BarSU.AI.Algorithms;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Govor Alexander on 30.04.2017.
 */
public class Genetic {
    private static double[][] CoastMatrix;

    private static ArrayList<ArrayList<Integer>> Population;

    private static ArrayList<Integer> SuperObj;

    private static int MinCoast;

    private static int NumberOfStableGenerationObjToSuper;

    private static int StableGeneration;

    public static ArrayList<Integer> StartMethod(double[][] SrcCoastMatrix, int NumberOfStableGeneration, int MaxSizePopulation) {
        if (!CreateNewPopulation(SrcCoastMatrix)) {
            System.out.println("Matrix size is smile!");
            return null;
        }

        Prepare(NumberOfStableGeneration);
        int i = 0;

        do {

            Reproduction();

            Selection();

            if (Population.size() > MaxSizePopulation)
                while (Population.size() > MaxSizePopulation / 10)
                    Extermination();

            ++i;
        } while (NumberOfStableGenerationObjToSuper != StableGeneration);

        SuperObj.add(i);

        return SuperObj;
    }

    private  static void Extermination() {
        Random Rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < Population.size(); ++i)
            if (Rnd.nextInt(101) < 50)
                Population.remove(i);
    }

    private static boolean CreateNewPopulation(double[][] SrcCoastMatrix) {
        if (SrcCoastMatrix.length < 2)
            return false;

        CoastMatrix = SrcCoastMatrix.clone();

        Population = new ArrayList<>();

        ArrayList<Integer> Obj = new ArrayList<>();

        for (int i = 1; i <= SrcCoastMatrix.length; ++i)
            Obj.add(i);

        Obj.add(getSumm(Obj));

        Population.add(Obj);

        Reproduction();
        Reproduction();

        return true;
    }

    private static void Prepare(int NumberOfStableGeneration) {
        StableGeneration = NumberOfStableGeneration;

        SuperObj = Population.get(0);

        MinCoast = SuperObj.get(SuperObj.size() - 1);

        NumberOfStableGenerationObjToSuper = 0;
    }

    private static void Reproduction() {
        Random RndObj = new Random(System.currentTimeMillis());

        for (int i = Population.size() - 1; i > -1 ; --i) {
            int OldPlace = RndObj.nextInt(CoastMatrix.length);
            int NewPlace = RndObj.nextInt(CoastMatrix.length);

            ArrayList<Integer> NewObj = (ArrayList<Integer>) Population.get(i).clone();

            int Temp = NewObj.get(NewPlace);

            NewObj.set(NewPlace, NewObj.get(OldPlace));
            NewObj.set(OldPlace, Temp);

            NewObj.set(NewObj.size() - 1, getSumm(NewObj));

            Population.add(NewObj);
        }
    }

    private static void Selection() {
        double Average = 0;
        int OldMinCoast = MinCoast;

        int SummIndex = Population.get(0).size() - 1;

        for (int i = 0; i < Population.size(); ++i) {
            int TempSumm = Population.get(i).get(SummIndex);
            Average += TempSumm;

            if (TempSumm < MinCoast) {
                MinCoast = TempSumm;
                NumberOfStableGenerationObjToSuper = 0;
                SuperObj = Population.get(i);
            }
        }

        if (OldMinCoast == MinCoast) {
            ++NumberOfStableGenerationObjToSuper;
            if (NumberOfStableGenerationObjToSuper == StableGeneration)
                return;
        }

        Average /= Population.size();
        // Trouble area
        Random Rnd = new Random(System.currentTimeMillis());

        for (int i = 0; i < Population.size(); ++i)
            if (getSumm(Population.get(i)) > Average)
                Population.remove(i);
    }

    private static int getSumm(ArrayList<Integer> SrcPath) {
        int Summ = 0;
        int OldPoint = SrcPath.get(0);

        for (int i = 1; i < SrcPath.size() - 1; ++i) {
            int Temp = SrcPath.get(i);

            Summ += CoastMatrix[OldPoint - 1][Temp - 1];

            if ((i + 2) == SrcPath.size())
                Summ += CoastMatrix[Temp - 1][SrcPath.get(0) - 1];
            else
                OldPoint = Temp;
        }

        return Summ;
    }
}
