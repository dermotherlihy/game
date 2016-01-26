package com.dermotherlihy.game.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Game {

    private List<Player> players;
    private Referee referee;


    public Game(List<Player> players, Referee referee) {
        this.players = players;
        this.referee = referee;
        for(Player player : players){
            player.setReferee(this.referee);
        }
    }

    public synchronized List<Player> getPlayers() {
        return new ArrayList<Player>(players);
    }

    /**
     * Removes player from game
     * @param player
     */
    public synchronized void removePlayer(Player player){
        players.remove(player);
    }
    /**
     * Removes player from game
     * @param player
     */
    public synchronized void addPlayer(Player player){
        players.add(player);
    }

}
