package com.dermotherlihy.game.domain;

/**
 * Created by dermot.herlihy on 25/01/2016.
 */
public class Coordinate {

    private final int length;
    private final int width;

    public Coordinate(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    /*
     *
     * @param other co-ordinate you wish to compare
     * @return distance to the nearest metre
     */
    public double getDistance(Coordinate other){
        return Math.sqrt((length-other.length)*(length-other.length) + (width-other.width)*(width-other.width));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (length != that.length) return false;
        return width == that.width;

    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + width;
        return result;
    }

    @Override
    public String toString() {
        return "{" +length +
                ", " + width +
                '}';
    }
}
