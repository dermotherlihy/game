package com.dermotherlihy.game.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Pitch {

    private List<Coordinate> pitchCordinates = new ArrayList<Coordinate>();
    private Random randomGenerator = new Random();


    public Pitch(int length, int width) {
        for(int i = 0; i<=length; i++){
            for(int j =0; j<=width; j++){
                pitchCordinates.add(new Coordinate(i, j));
            }
        }
    }

    public Coordinate getRandomCoordinate(){
        return pitchCordinates.get(randomGenerator.nextInt(pitchCordinates.size()));
    }

    public boolean contains(Coordinate coordinate){
        return pitchCordinates.contains(coordinate);
    }

}
