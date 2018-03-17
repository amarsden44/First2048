package com.bigfatcaterpillar.first2048;

import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {
    private Board board;
    float px = 21;
    private float x1,x2;
    private float y1;
    private boolean gameStarted = false;
    static final String STORE_GAMESTATE = "store_state";
    static final String STORE_GAMESTARTED = "store_gamestarted";


    private static int gridSize = 4;
    GameGrid gameGrid;
    ConstraintLayout newView;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        board = new Board();
        if (savedInstanceState != null)
        {
            GameState gameState = (GameState) savedInstanceState.getSerializable(STORE_GAMESTATE);
            board.setGameState(gameState);
            gameStarted = savedInstanceState.getBoolean(STORE_GAMESTARTED);
        }

        View v = findViewById(R.id.activity_main);

        v.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            @Override
            public void onSwipeTop() {
                Log.d(TAG, "onSwipeTop(before): " + board.toString());
                if ((board.swipeUp(MainActivity.this, newView))||
                    (board.getGameState().getNumberOfTiles() == board.getGameState().getMaxTiles())) {
                    Log.d(TAG, "onSwipeTop(after): " + board.toString());
                    if (board.doMove(MainActivity.this, newView)) {
                        //board.drawTiles(MainActivity.this, newView);
                    } else {
                        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onSwipeRight() {
                Log.d(TAG, "onSwipeRight: " + board.toString());
                if ((board.swipeRight(MainActivity.this, newView))||
                    (board.getGameState().getNumberOfTiles() == board.getGameState().getMaxTiles())){
                    if (board.doMove(MainActivity.this, newView)) {
                        //board.drawTiles(MainActivity.this, newView);
                    } else {
                        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onSwipeLeft() {
                if ((board.swipeLeft(MainActivity.this, newView))||
                    (board.getGameState().getNumberOfTiles() == board.getGameState().getMaxTiles())) {
                    if (board.doMove(MainActivity.this, newView)) {
                        //board.drawTiles(MainActivity.this, newView);
                    } else {
                        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onSwipeBottom() {
                Log.d(TAG, "onSwipeBottom (before): " + board.toString());
                if ((board.swipeSwipeDown(MainActivity.this, newView))||
                    (board.getGameState().getNumberOfTiles() == board.getGameState().getMaxTiles())) {
                    Log.d(TAG, "onSwipeBottom (after): " + board.toString());
                    if (board.doMove(MainActivity.this, newView)) {
                        Log.d(TAG, "onSwipeBottom (after doMove): " + board.toString());
                        //board.drawTiles(MainActivity.this, newView);
                    } else {
                        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        gameGrid = new GameGrid(this, gridSize);
        newView = findViewById(R.id.constraintLayout);
        newView.addView(gameGrid);



        newView.post(new Runnable() {
            @Override
            public void run() {
                // set up width and height of board
                int h = newView.getHeight(); //height is ready
                int w = newView.getWidth();
                board.setTileDrawMap(gameGrid.getTileDrawMap(h,w));
                if (gameStarted){
                    board.reDrawTiles(MainActivity.this, newView);
                }
                else {
                    board.doMove(MainActivity.this, newView);
                    board.doMove(MainActivity.this, newView);
                    gameStarted = true;
                }
                //board.drawTiles(MainActivity.this, newView);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu,menu);
        // add commands to set values in menu
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean changed = true;
        switch(id){
            case R.id.mnuRestartGame:
                board = new Board();
                int h = newView.getHeight(); //height is ready
                int w = newView.getWidth();
                board.setTileDrawMap(gameGrid.getTileDrawMap(h,w));
                newView.removeAllViews();
                newView.addView(gameGrid);
                board.doMove(MainActivity.this, newView);
                board.doMove(MainActivity.this, newView);
                gameStarted = true;
                break;

            default:
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
//                Toast.makeText(this,"Down action",Toast.LENGTH_SHORT).show();
//                Log.d(TAG,"Action was DOWN");
                x1 = event.getX();
                y1 = event.getY();
                return true;
            case (MotionEvent.ACTION_MOVE) :
//                Log.d(TAG,"Action was MOVE");
               return true;
            case (MotionEvent.ACTION_UP) :
//                Log.d(TAG,"Action was UP");
                x2 = event.getX();

//                float deltaX = x2 - x1;
//                if (Math.abs(deltaX) > MIN_DISTANCE)
//                {
//                    // Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
//                }
//                else
//                {
//                    // consider as something else - a screen tap for example
//                }

                return true;
//            case (MotionEvent.ACTION_CANCEL) :
//                Log.d(TAG,"Action was CANCEL");
//                return true;
//            case (MotionEvent.ACTION_OUTSIDE) :
//                Log.d(TAG,"Movement occurred outside bounds " +
//                        "of current screen element");
//                return true;
            default :
                return super.onTouchEvent(event);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        GameState gameState = board.getGameState();
        outState.putSerializable(STORE_GAMESTATE, gameState);
        outState.putBoolean(STORE_GAMESTARTED, gameStarted);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }
}
