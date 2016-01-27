package com.dermotherlihy.game.domain;

import com.dermotherlihy.game.utils.GameRandom;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Player implements Runnable{

    private static final int ONE_SECOND = 1000;
    private static final int TEN_SECONDS = 10000;
    private CountDownLatch startWhistle = null;
    private AtomicBoolean playing = new AtomicBoolean();
    private Pitch pitch;
    private GameRandom randomGenerator = new GameRandom();
    private int id;

    private Coordinate currentPosition;

    private Referee referee;

    public Player(int id, Pitch pitch, CountDownLatch startWhistle) {
        this.startWhistle=startWhistle;
        this.pitch=pitch;
        currentPosition = pitch.getRandomCoordinate();
        this.id = id;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Coordinate getCurrentPosition() {
        return currentPosition;
    }

    public int getId() {
        return id;
    }


    private void playGame(){
        while(playing.get()){
            updatePosition();
        }
        sleep(TEN_SECONDS); //not playing so wait 10 seconds
        if(referee.allowsEntry(this)){
            playing.set(true);
            playGame();
        }
    }

    private void updatePosition() {
        currentPosition= getNextPosition();
        this.referee.update(this);
        sleep(ONE_SECOND); //wait for 1 seconds
    }

    /**
     * Assumption : For the purposes of this exercise, I have assume that a player will only move
     * left, right, forward or back 1 meter every 10 seconds. Other directions are excluded.
     */
    protected Coordinate getNextPosition() {
        int LEFT = 0;
        int RIGHT = 1;
        int FORWARD = 2;
        int randomMove = randomGenerator.nextInt(4);
        Coordinate newPosition;
        //Step left
        if(randomMove == LEFT){
             newPosition = new Coordinate(currentPosition.getLength(),currentPosition.getWidth()-1);
        }
        //Step right
        else if(randomMove == RIGHT){
             newPosition = new Coordinate(currentPosition.getLength(),currentPosition.getWidth()+1);
        }
        //Step forward
        else if(randomMove == FORWARD){
             newPosition = new Coordinate(currentPosition.getLength()+1,currentPosition.getWidth());
        }
        //Step back
        else{
             newPosition = new Coordinate(currentPosition.getLength()-1,currentPosition.getWidth());
        }
        return pitch.contains(newPosition) ? newPosition : getNextPosition();
    }

    public void run() {
        try {
            startWhistle.await();
            playing.set(true);
            playGame();
        } catch (InterruptedException e) {
            System.out.println("Interrupted so game over for me");
        }
    }

    private void sleep(int millesconds) {
        try {
            Thread.sleep(millesconds);
        } catch(InterruptedException ex) {
        }
    }

    public void setPlaying(boolean update) {
         playing.set(update);
    }

    protected void setRandomGenerator(GameRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
    }
}
