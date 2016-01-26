package com.dermotherlihy.game.domain;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class CoordinateTest {


    @Test
    public void testDistanceCalculateWithCordinatesLessThan2Apart(){

        Coordinate coordinate = new Coordinate(1,1);
        Coordinate other = new Coordinate(2,2);

        Double result = coordinate.getDistance(other);

        Assert.assertTrue(result.intValue() < 2 );
    }

    @Test
    public void testDistanceCalculateWithCordinatesMoreThanUsingLength(){

        Coordinate coordinate = new Coordinate(1,1);
        Coordinate other = new Coordinate(3,2);

        Double result = coordinate.getDistance(other);

        Assert.assertTrue(result.intValue() >= 2 );
    }

    @Test
    public void testDistanceCalculateWithCordinatesMoreThan2UsingWidth(){

        Coordinate coordinate = new Coordinate(1,1);
        Coordinate other = new Coordinate(2,3);

        Double result = coordinate.getDistance(other);

        Assert.assertTrue(result.intValue() >= 2 );
    }
}
