package com.dermotherlihy.game.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class PitchTest {

    @Test
    public void testPitchCreation(){
        int length = 10;
        int width = 10;
        Pitch pitch = new Pitch(length,width);
        for(int i = 0; i<= length; i++){
            for(int j =0; j<= width; j++){
                Assert.assertTrue(pitch.contains(new Coordinate(i, j)));
            }
        }
    }
}
