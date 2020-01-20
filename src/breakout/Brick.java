package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
 Brick codes:
    1 : Regular Brick
    2 : Hard Brick
    3 : Permanent Brick
    4 : **Hydra** Brick
    5 : Power-up Brick
 */

public class Brick {

    private ImageView brickImage;
    private int WIDTH = 65;
    private int HEIGHT = 30;
    private int hitsAllowed;
    private int popped;
    private int row;
    private boolean powerUp;
    private boolean permanentBrick;
    private boolean isHydra;

    public Brick (String gif_string, int hits, int pop, boolean power, boolean perm){
        this.hitsAllowed = hits;
        this.popped = pop;
        this.powerUp = power;
        this.permanentBrick = perm;
        this.isHydra = false; //Default case

        String style = gif_string;
        Image brickGif = new Image(this.getClass().getClassLoader().getResourceAsStream(style));
        this.brickImage =  new ImageView(brickGif);
        this.brickImage.setFitWidth(WIDTH);
        this.brickImage.setFitHeight(HEIGHT);

    }
    public Brick (String gif_string, int hits, int pop, boolean power, boolean perm, boolean hydra){
        this.hitsAllowed = hits;
        this.popped = pop;
        this.powerUp = power;
        this.permanentBrick = perm;
        this.isHydra = hydra; //Default case

        String style = gif_string;
        Image brickGif = new Image(this.getClass().getClassLoader().getResourceAsStream(style));
        this.brickImage =  new ImageView(brickGif);
        this.brickImage.setFitWidth(WIDTH);
        this.brickImage.setFitHeight(HEIGHT);
    }

    public void setHEIGHT(int h)
    {
        this.HEIGHT = h;
        this.brickImage.setFitHeight(h);
    }
    public void decreaseHits(){
        this.hitsAllowed --;
    }
    public void setWIDTH(int w)
    {
        this.WIDTH = w;
        this.brickImage.setFitWidth(w);
    }

    public ImageView getBrickImage(){
        return this.brickImage;
    }

    public int getWidth(){
        return this.WIDTH;
    }

    public int getHeight(){
        return this.HEIGHT;
    }

    public int getHitsAllowed(){
        return this.hitsAllowed;
    }

    public int getPopped(){
        return this.popped;
    }

    public boolean isPowerUp(){
        return this.powerUp;
    }

    public boolean isPermanentBrick(){
        return this.permanentBrick;
    }
    public boolean isHydra(){ return this.isHydra;}

    public void setBrickX(double x) {this.brickImage.setX(x);}
    public void setBrickY(double y) {this.brickImage.setY(y);}
    public void setRow(int x){ this.row = x;}

    public int getRow(){
        return this.row;
    }

}
