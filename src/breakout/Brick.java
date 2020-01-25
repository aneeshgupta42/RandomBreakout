package breakout;
/*
good class because: is active, makes bricks,
and provides a layer of abstraction between main and brick object
Does not have a lot of dupicated code
 */
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

    /***
     * setter method for height
     * @param h
     */
    public void setHEIGHT(int h)
    {
        this.HEIGHT = h;
        this.brickImage.setFitHeight(h);
    }
    /***
     * changer method for hits decrement
     */
    public void decreaseHits(){
        this.hitsAllowed --;
    }
    /***
     * setter method for height
     * @param w
     */
    public void setWIDTH(int w)
    {
        this.WIDTH = w;
        this.brickImage.setFitWidth(w);
    }
    /***
     * getter method for image
     */
    public ImageView getBrickImage(){
        return this.brickImage;
    }
    /***
     * getter method for width
     */
    public int getWidth(){
        return this.WIDTH;
    }
    /***
     * getter method for height
     */
    public int getHeight(){
        return this.HEIGHT;
    }
    /***
     * getter method for hits allowed
     */
    public int getHitsAllowed(){
        return this.hitsAllowed;
    }
    /***
     * getter method for popped bool
     */
    public int getPopped(){
        return this.popped;
    }
    /***
     * getter method for powerup bool
     */
    public boolean isPowerUp(){
        return this.powerUp;
    }
    /***
     * getter method for permanent bool
     */
    public boolean isPermanentBrick(){
        return this.permanentBrick;
    }
    /***
     * getter method for hydra bool
     */
    public boolean isHydra(){
        return this.isHydra;
    }
    /***
     * setter method for brick x coord
     */
    public void setBrickX(double x) {
        this.brickImage.setX(x);
    }
    /***
     * setter method for brick y coord
     */
    public void setBrickY(double y) {
        this.brickImage.setY(y);
    }
    /***
     * setter method for row
     */
    public void setRow(int x){
        this.row = x;
    }

    /***
     * getter method for row
     */
    public int getRow(){
        return this.row;
    }

}
