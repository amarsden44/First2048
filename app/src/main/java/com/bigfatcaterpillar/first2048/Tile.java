package com.bigfatcaterpillar.first2048;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Andrew on 12/03/2018.
 * Used to store tile information - eg value, location on screen, image to use.
 * Should split out into to classes Tile and TileLoc classes.
 * As GameState really only uses value, viewId(?) and imageView (imageView will store x1, y1, width and height)
 * And GameGrid tileDrawMap uses the x1, y1, width, height values.
 *
 */

public class Tile implements Serializable{
    private int x1;
    private int y1;
    private int width;
    private int height;
    private String image;
    private int value;
    private int viewId;
    private transient ImageView imageView;

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Tile(){
        initTile(0,0,0,0,"",0);
    }

    public Tile(int x1, int y1, int width, int height, String image, int value) {
        initTile(x1,y1,width,height,image,value);
    }

    private void initTile(int x1, int y1, int width, int height, String image, int value){
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;
        this.image = image;
        this.value = value;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    protected Object clone() {
        Tile newTile = new Tile(this.x1,this.y1,this.width,this.height,this.image,this.value);

        newTile.viewId = this.viewId;
        newTile.imageView = this.imageView;
        return newTile;
    }

    public void setAll(Tile newTile)
    {
        this.imageView = newTile.getImageView();
        this.image = newTile.getImage();
        this.value = newTile.getValue();
        this.height = newTile.getHeight();
        this.width = newTile.getWidth();
        this.viewId = newTile.getViewId();
        this.x1 = newTile.getX1();
        this.y1 = newTile.getY1();
    }
    public void reset()
    {
        this.imageView = null;
        this.image = "";
        this.value = 0;
        this.height = 0;
        this.width = 0;
        this.viewId = 0;
        this.x1 = 0;
        this.y1 = 0;
    }


}
