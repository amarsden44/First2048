package com.bigfatcaterpillar.first2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 13/03/2018.
 * Used to draw the game grid.
 * - tileDrawMap - contains co-ordinates for drawing tiles.  Uses Tile to store the information,
 *                 but really only the width, height, x1 = left px co-ordinate, y1 = top px co-ordinate
 *                 are setup and used so could re-do this.
 */

public class GameGrid extends View {
    private Paint black = new Paint();
    private int gridSize = 4;
    private Map<Position,Tile> tileDrawMap = new HashMap<Position,Tile>();
    private static int lineWidth = 8;
    private int width = 0;
    private int height = 0;

    public GameGrid(Context context, int gridSize) {
        super(context);

        black.setColor(Color.BLACK);
        black.setStrokeWidth(lineWidth);
        this.gridSize = gridSize;

    }

    @Override
    public void onDraw(Canvas canvas) {

        float startX=1;
        float stopX;
        float startY=1;
        float stopY;

        width = canvas.getWidth();
        height = canvas.getHeight();

        int gridSpacing = Math.min(width, height) / gridSize;
        int boardSize = gridSize * gridSpacing;

        int xOffset = (width - boardSize)/2;
        int yOffset = (height - boardSize)/2;

        //Vertical Grid-lines
        for (int i = 0; i <= gridSize; i++) {

            startX = xOffset + i*gridSpacing;
            startY = yOffset;

            stopX = startX;
            stopY = startY + boardSize;

            canvas.drawLine(startX, startY, stopX, stopY, black);

        }

        //Horizontal Grid-lines
        for (int i = 0; i <= gridSize; i++) {

            startX = xOffset;
            startY = yOffset + i*gridSpacing;

            stopX = startX + boardSize;
            stopY = startY;

            canvas.drawLine(startX, startY, stopX, stopY, black);
        }

    }

    public Map<Position, Tile> getTileDrawMap(int height, int width) {

        int gridSpacing = Math.min(width, height) / gridSize;
        int boardSize = gridSize * gridSpacing;

        int xOffset = (width - boardSize)/2;
        int yOffset = (height - boardSize)/2;


        // set tile positions and values
        for (int y=0;y<gridSize;y++)
            for (int x=0;x<gridSize;x++){
                Position p = new Position(x,y);
                Tile newTile = new Tile();
                newTile.setWidth(gridSpacing-(2*lineWidth));
                newTile.setHeight(gridSpacing-(2*lineWidth));
                newTile.setX1(xOffset+(x*gridSpacing)+lineWidth);
                newTile.setY1(yOffset+(y*gridSpacing)+lineWidth);
                newTile.setValue(-1);
                tileDrawMap.put(p,newTile);
            }
        return tileDrawMap;
    }
}
