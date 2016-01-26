package com.dermotherlihy.game.domain;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Referee {

    private static final Card YELLOW_CARD = new Card(Card.Colour.YELLOW);
    private static final int FIRST_REMOVAL = 2;
    private static final int SECOND_REMOVAL = 4;

    private ExecutorService executorService;

    private Map<Integer,Coordinate> playerPositions= new ConcurrentHashMap<Integer, Coordinate>();
    private Map<Integer,List<Card>> playerCards= new HashMap();

    private Game game;
    private final CountDownLatch whistle;

    public Referee(CountDownLatch whistle) {
        this.whistle = whistle;
    }

    public synchronized void update(Player player){
        playerPositions.put(player.getId(), player.getCurrentPosition());
        Iterator it = playerPositions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer,Coordinate> pair = (Map.Entry)it.next();
            if(pair.getKey()!= player.getId()){
               if(player.getCurrentPosition().getDistance(pair.getValue()) <= 2){
                   issueYellowCard(player);
               }
            }
        }
    }

    private void issueYellowCard(Player player) {
        playerCards.get(player.getId()).add(YELLOW_CARD);
        if(playerMustBeRemoved(player)){
            removePlayerFromGame(player);
        }
    }

    private boolean playerMustBeRemoved(Player player) {
        return playerCards.get(player.getId()).size() == FIRST_REMOVAL || playerCards.get(player.getId()).size() == SECOND_REMOVAL;
    }

    private void removePlayerFromGame(Player player) {
        game.removePlayer(player);
        playerPositions.remove(player.getId());
        player.setPlaying(false);
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
                if(pair.getKey()!= lastPlayer.getId() && pair.getValue().size() !=4){
                    return; //Game must continue
                }
            }
            System.out.println(String.format("Player %s wins", game.getPlayers().iterator().next().getId()));
            executorService.shutdownNow();
            System.exit(0);
        }
    }

    public synchronized boolean allowsEntry(Player player) {
        if(playerCards.get(player.getId()).size() != SECOND_REMOVAL){
            playerPositions.put(player.getId(), player.getCurrentPosition());
            game.addPlayer(player);
            return true;
        }
        else{
            checkForGameTermination();
            return false;
        }
    }

    public void startGame(Game game) {
        this.game = game;
        for(Player player : game.getPlayers()){
            playerPositions.put(player.getId(),player.getCurrentPosition());
            playerCards.put(player.getId(),new ArrayList<Card>());
        }
        executorService = Executors.newFixedThreadPool(game.getPlayers().size());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executorService.shutdown();
            }
        });

        for(Player player : game.getPlayers()){
            executorService.execute(player);
        }
        whistle.countDown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
    }
}
