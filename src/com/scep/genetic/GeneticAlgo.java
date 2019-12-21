package com.scep.genetic;

import com.scep.fighting.*;
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

public class GeneticAlgo {

    private List<Fighter> fighters;
    private List<Fight> fights;
    private HashMap<Integer, Float> idToScore;
    private HashMap<Integer, ADN> idToADN;
    private int numberOfFighters;

    public GeneticAlgo() {
        this(99);
    }

    public GeneticAlgo(int numberOfFighters){

        fighters = new ArrayList<>();
        idToScore = new HashMap<>();
        idToADN = new HashMap<>();

        this.numberOfFighters = numberOfFighters;

        for (int i = 0; i < numberOfFighters; i++) {
            switch (i%3) {
                case 0:
                    fighters.add(new Warrior(i, "sprite.png"));
                    break;
                case 1:
                    fighters.add(new Thief(i, "sprite.png"));
                    break;
                case 2:
                    fighters.add(new Mage(i, "sprite.png"));
                    break;
            }

            int bound = ADN.MAX_POINTS/ADN.NUMBER_CARAC;

            int damage = ThreadLocalRandom.current().nextInt(1, bound);
            int health = ThreadLocalRandom.current().nextInt(1, bound);
            int atk_speed = ThreadLocalRandom.current().nextInt(1, bound);
            int mov_speed = ThreadLocalRandom.current().nextInt(1, bound);
            int crit_chance = ThreadLocalRandom.current().nextInt(1, bound);
            int crit_damage = ThreadLocalRandom.current().nextInt(1, bound);
            int dodge = ThreadLocalRandom.current().nextInt(1, bound);

            ADN adn = new ADN(damage, health, atk_speed, mov_speed, crit_chance, crit_damage, dodge);

            //To meet the limits for the ADN
            adn.mutate(5);

            idToADN.put(i, adn);
            idToScore.put(i, 0f);
        }
    }


    private void computeScore(FightResult result){
        int idToChange = result.getWinner().getId();
        float toReplace = idToScore.get(idToChange);
        idToScore.replace(idToChange, toReplace+1);
    }

    private void computeFinalScore(){
        for(Integer id : idToScore.keySet()){
            float toReplace = idToScore.get(id);
            idToScore.replace(id, toReplace/(numberOfFighters*2));
        }
    }

    private void createMatches(){
        fights = new ArrayList<>();
        for(Fighter fighter : fighters){
            fighters.forEach(fighter1 -> fights.add(new Fight(fighter, fighter1)));
        }
        //Reset scores
        idToScore.keySet().stream().forEach(id -> idToScore.replace(id, 0f));
    }

    private void reproduce(){
        HashMap<Integer, ADN> idToADNnew = new HashMap<>();
        for (int fighterNumber = 0; fighterNumber < fighters.size(); fighterNumber++) {

            float reproduceThreshold = 0f;
            int idToReproduce = 0;

            //We only reproduce warriors with warriors, a magician adn will never be passed down to a thief or a warrior
            for (int id = fighterNumber; id < idToScore.size(); id+=3) {

                //The chance to reproduce is a random number multiplied by the score of the fighter
                float chance = ThreadLocalRandom.current().nextFloat()*0.5f+idToScore.get(id);

                if(chance > reproduceThreshold){
                    reproduceThreshold = chance;
                    idToReproduce = id;
                }

            }

            ADN replacingADN = idToADN.get(idToReproduce).mutate(4);
/*
            if(replacingADN.equals(idToADN.get(fighterNumber)) && idToReproduce != fighterNumber) {
                System.out.println("At index " + fighterNumber);
                System.out.println("idToReproduce = " + idToReproduce);
                System.out.println("score : " + idToScore.get(idToReproduce));
                System.out.println("Mutated ADN = " + replacingADN);
            }
*/
            idToADNnew.put(fighterNumber, replacingADN);
        }
        idToADN = idToADNnew;
    }

    public void runMatches() {
        createMatches();

        for(Fight fight : fights){

            fight.fighter1.reset();
            fight.fighter2.reset();
            fight.fighter1.moveTo(50, 50);
            fight.fighter2.moveTo(150, 50);

            fight.fighter1.setOpponent(fight.fighter2);
            fight.fighter2.setOpponent(fight.fighter1);
            FightResult result = fight.call();
            computeScore(result);
        }
        computeFinalScore();
        reproduce();
    }

    public List<ADN> getADNList(){
        return new ArrayList<>(idToADN.values());
    }

    public void toFile(String filename){
        JSONObject dnas = new JSONObject();
        for(int id:idToADN.keySet()){
            dnas.put(id, idToADN.get(id).toJson());
        }
        //Write JSON file
        try (FileWriter file = new FileWriter(filename)) {
            file.write(dnas.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fromFile(String filename) {
        idToADN = new HashMap<>();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filename))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject dnas = (JSONObject) obj;
            System.out.println(dnas);

            //Iterate over dna array
            for(int i=0; i<dnas.size(); i++){
                idToADN.put(i, (ADN) dnas.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
