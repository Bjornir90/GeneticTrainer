package com.scep;

import com.scep.genetic.ADN;
import com.scep.genetic.Caracteristic;
import com.scep.genetic.GeneticAlgo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointOfEntry {
    private static int NUMBER_OF_FIGHTERS = 498;

    public static void writeResults(List<List<ADN>> list) throws IOException {
        FileWriter fw = new FileWriter("Result.csv");
        fw.write("Iteration,Health,AttackSpeed,CritChance,CritDamage,Damage,Dodge,MoveSpeed,\n");
        int iterationNumber = 1;
        for(List<ADN> iterationResult : list){
            for(ADN adn : iterationResult){
                String toPrint = iterationNumber+","+adn.genes.get(Caracteristic.HEALTH) + ","+adn.genes.get(Caracteristic.ATK_SPEED) + ","+adn.genes.get(Caracteristic.CRIT_CHANCE) + ","+adn.genes.get(Caracteristic.CRIT_DAMAGE) + ","+adn.genes.get(Caracteristic.DAMAGE) + ","+adn.genes.get(Caracteristic.DODGE) + ","+adn.genes.get(Caracteristic.MOV_SPEED) + ",\n";
                fw.write(toPrint);
            }
            fw.flush();
            iterationNumber++;
        }
        fw.close();
    }

    public static void writeMeanResults(List<List<ADN>> list) throws IOException {
        FileWriter fw = new FileWriter("MeanResult10.csv");
        Caracteristic[] enumValues = Caracteristic.values();

        fw.write("Iteration,Class,");
        for (int i = 0; i < ADN.NUMBER_CARAC; i++) {
            fw.write(enumValues[i].toString()+",");
        }
        fw.write("\n");

        List<HashMap<String, HashMap<Caracteristic, Integer>>> listMeanValues = new ArrayList<>();

        for(List<ADN> iterationResult : list){
            int adnNumber = 0;
            HashMap<String, HashMap<Caracteristic, Integer>> meanValues = new HashMap<>();
            meanValues.put("Warrior", new HashMap<>());
            meanValues.put("Thief", new HashMap<>());
            meanValues.put("Mage", new HashMap<>());

            for (int i = 0; i < ADN.NUMBER_CARAC; i++) {
                meanValues.get("Warrior").put(Caracteristic.values()[i], 0);
                meanValues.get("Thief").put(Caracteristic.values()[i], 0);
                meanValues.get("Mage").put(Caracteristic.values()[i], 0);
            }


            for(ADN adn : iterationResult){
                String classId = "";
                switch(adnNumber%3){
                    case 0:
                        classId = "Warrior";
                        break;
                    case 1:
                        classId = "Thief";
                        break;
                    case 2:
                        classId = "Mage";
                        break;
                }

                for (int i = 0; i < ADN.NUMBER_CARAC; i++) {
                    //Add the value of the current ADN to the current value to compute the mean
                    if(adn.genes.get(enumValues[i]) >= 100)
                        System.out.println("ERROR : value out of range : "+enumValues[i]+" : "+adn.genes.get(enumValues[i]));
                    meanValues.get(classId).replace(enumValues[i], adn.genes.get(enumValues[i])+meanValues.get(classId).get(enumValues[i]));
                }
                adnNumber++;
            }

            for (int i = 0; i < ADN.NUMBER_CARAC; i++) {
                meanValues.get("Warrior").replace(enumValues[i], meanValues.get("Warrior").get(enumValues[i])/(NUMBER_OF_FIGHTERS/3));
                meanValues.get("Thief").replace(enumValues[i], meanValues.get("Thief").get(enumValues[i])/(NUMBER_OF_FIGHTERS/3));
                meanValues.get("Mage").replace(enumValues[i], meanValues.get("Mage").get(enumValues[i])/(NUMBER_OF_FIGHTERS/3));
            }

            listMeanValues.add(meanValues);
        }

        int iterationNumber = 1;
        for(HashMap<String, HashMap<Caracteristic, Integer>> meanValue : listMeanValues){

            HashMap<Caracteristic, Integer> warriorValues = meanValue.get("Warrior");
            HashMap<Caracteristic, Integer> thiefValues = meanValue.get("Thief");
            HashMap<Caracteristic, Integer> mageValues = meanValue.get("Mage");


            String toPrint = iterationNumber+",Warrior,";
            for (int i = 0; i < ADN.NUMBER_CARAC; i++) {
                toPrint += warriorValues.get(enumValues[i])+",";
            }
            toPrint += "\n";
            fw.write(toPrint);

            toPrint = iterationNumber+",Thief,";
            for (int i = 0; i < ADN.NUMBER_CARAC; i++) {
                toPrint += thiefValues.get(enumValues[i])+",";
            }
            toPrint += "\n";
            fw.write(toPrint);

            toPrint = iterationNumber+",Mage,";
            for (int i = 0; i < ADN.NUMBER_CARAC; i++) {
                toPrint += mageValues.get(enumValues[i])+",";
            }
            toPrint += "\n";
            fw.write(toPrint);

            fw.flush();

            iterationNumber++;
        }
        fw.close();
    }

    public static void main(String[] args) throws IOException {
        GeneticAlgo algo = new GeneticAlgo(NUMBER_OF_FIGHTERS);
        List<List<ADN>> listOfResult = new ArrayList<>();
        int numberOfIterations = 400;
        for (int i = 0; i < numberOfIterations; i++) {
            algo.runMatches();
            List<ADN> listADN = algo.getADNList();
            //algo.toFile("iterations\iteration"+i+".json");
            listOfResult.add(listADN);
            if(i%(numberOfIterations/10) == 0){
                System.out.println("Done "+i/(numberOfIterations/100)+"%");
            }
        }
        //writeResults(listOfResult);

        System.out.println("Writing results...");
        writeMeanResults(listOfResult);
        System.out.println("Computation complete with "+numberOfIterations+" iterations");
    }
}
