package com.bigfatcaterpillar.first2048;

/**
 * Created by Andrew on 13/03/2018.
 * This is used by GameState to store X,Y grid position, for use in HASH array
 * e.g. if 5x5 grid, position x=0,y=0 is top left. position x=4, y=4 is bottom right.
 */

public class Position {
    private int x1;
    private int y1;

    public Position() {
        this.initPosition(0,0);
    }

    public Position(int x1, int y1) {
        initPosition(x1, y1);
    }

    private void initPosition(int x1, int y1){
        this.x1 = x1;
        this.y1 = y1;
    }

    public void setPosition(int x, int y)
    {
        initPosition(x,y);
    }

    public int getX()
    {
        return x1;
    }

    public int getY()
    {
        return y1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        Position position = (Position) o;

        if (x1 != position.x1) return false;
        return y1 == position.y1;
    }

    @Override
    public int hashCode() {
        int result = x1;
        result = 31 * result + y1;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("X=").append(x1).append(",Y=").append(y1);
        return sb.toString();
    }
}
