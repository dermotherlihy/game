package com.dermotherlihy.game.domain;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Player implements Runnable{

    private static final int TEN_SECONDS = 10000;
    private CountDownLatch latch = null;
    private boolean playing = false;
    private Pitch pitch;
    private Random randomMoveGenerator = new Random();
    private int id;

    private Coordinate currentPosition;

    private Referee referee;

    public Player(int id, Pitch pitch, CountDownLatch latch) {
        this.latch=latch;
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
        while(playing){
            updatePosition();
        }
        sleep(); //not playing so wait 10 seconds
        if(referee.allowsEntry(this)){
            playing = true;
            playGame();
        }
    }

    private void updatePosition() {
        currentPosition= getNextPosition();
        this.referee.update(this);
        sleep(); //wait for 10 seconds
    }

    /**
     * Assumption : For the purposes of this exercise, I have assume that a player will only move
     * left, right, forward or back 1 meter every 10 seconds. Other directions are excluded.
     */
    private Coordinate getNextPosition() {
        int LEFT = 0;
        int RIGHT = 1;
        int FORWARD = 2;
        int randomMove = randomMoveGenerator.nextInt(4);
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
            latch.await();
            playing = true;
            playGame();
        } catch (InterruptedException e) {
            System.out.println("Interrupted so game over for me");
        }
    }

    private void sleep() {
        try {
            Thread.sleep(TEN_SECONDS);
        } catch(InterruptedException ex) {
        }
    }

    public synchronized void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
