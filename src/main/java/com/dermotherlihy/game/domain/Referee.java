package com.dermotherlihy.game.domain;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Referee {

    private static final int FIRST_REMOVAL = 2;
    private static final int MAX_CARDS = 4;

    private ExecutorService executorService;

    private Map<Integer,Coordinate> playerPositions= new ConcurrentHashMap<Integer, Coordinate>();
    private Map<Integer,List<Card>> playerCards= new HashMap();

    private Game game;
    private final CountDownLatch whistle;

    public Referee(CountDownLatch whistle) {
        this.whistle = whistle;
    }

    public void startGame(Game game) {
        this.game = game;

        initialiseExecutorService(game.getPlayers());

        for(Player player : game.getPlayers()){
            playerPositions.put(player.getId(),player.getCurrentPosition());
            playerCards.put(player.getId(),new ArrayList<Card>());
            executorService.execute(player);
        }
        whistle.countDown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
    }

    public synchronized void update(Player player){
        playerPositions.put(player.getId(), player.getCurrentPosition());
        Iterator it = playerPositions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer,Coordinate> pair = (Map.Entry)it.next();
            if(pair.getKey()!= player.getId()){
               if(player.getCurrentPosition().getDistance(pair.getValue()) <= 2){
                   issueYellowCard(player);
                   removePlayerFromGameIfNeeded(player);
               }
            }
        }
    }

    private void issueYellowCard(Player player) {
        playerCards.get(player.getId()).add(Card.YELLOW);
    }

    private void removePlayerFromGameIfNeeded(Player player) {
        if(playerCards.get(player.getId()).size() == FIRST_REMOVAL
                || playerCards.get(player.getId()).size() == MAX_CARDS){
            game.removePlayer(player);
            playerPositions.remove(player.getId());
            player.setPlaying(false);
        }
    }

    public synchronized boolean allowsEntry(Player player) {
        if(playerCards.get(player.getId()).size() != MAX_CARDS){
            playerPositions.put(player.getId(), player.getCurrentPosition());
            game.addPlayer(player);
            return true;
        }
        else{
            System.out.println(String.format("Player %s has left the game", player.getId()));
            checkForGameTermination();
            return false;
        }
    }

    /**
     * Game is terminated when one player left and all other player have 4 yellow cards
     *
     */
    private void checkForGameTermination() {
        if(game.getPlayers().size() == 1){
            Player lastPlayer = game.getPlayers().iterator().next();
            Iterator it = playerCards.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer,List<Card>> pair = (Map.Entry)it.next();
                if(pair.getKey()!= lastPlayer.getId() && pair.getValue().size() != MAX_CARDS){
                    return; //Game must continue
                }
            }
            System.out.println(String.format("Player %s wins", game.getPlayers().iterator().next().getId()));
            executorService.shutdownNow();
            System.exit(0);
        }
    }



    private void initialiseExecutorService(List<Player> players) {
        executorService = Executors.newFixedThreadPool(players.size());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executorService.shutdown();
            }
        });
    }

    protected Map<Integer, List<Card>> getPlayerCards() {
        return playerCards;
    }

    protected void setPlayerPositions(Map<Integer, Coordinate> playerPositions) {
        this.playerPositions = playerPositions;
    }

    protected void setPlayerCards(Map<Integer, List<Card>> playerCards) {
        this.playerCards = playerCards;
    }

    protected void setGame(Game game) {
        this.game = game;
    }
}
