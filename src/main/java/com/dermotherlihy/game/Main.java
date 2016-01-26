package com.dermotherlihy.game;

import com.dermotherlihy.game.domain.Game;
import com.dermotherlihy.game.domain.Pitch;
import com.dermotherlihy.game.domain.Player;
import com.dermotherlihy.game.domain.Referee;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Main {

    private static final int PITCH_LENGTH = 100;
    private static final int PITCH_WIDTH = 100;
    private static final int NUMBER_OF_PLAYERS = 10;

    public static void main(String args[]){
         Pitch pitch = new Pitch(PITCH_LENGTH,PITCH_WIDTH);
         CountDownLatch whistle = new CountDownLatch(1);
         List<Player> playerList = createPlayers(pitch, whistle);
         Referee referee = new Referee(whistle);
         Game game = new Game(playerList, referee);
         referee.startGame(game);

    }

    private static List<Player> createPlayers(Pitch pitch, CountDownLatch whistle) {
        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i< NUMBER_OF_PLAYERS; i++){
            players.add(new Player(i,pitch, whistle));
        }
        return players;
    }
}
