package com.dermotherlihy.game.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dermot.herlihy on 27/01/2016.
 */
public class RefereeTest {

    private final int DEFAULT_PLAYER_ID = 1;
    private final int OTHER_PLAYER_ID = 2;
    private final Coordinate DEFAULT_PLAYER_CORDINATE = new Coordinate(1,1);
    private final Coordinate OTHER_PLAYER_CORDINATE_WITHIN_2_METRES = new Coordinate(0,1);
    private final Coordinate OTHER_PLAYER_CORDINATE_MORE_2_METRES_AWAY = new Coordinate(5,5);


    @Mock
    private Player playerMock;

    @Mock
    private Game gameMock;

    @Mock
    private Coordinate cordinateMock;

    @Mock
    private CountDownLatch whistleMock;

    private Referee testObj;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new Referee(whistleMock);
        testObj.setGame(gameMock);
        when(playerMock.getId()).thenReturn(DEFAULT_PLAYER_ID);
        when(playerMock.getCurrentPosition()).thenReturn(DEFAULT_PLAYER_CORDINATE);
    }

    @Test
    public void testYellowCardIssuedToPlayerWhenAnotherPlayerIsWithinTwoMetres(){
        setDefaultPlayerCards(0);
        setUpOtherPlayerWithCoOrdinatesWithinTwoMetre();

        testObj.update(playerMock);

        Assert.assertTrue(testObj.getPlayerCards().get(playerMock.getId()).size() == 1);
    }

    @Test
    public void testYellowCardNotIssuedWhenOtherPlayersMoreThanTwoMetresAway(){
        setDefaultPlayerCards(0);
        setUpOtherPlayerWithCoOrdinatesMoreTwoMetre();

        testObj.update(playerMock);

        Assert.assertTrue(testObj.getPlayerCards().get(playerMock.getId()).size() == 0);
    }

    @Test
    public void testPlayerRemovedFromGameAfter2YellowCards(){
        setDefaultPlayerCards(1);
        setUpOtherPlayerWithCoOrdinatesWithinTwoMetre();

        testObj.update(playerMock);

        verify(playerMock).setPlaying(false);
        verify(gameMock).removePlayer(playerMock);

    }

    @Test
    public void testPlayerRemovedFromGameAfter4YellowCards(){
        setDefaultPlayerCards(3);
        setUpOtherPlayerWithCoOrdinatesWithinTwoMetre();

        testObj.update(playerMock);

        verify(playerMock).setPlaying(false);
        verify(gameMock).removePlayer(playerMock);

    }

    @Test
    public void testRefereeAllowsPlayerRenterAfterFirstRemoval(){
        setDefaultPlayerCards(2);

        boolean result = testObj.allowsEntry(playerMock);

        verify(gameMock).addPlayer(playerMock);
        Assert.assertTrue(result);

    }

    @Test
    public void testRefereeDoesNotAllowPlayerRenterAfterSecondRemoval(){
        setDefaultPlayerCards(4);

        boolean result = testObj.allowsEntry(playerMock);

        Assert.assertFalse(result);

    }


    private void setDefaultPlayerCards(int numberOfCards) {
        Map<Integer,List<Card>> playerCards= new HashMap();
        playerCards.put(DEFAULT_PLAYER_ID,new ArrayList<Card>());
        for(int i = 0; i <numberOfCards; i++){
            playerCards.get(DEFAULT_PLAYER_ID).add(Card.YELLOW);
        }
        testObj.setPlayerCards(playerCards);
    }

    private void setUpOtherPlayerWithCoOrdinatesWithinTwoMetre() {
        Map<Integer,Coordinate> playerPositions= new ConcurrentHashMap<Integer, Coordinate>();
        playerPositions.put(OTHER_PLAYER_ID,OTHER_PLAYER_CORDINATE_WITHIN_2_METRES);
        testObj.setPlayerPositions(playerPositions);
    }
    private void setUpOtherPlayerWithCoOrdinatesMoreTwoMetre() {
        Map<Integer,Coordinate> playerPositions= new ConcurrentHashMap<Integer, Coordinate>();
        playerPositions.put(OTHER_PLAYER_ID,OTHER_PLAYER_CORDINATE_MORE_2_METRES_AWAY);
        testObj.setPlayerPositions(playerPositions);
    }

}
