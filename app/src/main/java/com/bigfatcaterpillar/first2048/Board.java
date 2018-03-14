package com.bigfatcaterpillar.first2048;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Map;
import java.util.Random;

/**
 * Created by Andrew on 12/03/2018.
 * Manages drawing tiles on board and doing moves on board.
 * GameState is called to get tiles and values of tiles.
 */


public class Board {
    private Random random = new Random();
    private Map<Position,Tile> tileDrawMap;
    private static final String TAG = "Board";
    private GameState gameState;

    public Board() {
        // setup board with empty tiles
        gameState = new GameState();

    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean doMove(Context context, ConstraintLayout view)
    {
        return addTile(context, view);

    }

    public boolean reDrawTiles(Context context, ConstraintLayout view){
        // this method goes through the existing tile list and creates a new
        // image for existing tiles and draws it on the screen
        for(int y=0;y<GameState.tilesY;y++){
            for(int x=0;x<GameState.tilesX;x++){
                drawTile(context, view, x, y, gameState.getCurrTiles()[x][y]);
            }
        }
        return true;
    }

    private void drawTile(Context context, ConstraintLayout view, int x, int y, Tile currTile){

        // if tile doesn't have a value, then we don't draw a tile, as there isn't one!
        if (currTile.getValue() == 0)
            return;

        // Get location for where to draw tile, and tile size details
        Position position = new Position(x,y);
        Tile tilePos = this.getTileDrawMap().get(position);

        // create new image view
        ImageView imageView;
        imageView = new ImageView(context);
        int resourceId = context.getResources().getIdentifier(
                 "num" + Integer.toString(currTile.getValue()),
                 "mipmap",
                 context.getPackageName());
        imageView.setAdjustViewBounds(true);
        imageView.setX(tilePos.getX1());
        imageView.setY(tilePos.getY1());
        imageView.setMaxHeight(tilePos.getHeight());
        imageView.setMaxWidth(tilePos.getWidth());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageView.setImageResource( resourceId );
        int viewId = View.generateViewId();
        imageView.setId(viewId);

        currTile.setViewId(viewId);
        currTile.setImageView(imageView);

        // and finally add it to the screen
        view.addView(imageView);

    }

    private boolean addTile(Context context, ConstraintLayout view)
    {
        // Finds a free tile and sets it to a value
        if (gameState.getNumberOfTiles() == GameState.maxTiles){
            // will return false if no moves left, true if a move is possible
            return checkIfMovePossible();
            //return false;
        }

        gameState.setNumberOfTiles(gameState.getNumberOfTiles()+1);
        // lets start in a random place
        int x1 = 0;
        int y1 = 0;
        int n = random.nextInt(GameState.maxTiles);
        if (n != 0) {
            x1 = n % GameState.tilesX;
            y1 = n / GameState.tilesY;
        }

        // move through tiles looking for an unused one
        for(int y = 0; y<GameState.tilesY; y++){
            for(int x = 0;x<GameState.tilesX;x++){
                if (gameState.getCurrTiles()[x1][y1].getValue() == 0) {
                    // free tile
                    gameState.getCurrTiles()[x1][y1].setValue((random.nextInt(2)+1)*2);
                    Position position = new Position(x1,y1);
                    Tile tilePos = this.getTileDrawMap().get(position);
                    ImageView imageView = new ImageView(context);

                    int resourceId = context.getResources().getIdentifier(
                            "num" + Integer.toString(gameState.getCurrTiles()[x1][y1].getValue()),
                            "mipmap",
                            context.getPackageName());

                    imageView.setAdjustViewBounds(true);
                    imageView.setX(tilePos.getX1());
                    imageView.setY(tilePos.getY1());
                    imageView.setMaxHeight(tilePos.getHeight());
                    imageView.setMaxWidth(tilePos.getWidth());
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    imageView.setImageResource( resourceId );
                    int viewId = View.generateViewId();
                    gameState.getCurrTiles()[x1][y1].setViewId(viewId);
                    gameState.getCurrTiles()[x1][y1].setImageView(imageView);
                    imageView.setId(viewId);
                    view.addView(imageView);
                    return true;
                }
                else
                {
                    x1 +=1;
                    if (x1 == GameState.tilesX) x1 = 0;
                }
            }
            y1 += 1;
            if (y1 == GameState.tilesX) y1 = 0;
        }

        return true;
    }

    private void moveTile(Context context, ConstraintLayout view, Position currPos, Position newPos){
        Tile currTile = gameState.getCurrTiles()[currPos.getX()][currPos.getY()];
        Tile newTile = gameState.getCurrTiles()[newPos.getX()][newPos.getY()];

        if ((currTile.getValue() == 0) && (newTile.getValue() == 0)){
            // no change, so do nothing

        }
        else if ((currTile.getValue() == 0) && (newTile.getValue() != 0))
        {
            // current tile doesn't have a value so we don't move it
        }
        else if ((currTile.getValue() != 0) && (newTile.getValue() == 0)) {
           // need to move curr tile to new tile position and repoint image location
           // on curr tile to new location
           Tile tilePos = this.getTileDrawMap().get(newPos);
           ImageView tileImage = currTile.getImageView();
           tileImage.setX(tilePos.getX1());
           tileImage.setY(tilePos.getY1());
           // below updates the new tile in array with updated currTile.
           newTile.setAll(currTile);
           currTile.reset();


        }
        else if ((currTile.getValue() != 0) && (newTile.getValue() != 0)) {
            // update existing tile image, but only if new number is needed
            if (currTile.getValue() != newTile.getValue())
                // no change required as they can't merge
                return;

            Log.d(TAG, "moveTile (before replace move): " + this.toString());
            // need to move curr tile to new tile position and repoint image location
           // on curr tile to new location
           Tile tilePos = this.getTileDrawMap().get(newPos);
           ImageView tileImage = currTile.getImageView();
           tileImage.setX(tilePos.getX1());
           tileImage.setY(tilePos.getY1());

           ImageView removeImage = newTile.getImageView();
           // below updates the new tile with updated currTile location change.
           newTile.setAll(currTile);
           currTile.reset();
           gameState.setNumberOfTiles(gameState.getNumberOfTiles()-1);
           if (removeImage != null){
               view.removeView(removeImage);
           }

           // We have a merge so need to update tile image to new number
           newTile.setValue(newTile.getValue()*2);
           int resourceId = context.getResources().getIdentifier(
                   "num" + Integer.toString(newTile.getValue()),
                   "mipmap",
                   context.getPackageName());
           newTile.getImageView().setImageResource(resourceId);
           view.invalidate();
           Log.d(TAG, "moveTile (after replace move): " + this.toString());

        }

    }

    private boolean checkIfMovePossible()
    {
        // loop through and see if there are any tiles next to each other that are the same
        for(int y=0;y<GameState.tilesY;y++){
            for(int x=0;x<GameState.tilesX;x++){
                // check left
                if ((x > 0 ) && (gameState.getCurrTiles()[x][y].getValue() == gameState.getCurrTiles()[x-1][y].getValue()))
                    return true;
                // check right
                if ((x < GameState.tilesX-2 ) && (gameState.getCurrTiles()[x][y].getValue() == gameState.getCurrTiles()[x+1][y].getValue()))
                    return true;
                // check above
                if ((y > 0 ) && (gameState.getCurrTiles()[x][y].getValue() == gameState.getCurrTiles()[x][y-1].getValue()))
                    return true;
                // check below
                if ((y < GameState.tilesY-2 ) && (gameState.getCurrTiles()[x][y].getValue() == gameState.getCurrTiles()[x][y+1].getValue()))
                    return true;

            }
        }
        return false;
    }



    @Override
    public String toString() {
        return gameState.toString();

    }

    public Map<Position, Tile> getTileDrawMap() {
        return tileDrawMap;
    }

    public void setTileDrawMap(Map<Position, Tile> tileDrawMap) {
        this.tileDrawMap = tileDrawMap;
    }

    public boolean swipeLeft(Context context, ConstraintLayout view){
        Position currPos = new Position(0,0);
        Position nextPos = new Position(0,0);
        int x0, x1;
        boolean moved = false;

        for (int y=0;y<GameState.tilesY;y++)
            for (int x=0;x<GameState.tilesX;x++)
            {
                if (gameState.getCurrTiles()[x][y].getValue() != 0) {
                    // check if we need to move the tile
                    x1 = x-1;
                    x0 = x;
                    while (x1 > -1) {
                        // move the tile if tile above it is empty or has same value
                        if ((gameState.getCurrTiles()[x1][y].getValue() == 0) || (gameState.getCurrTiles()[x1][y].getValue() == gameState.getCurrTiles()[x0][y].getValue()))
                        {
                            currPos.setPosition(x0, y);
                            nextPos.setPosition(x1, y);
                            this.moveTile(context, view, currPos, nextPos);
                            x0 = x1;
                            moved = true;
                        }
                        else if ((gameState.getCurrTiles()[x1][y].getValue() != gameState.getCurrTiles()[x0][y].getValue())){
                            // exit while loop as we have hit a tile next to us that isn't same value
                            break;
                        }
                        x1 -= 1;
                    }
                }

            }

        return moved;
    }

    public boolean swipeRight(Context context, ConstraintLayout view){
        Position currPos = new Position(0,0);
        Position nextPos = new Position(0,0);
        int x0, x1;
        boolean moved = false;

        for (int y=GameState.tilesY-1;y>-1;y--)
            for (int x=GameState.tilesX-1;x>-1;x--)
            {
                if (gameState.getCurrTiles()[x][y].getValue() != 0) {
                    // check if we need to move the tile
                    x1 = x+1;
                    x0 = x;
                    while (x1 < GameState.tilesX) {
                        // move the tile if tile above it is empty or has same value
                        if ((gameState.getCurrTiles()[x1][y].getValue() == 0) || (gameState.getCurrTiles()[x1][y].getValue() == gameState.getCurrTiles()[x0][y].getValue()))
                        {
                            currPos.setPosition(x0, y);
                            nextPos.setPosition(x1, y);
                            this.moveTile(context, view, currPos, nextPos);
                            x0 = x1;
                            moved = true;
                        }
                        else if ((gameState.getCurrTiles()[x1][y].getValue() != gameState.getCurrTiles()[x0][y].getValue())){
                            // exit while loop as we have hit a tile next to us that isn't same value
                            break;
                        }
                        x1 += 1;
                    }
                }

            }
        return moved;

    }

    public boolean swipeUp(Context context, ConstraintLayout view){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            TransitionManager transitionManager = new TransitionManager();
            transitionManager.beginDelayedTransition(view);

        }

        Position currPos = new Position(0,0);
        Position nextPos = new Position(0,0);
        int y0, y1;
        boolean moved = false;

        for (int x=0;x<GameState.tilesX;x++)
            for (int y=0;y<GameState.tilesY;y++)
            {
                if (gameState.getCurrTiles()[x][y].getValue() != 0) {
                    // check if we need to move the tile
                    y0 = y;
                    y1 = y-1;
                    while (y1 > -1) {
                        // move the tile if tile above it is empty or has same value
                        if ((gameState.getCurrTiles()[x][y1].getValue() == 0) || (gameState.getCurrTiles()[x][y1].getValue() == gameState.getCurrTiles()[x][y0].getValue()))
                        {
                            currPos.setPosition(x, y0);
                            nextPos.setPosition(x, y1);
                            this.moveTile(context, view, currPos, nextPos);
                            y0 = y1;
                            moved = true;
                        }
                        else if ((gameState.getCurrTiles()[x][y1].getValue() != gameState.getCurrTiles()[x][y0].getValue())){
                            // exit while loop as we have hit a tile next to us that isn't same value
                            break;
                        }
                        y1 -= 1;
                    }
                }

            }
        return moved;
    }

