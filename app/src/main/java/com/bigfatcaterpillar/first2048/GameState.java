package com.bigfatcaterpillar.first2048;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

/**
 * Created by Andrew on 14/03/2018.
 * Used to store what tiles are currently on the Board and where on the board they are.
 */

public class GameState implements Serializable {
    private int numberOfTiles = 0;
    public static int tilesX = 4;
    public static int tilesY = 4;
    public static int maxTiles = tilesX * tilesY;
    private static final String TAG = "GameState";
    private Tile[][] currTiles = new Tile[tilesX][tilesY];;

    public GameState() {

        for(int y=0;y<tilesY;y++){
            for(int x=0;x<tilesX;x++){
                Tile tile = new Tile(0,0,0,0,"",0);
                currTiles[x][y] = tile;
            }
        }
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public void setNumberOfTiles(int numberOfTiles) {
        this.numberOfTiles = numberOfTiles;
    }

    public static int getTilesX() {
        return tilesX;
    }


    public static int getTilesY() {
        return tilesY;
    }


    public int getMaxTiles() {
        return maxTiles;
    }


    public Tile[][] getCurrTiles() {
        return currTiles;
    }

    public void setCurrTiles(Tile[][] currTiles) {
        this.currTiles = currTiles;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for(int y=0;y<tilesY;y++) {
            for (int x = 0; x < tilesX; x++) {
                sb.append(x);
                sb.append("=");
                sb.append(currTiles[x][y].getValue());
                sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
