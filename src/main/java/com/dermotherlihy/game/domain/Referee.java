package com.dermotherlihy.game.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Referee {

    private ExecutorService executorService;

    private Map<Integer,Coordinate> playerPositions= new ConcurrentHashMap<Integer, Coordinate>();
    private Map<Integer,Integer> playerCards= new HashMap();

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
                   ejectOrSinBinPlayer(player);
               }
            }
        }
    }

    private void ejectOrSinBinPlayer(Player player) {
        Integer numOfCards= playerCards.get(player.getId());
        if(numOfCards == 0){
            playerCards.put(player.getId(), 1);
            player.setPlaying(false);
            playerPositions.remove(player.getId());
        }else{
            player.setPlaying(false);
            game.removePlayer(player);
            checkForGameTermination();
        }
    }

    private void checkForGameTermination() {
        if(game.getPlayers().size() == 1){

            System.out.println(String.format("Player %s wins", game.getPlayers().iterator().next().getId()));
            executorService.shutdownNow();
            System.exit(0);
        }
    }

    public synchronized boolean allowsEntry(Player player) {
        if(playerCards.get(player.getId()) !=null && playerCards.get(player.getId()) < 2){
            playerPositions.put(player.getId(),player.getCurrentPosition());
            return true;
        }
        return false;
    }

    public void startGame(Game game) {
        this.game = game;
        for(Player player : game.getPlayers()){
            playerPositions.put(player.getId(),player.getCurrentPosition());
            playerCards.put(player.getId(),0);
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