    public boolean swipeSwipeDown(Context context, ConstraintLayout view){
        Position currPos = new Position(0,0);
        Position nextPos = new Position(0,0);
        int y0, y1;
        boolean moved = false;

        for (int x=GameState.tilesX-1;x>-1;x--)
            for (int y=GameState.tilesY-1;y>-1;y--)
            {
                if (gameState.getCurrTiles()[x][y].getValue() != 0) {
                    // check if we need to move the tile
                    y0 = y;
                    y1 = y+1;
                    while (y1 < GameState.tilesY) {
                        // move the tile if tile above it is empty or has same value
                        if ((gameState.getCurrTiles()[x][y1].getValue() == 0) || (gameState.getCurrTiles()[x][y1].getValue() == gameState.getCurrTiles()[x][y0].getValue()))
                        {
                            currPos.setPosition(x, y0);
                            nextPos.setPosition(x, y1);
                            this.moveTile(context, view, currPos, nextPos);
                            y0 = y1;
                            moved = true;
                        }
                        else if ((gameState.getCurrTiles()[x][y1].getValue() != gameState.getCurrTiles()[x][y0].getValue())){
                            // exit while loop as we have hit a tile next to us that isn't same value
                            break;
                        }
                        y1 += 1;
                    }
                }

            }
        return moved;
    }



}
