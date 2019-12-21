package com.scep.genetic;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ADN {

    public static final int MAX_POINTS = 100, NUMBER_CARAC = 7;

    public HashMap<Caracteristic, Integer> genes;

    public ADN(int damage, int health, int atk_speed, int mov_speed, int crit_chance, int crit_damage, int dodge) {
        genes = new HashMap<>();
        genes.put(Caracteristic.DAMAGE, damage);
        genes.put(Caracteristic.HEALTH, health);
        genes.put(Caracteristic.ATK_SPEED, atk_speed);
        genes.put(Caracteristic.MOV_SPEED, mov_speed);
        genes.put(Caracteristic.CRIT_CHANCE, crit_chance);
        genes.put(Caracteristic.CRIT_DAMAGE, crit_damage);
        genes.put(Caracteristic.DODGE, dodge);
    }

    public ADN(JSONObject dna){
        genes = new HashMap<>();
        for(Caracteristic c:Caracteristic.values()){
            genes.put(c, (int) dna.get(c));
        }
    }

    public ADN mutate(int maxOvershoot){

        int total = 0;
        for (int i = 0; i < NUMBER_CARAC; i++) {
            total += genes.get(Caracteristic.values()[i]);
        }
        List<Integer> modifiedCaracs = new ArrayList<>();

        while(modifiedCaracs.size() < NUMBER_CARAC){

            int rankToMutate = ThreadLocalRandom.current().nextInt(0, NUMBER_CARAC);

            if(!modifiedCaracs.contains(rankToMutate)){

                modifiedCaracs.add(rankToMutate);
                //Costly !
                Caracteristic toChange = Caracteristic.values()[rankToMutate];

                int originalValue = genes.get(toChange);

                //Only a limited chance of mutation
                if(ThreadLocalRandom.current().nextInt(0,100) < 95){
                    genes.replace(toChange, originalValue);
                    continue;
                }

                //Choose the amount by which to change the gene
                int randomChange = ThreadLocalRandom.current().nextInt(-maxOvershoot, maxOvershoot);

                while(originalValue + randomChange < 0 || originalValue + randomChange > MAX_POINTS){
                    randomChange = ThreadLocalRandom.current().nextInt(-maxOvershoot, maxOvershoot);
                }

                //If we reached a limit, do not choose the amount randomly
                if(total < MAX_POINTS-maxOvershoot && originalValue <= MAX_POINTS-maxOvershoot){
                    randomChange = maxOvershoot;
                } else if (total > MAX_POINTS+maxOvershoot && originalValue >= maxOvershoot){
                    randomChange = -maxOvershoot;
                }

                genes.replace(toChange, originalValue+randomChange);
                total += randomChange;
            }
        }

        while(total != MAX_POINTS){
            int rankToMutate = ThreadLocalRandom.current().nextInt(0, NUMBER_CARAC);
            int change = MAX_POINTS-total;

            Caracteristic toChange = Caracteristic.values()[rankToMutate];
            int originalValue = genes.get(toChange);

            if(originalValue > MAX_POINTS-change || originalValue < -change)
                continue;

            genes.replace(toChange, originalValue+change);
            total += change;
        }

        return this;
    }

    @Override
    public String toString() {
        return "genes=" + genes;
    }

    public JSONObject toJson(){
        JSONObject dna = new JSONObject();
        for(Caracteristic c:Caracteristic.values()){
            dna.put(c.name(), genes.get(c));
        }
        return dna;
    }

    public ADN fromJson(JSONObject dna){
        return new ADN(dna);
    }

    public static void main(String[] args) {
        ADN adn = new ADN(14, 12, 21, 18, 23, 10, 2);
        ADN originalADN = new ADN(14, 12, 21, 18, 23, 10, 2);
        for (int i = 0; i < 100; i++) {
            adn.mutate(5);
            int totalValues = 0;
            for (int value : adn.genes.values()){
                totalValues += value;
                if(value < 0 || value > MAX_POINTS){
                    System.out.println("Valeur hors des limites : "+value);
                }
            }
            if(totalValues != 100)
                System.out.println("Total hors des limites");
        }
        System.out.println("System is done");
        System.out.println("Result : "+adn.genes);
        System.out.println("Original : "+originalADN.genes);
    }

}
