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
    public static int WIDTH = 65;
    public static int HEIGHT = 30;
    private int hitsAllowed;
    private int popped;
    private boolean powerUp;
    private boolean permanentBrick;

    public Brick (String gif_string, int hits, int pop, boolean power, boolean perm){
        this.hitsAllowed = hits;
        this.popped = pop;
        this.powerUp = power;
        this.permanentBrick = perm;

        String style = gif_string;
        Image brickGif = new Image(this.getClass().getClassLoader().getResourceAsStream(style));
        this.brickImage =  new ImageView(brickGif);
        this.brickImage.setFitWidth(WIDTH);
        this.brickImage.setFitHeight(HEIGHT);

    }
//    public Brick (int hitsAllowed){
//        String style = new String();
//        popped = 0;
//        powerUp = false;
//        permanentBrick = false;
//        switch (brickCode){
//            case 1: {
//                hitsAllowed = 1;
//                style = "brick3.gif";
//            }
//            case 2: {
//                hitsAllowed = 3;
//                style = "brick5.gif";
//            }
//            case 3: {
//                permanentBrick = true;
//                style = "brick7.gif";
//            }
//            case 5: {
//                hitsAllowed = 1;
//                powerUp = true;
//                style = "brick10.gif";
//            }
//        }
//        Image brickGif = new Image(this.getClass().getClassLoader().getResourceAsStream(style));
//        ImageView brickView = new ImageView(brickGif);
//
//        this.brickImage = brickView;
//        this.brickImage.setFitWidth(WIDTH);
//        this.brickImage.setFitHeight(HEIGHT);
//    }

    public void setHEIGHT(int h) {
        this.HEIGHT = h;
    }
    public void decreaseHits(){
        this.hitsAllowed --;
    }
    public void setWIDTH(int w){
        this.WIDTH = w;
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

    public void setBrickX(double x) {this.brickImage.setX(x);}
    public void setBrickY(double y) {this.brickImage.setY(y);}

}
