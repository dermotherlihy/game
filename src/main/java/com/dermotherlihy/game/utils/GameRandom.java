package com.dermotherlihy.game.utils;

import java.util.Random;

/**
 * Created by dermot.herlihy on 27/01/2016.
 */
public class GameRandom {

    private final Random random = new Random();


    public int nextInt(int boundary) {
        return random.nextInt(boundary);
    }
}
