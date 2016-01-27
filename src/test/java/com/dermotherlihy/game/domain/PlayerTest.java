package com.dermotherlihy.game.domain;

import com.dermotherlihy.game.utils.GameRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.when;

/**
 * Created by dermot.herlihy on 26/01/2016.
 */
public class PlayerTest {

    private static final int PLAYER_DIRECTIONS = 4;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    @Mock
    private GameRandom randomMock;
    @Mock
    private Pitch pitchMock;
    @Mock
    private CountDownLatch startWhistleMock;

    private static final Coordinate DEFAULT_CORDINATE = new Coordinate(2,2);

    private Player testObj;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        when(pitchMock.getRandomCoordinate()).thenReturn(DEFAULT_CORDINATE);
        testObj = new Player(1,pitchMock,startWhistleMock);
        testObj.setRandomGenerator(randomMock);
    }

    @Test
    public void testGetNextPositionMovesPlayerLeft(){
        Coordinate expectedResult = new Coordinate(2,1);
        when(randomMock.nextInt(PLAYER_DIRECTIONS)).thenReturn(LEFT);
        when(pitchMock.contains(expectedResult)).thenReturn(true);

        Coordinate result = testObj.getNextPosition();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetNextPositionMovesPlayerRight(){
        Coordinate expectedResult = new Coordinate(2,3);
        when(randomMock.nextInt(PLAYER_DIRECTIONS)).thenReturn(RIGHT);
        when(pitchMock.contains(expectedResult)).thenReturn(true);

        Coordinate result = testObj.getNextPosition();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetNextPositionMovesPlayerUpPitch(){
        Coordinate expectedResult = new Coordinate(3,2);
        when(randomMock.nextInt(PLAYER_DIRECTIONS)).thenReturn(UP);
        when(pitchMock.contains(expectedResult)).thenReturn(true);

        Coordinate result = testObj.getNextPosition();

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetNextPositionMovesPlayerDownPitch(){
        Coordinate expectedResult = new Coordinate(1,2);
        when(randomMock.nextInt(PLAYER_DIRECTIONS)).thenReturn(DOWN);
        when(pitchMock.contains(expectedResult)).thenReturn(true);

        Coordinate result = testObj.getNextPosition();

        Assert.assertEquals(expectedResult, result);
    }
}
