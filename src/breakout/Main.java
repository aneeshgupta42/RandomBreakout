package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 * This code is inspired by:
 * The basic example JavaFX program for the first lab.
 * Which was authored by: Robert Duvall
 *
 * @author Aneesh
 */
public class Main extends Application {
    public static final String TITLE = "Game - ag468";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 120;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.WHITESMOKE;
    public static final Paint altBCKGRND = Color.SALMON;

    private String BOUNCER_IMAGE = "ball.gif";
    private String PADDLE_IMAGE = "paddle.gif";
    private int BOUNCER_SPEED = 300;

    public static final int MOVER_WIDTH = 80;
    public static final int MOVER_HEIGHT = 20;
    private int mover_speed = 35;
    private int ball_y_direction = 1;
    private int ball_x_direction = 1;


    private ArrayList<Brick> brickList;

    // some things needed to remember during game
    private Stage stage;
    Timeline animation = new Timeline();
    private Scene gameScene;
    private ImageView myBouncer;
    private Rectangle myPaddle;
    private Group root;

    private Label livesDisp;
    private Label scoreDisp;
    private Label promptDisp;
    private Label powerDisp;

    private int currentLevel = 1;
    private int currentLives;
    private int gameScore = 0;
    private int fullBottomActivated = 0;

    private boolean gameLost;
    private boolean randomGame;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) throws FileNotFoundException {
        // attach scene to the stage and display it
        gameScene = setupLevel(SIZE, SIZE, BACKGROUND, currentLevel);
        Scene startSplash = startScreen();
        stage.setTitle(TITLE);
        stage.setScene(startSplash);
        stage.show();
        this.stage = stage;
    }

    /***
     * For the start screen
     * @return VBox with Bricks-qualities key
     */
    private VBox GetBricksKey(){
        Image regular = new Image(this.getClass().getClassLoader().getResourceAsStream("brick3.gif"));
        Label reg = new Label("Regular Brick - 1 Hit");
        reg.setGraphic(new ImageView(regular));

        Image hard = new Image(this.getClass().getClassLoader().getResourceAsStream("brick5.gif"));
        Label h = new Label("Hard Brick - 2+ Hits");
        h.setGraphic(new ImageView(hard));

        Image permanent = new Image(this.getClass().getClassLoader().getResourceAsStream("brick7.gif"));
        Label perm = new Label("Permanent Brick - Infinite hits");
        perm.setGraphic(new ImageView(permanent));

        Image power = new Image(this.getClass().getClassLoader().getResourceAsStream("brick10.gif"));
        Label pow = new Label("PowerUp Brick - 1 Hit, and gives power");
        pow.setGraphic(new ImageView(power));

        Image hydra = new Image(this.getClass().getClassLoader().getResourceAsStream("brick2.gif"));
        Label hyd = new Label("Hydra Brick - Splits into 2 Children (1 Hit each)");
        hyd.setGraphic(new ImageView(hydra));
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(reg, h, perm, pow, hyd);
        vbox.setLayoutX(90); vbox.setLayoutY(270);
        return vbox;
    }

    /***
     * Starting splash screen with
     * name, instructions, start button, etc.
     * @return Scene startScene
     */
    private Scene startScreen(){
        Button cont = new Button("Start Game");
        cont.setLayoutX(210); cont.setLayoutY(450);
        cont.setOnAction(e -> advanceScene(gameScene));

        Label Header = new Label("?Random Breakout¿");
        Header.setFont(new Font("Garamond", 30));
        Header.setTextFill(Color.DARKCYAN);
        Header.setLayoutX(140); Header.setLayoutY(10);

        Rectangle hypno = new Rectangle(50, 50);
        Image hypnoImage = new Image(this.getClass().getClassLoader().getResourceAsStream("hypno.gif"));
        ImagePattern hypnoImagePattern = new ImagePattern(hypnoImage);
        hypno.setFill(hypnoImagePattern);
        hypno.setLayoutX(230); hypno.setLayoutY(50);

        Label rules = new Label("GAME RULES\n" +
                "\t 1. Use arrow keys to move paddle to guide ball to pop bricks\n" +
                "\t 2. You get three lives per level\n" +
                "\t 3. There are different types of bricks in this game\n" +
                "\t 4. Use powerups such as Stretch, Swift, or Life+ \n"+
                "\t 4. In the middle of the level controls change (such as arrow keys)\n"+
                "\t\t Adapt to this change and finish the level!");

        rules.setLayoutX(40); rules.setLayoutY(120);
        rules.setFont(new Font("Garamond", 15));
        VBox vbox = GetBricksKey();
        Group startRoot = new Group();
        startRoot.getChildren().addAll(Header,hypno, rules, cont, vbox);
        return new Scene(startRoot, SIZE+100, SIZE+100, BACKGROUND);
    }

    /***
     * advances to the nextScene, by
     * putting it on the Stage, and
     * creating a new Timeline
     * @param nextScene
     */
    private void advanceScene(Scene nextScene){
        this.gameScene = nextScene;
        stage.setScene(gameScene);
        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            try {
                step(SECOND_DELAY);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
    }

    /***
     * Used for advancing levels.
     * If Level>3 then takes to End Screen
     * Otherwise calls SetupLevel with reqd level
     * @param lvl
     * @throws FileNotFoundException
     */
    private void advanceLevel(int lvl) throws FileNotFoundException {
        animation.stop();
        if(lvl>3){
            Scene theEnd = EndGame();
            animation.stop();
            stage.setScene(theEnd);
        }
        else {
            gameScene = setupLevel(SIZE, SIZE, BACKGROUND, lvl);
            advanceScene(gameScene);
            ResetParams();
        }
    }

    /**
     * Resets the starting position of ball, paddle
     * Resets paddle speed, directions, and
     * if Level = 3, raises paddle up
     */
    private void ResetParams() {
        myBouncer.setX(gameScene.getWidth() / 4 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(gameScene.getHeight() / 2 - myBouncer.getBoundsInLocal().getHeight() / 2);
        myPaddle.setX(gameScene.getWidth()/2 - MOVER_WIDTH/2);
        myPaddle.setY(gameScene.getHeight()- MOVER_HEIGHT - 10);
        ball_y_direction = 1; ball_x_direction = 1;
        mover_speed = 35;
        if(currentLevel==3){
            BOUNCER_SPEED = 330;
            myPaddle.setY(gameScene.getHeight()- MOVER_HEIGHT - 40);
        }
    }

    /***
     * Helper that takes in a numeric code
     * and returns corresponging Brick object
     * intialized with correct parameters
     * such as Image, hits etc.
     * Called in addBricks
     * @param code
     * @return Brick instance
     */
    private Brick selectBrick(int code){
        Brick retBrick = new Brick("", 1 ,0, false, false);
        if(code == 1){retBrick = new Brick("brick3.gif", 1, 0, false, false);}
        else if(code == 2){retBrick = new Brick("brick5.gif", 3,0,false, false);}
        else if(code == 3){retBrick = new Brick("brick7.gif", 1,0,false, true);}
        else if(code == 4){retBrick = new Brick("brick2.gif", 1, 0, false, false, true);}
        else if(code == 5){retBrick = new Brick("brick10.gif", 1,0,true, false);}

        return retBrick;
    }

    /***
     * Adds the Bricks to the screen.
     * Reads in configuration from txt file
     * @param level
     * @throws FileNotFoundException
     */
    private void addBricks (int level) throws FileNotFoundException {
        File file = new File ("resources/lvl"+ level +".txt");
        Scanner reader = new Scanner(file);
        brickList = new ArrayList<>();

        int initXValue = 5; int initYValue = 40;
        int[] heightArray = {30, 20, 18};

        int yOffset = initYValue;
        int rowNumber = 0;
        while(reader.hasNextLine()){
            String [] brickHits = reader.nextLine().split(" ");//Split the line on spaces to get the brick config
            int xOffset = initXValue;
            int yIncr = 30;
            for (int i = 0; i< brickHits.length; i++){
                int code = Integer.parseInt(brickHits[i]);
                Brick temp = new Brick("", 0,0, false, false );
                if(code != 0){
                    temp = selectBrick(code);
                    temp.setBrickX(xOffset);temp.setBrickY(yOffset + 5*rowNumber);
                    temp.setRow(rowNumber+1);
                    temp.setHEIGHT(heightArray[level-1]);
                    root.getChildren().add(temp.getBrickImage()); //add the nodes to the group
                    brickList.add(temp);
                }
                xOffset += temp.getWidth();
                yIncr = temp.getHeight();
            }
            yOffset += yIncr;
            rowNumber++;
        }
        return;
    }

    /***
     * Adds the children of a Hydra brick
     * to the screen, by adding to Bricks
     * Param: coordinate where children should be added
     * @param x
     * @param y
     */
    private void addHydraBricks(int x, int y){
        Brick child1 = new Brick("brick1.gif", 1, 0, false, false);
        Brick child2 = new Brick("brick1.gif", 1, 0, false, false);
        child1.setWIDTH(30);child1.setHEIGHT(18);
        child2.setWIDTH(30); child2.setHEIGHT(18); //split into half
        child1.setBrickX(x); child1.setBrickY(y); //set location of child1
        child2.setBrickX(x+ child1.getWidth()+5); child2.setBrickY(y); //set location of child2
        child1.setRow(3);child2.setRow(3); //allow it to be hitten by the P cheatcode
        root.getChildren().addAll(child1.getBrickImage(), child2.getBrickImage());
        brickList.add(child1);brickList.add(child2);
    }

    /***
     * Initializes and sets
     * ball and paddle on the screen
     * takes in the dim. of screen
     * @param width
     * @param height
     */
    private void setBallPaddle(int width, int height){
        Image ball = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBouncer = new ImageView(ball);
        // x and y represent the top left corner, so center it in window
        myBouncer.setX(width / 4 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(height / 2 - myBouncer.getBoundsInLocal().getHeight() / 2);
        BOUNCER_SPEED = 250;
        myPaddle = new Rectangle(width / 2 - MOVER_WIDTH / 2, height - MOVER_HEIGHT - 10, MOVER_WIDTH, MOVER_HEIGHT);
        Image paddleImage = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
        if(currentLevel==3){
            BOUNCER_SPEED = 330;
            myPaddle.setY(gameScene.getHeight()- MOVER_HEIGHT - 40);
        }
        ImagePattern paddleImagePattern = new ImagePattern(paddleImage);
        myPaddle.setFill(paddleImagePattern);
    }

    /***
     * Used for creating displays of
     * Lives, Scores, and PowerUp
     * That can change withing the level
     */
    private void makeDynamicDisplays(){
        currentLives = 3;//reset lives for level
        livesDisp = new Label("Lives = " + currentLives);
        livesDisp.setLayoutY(2); livesDisp.setLayoutX(90);
        livesDisp.setFont(new Font("Georgia", 12));

        scoreDisp = new Label("Score = " + gameScore);
        scoreDisp.setLayoutY(2); scoreDisp.setLayoutX(340);
        livesDisp.setFont(new Font("Georgia", 12));

        powerDisp = new Label("POWERUP = ");
        powerDisp.setLayoutY(2); powerDisp.setLayoutX(170);
        powerDisp.setFont(new Font("Georgia", 13));
        powerDisp.setTextFill(Color.DARKRED);
        powerDisp.setVisible(false);

        promptDisp = new Label("PRESS SPACEBAR TO START...");
        promptDisp.setLayoutY(240);
        promptDisp.setLayoutX(80);
        promptDisp.setFont(new Font("Lucida", 20));
    }

    /***
     * Intializes the scene for a particular level,
     * with all desired objects and parameters
     * @param width
     * @param height
     * @param background
     * @param lvl
     * @return Scene instance for a particular level
     * @throws FileNotFoundException
     */
    private Scene setupLevel(int width, int height, Paint background, int lvl) throws FileNotFoundException {

        gameLost = false; randomGame = false;
        gameScore = 0;
        root = new Group();
        setBallPaddle(width, height); //init ball and paddle
        // make the static level label (doesn't change within a level)
        Label levelDisp = new Label("Level: " + lvl);
        levelDisp.setFont(new Font("Georgia", 12));
        levelDisp.setLayoutY(2); levelDisp.setLayoutX(2);
        //make the dynamic labels (change within a level)
        makeDynamicDisplays();
        addBricks(lvl); //add the bricks
        root.getChildren().addAll(myBouncer, myPaddle, levelDisp, livesDisp, scoreDisp,promptDisp, powerDisp);

        Scene scene = new Scene(root, width, height, background);//create the game scene
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode())); // respond to input
        return scene;
    }

    /***
     * Generates and returns a Button that
     * allows presser to replay the game
     * @return
     */
    private Button replayButton(){
        Button cont = new Button("Replay Game");
        cont.setLayoutX(160); cont.setLayoutY(300);
        cont.setOnAction(e -> {
            currentLevel = 1;
            try {
                gameScene = setupLevel(SIZE, SIZE, BACKGROUND, currentLevel);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            ResetParams();
            advanceScene(gameScene);
        });
        return cont;
    }

    /***
     * Generates a credit label
     * crediting the creator of the fame
     * @author Aneesh
     * @return
     */
    private Label creditLabel(){
        Label credit = new Label("Made with <3 by Aneesh Gupta");
        credit.setFont(new Font("Garamond", 15));
        credit.setTextFill(Color.INDIANRED);
        return credit;
    }

    /***
     * Returns the scene for the end of the game
     * contains outcome message
     * and replaying options
     * @return Scene endScreen
     */
    private Scene EndGame(){ //We're in the endgame now
        animation.stop();

        Rectangle hypno = new Rectangle(50, 50);
        Image hypnoImage = new Image(this.getClass().getClassLoader().getResourceAsStream("hypno.gif"));
        ImagePattern hypnoImagePattern = new ImagePattern(hypnoImage);
        hypno.setFill(hypnoImagePattern);
        hypno.setLayoutX(180); hypno.setLayoutY(50);

        String outcome = gameLost ? "   You Lost :(" : "   You Won :)";
        Label endDisp = new Label("GAME OVER \n" + outcome);
        endDisp.setFont(new Font("Garamond", 30));
        endDisp.setLayoutY(120); endDisp.setLayoutX(110);
        Button replay = replayButton();
        Label credit = creditLabel();
        credit.setLayoutX(110); credit.setLayoutY(340);
        Group endRoot = new Group();
        endRoot.getChildren().addAll(endDisp, hypno, replay, credit);
        return new Scene(endRoot,SIZE, SIZE, BACKGROUND);
    }

    /***
     * Checks to see if all
     * destroyable bricks have been
     * destroyed
     * @return boolean (level over or not)
     */
    private boolean bricksDestroyed(){
        int count = 0;
        for (Brick brick : brickList) {
            if (!brick.isPermanentBrick()) {
                count++;
            }
        }
        return (count==0);
    }

    /***
     * checks to see if it's time to bring in the
     * random state
     * @return boolean
     */
    private boolean randomState(){
        int count = 0;
        for (Brick brick : brickList) {
            if (!brick.isPermanentBrick()) {
                count++;
            }
        }
        return (count==4); // at 4 bricks randomize
    }

    /***
     * responsible for motion,
     * and iterative checking and interaction
     * of ball and other objects
     * @param elapsedTime
     * @throws FileNotFoundException
     */
    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) throws FileNotFoundException {
        //if it's time to shake things up, do it
        if(randomState()) randomizeGame();
        // update "actors" attributes
        ballMotion(elapsedTime);
        //Collisions from sidewalls
        checkBallWallCollisions();
        //Collisions with paddle
        checkPaddleBallCollisions();
        //Collisions with bricks
        checkBrickBallCollision();
        //Advance to next?
        if(bricksDestroyed()){
            BOUNCER_SPEED = BOUNCER_SPEED/2;
            currentLevel +=1;
            advanceLevel(currentLevel);
        }
    }

    /***
     * moves the ball by ++ X,Y position
     * @param elapsedTime
     */
    private void ballMotion(double elapsedTime){
        myBouncer.setX(myBouncer.getX() + ball_x_direction*BOUNCER_SPEED * elapsedTime);
        myBouncer.setY(myBouncer.getY() + ball_y_direction*BOUNCER_SPEED * elapsedTime);
    }

    /***
     * Collision logic between ball and
     * sides of the screen
     */
    private void checkBallWallCollisions(){
        if (myBouncer.getY() <= 0) ball_y_direction *= -1;
        if (myBouncer.getY()>gameScene.getHeight()){
            currentLives--;
            livesDisp.setText("Lives = " + currentLives);
            animation.stop();
            if(currentLives>0){ //gone out, so reset
                promptDisp.setVisible(true);
                ResetParams();
            }
            else if(currentLives==0){//game over
                gameLost = true;
                this.gameScene = EndGame();
                stage.setScene(gameScene);
            }
        }
        if (myBouncer.getX() + myBouncer.getBoundsInLocal().getWidth() >= gameScene.getWidth()) ball_x_direction *= -1;
        if (myBouncer.getX() <= 0) ball_x_direction *= -1;
    }

    /***
     * Collision logic between ball
     * and paddle
     */
    private void checkPaddleBallCollisions(){
        if(myPaddle.getBoundsInParent().intersects(myBouncer.getBoundsInParent())){
            if (myBouncer.getY() + myBouncer.getBoundsInLocal().getHeight() - 4 > myPaddle.getY()){
                ball_x_direction *= -1; ball_y_direction *= -1;
            }
            ball_y_direction = -1;
        }
    }

    /***
     * Collision logic between ball
     * and bricks
     */
    private void checkBrickBallCollision(){
        for(int i = 0; i<brickList.size(); i++) {
            Brick currBrick = brickList.get(i);
            ImageView tempImage = currBrick.getBrickImage();
            if (tempImage.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
                if((tempImage.getY()<=myBouncer.getY()+myBouncer.getBoundsInLocal().getHeight()/2) && (myBouncer.getY()+myBouncer.getBoundsInLocal().getHeight()/2 <=tempImage.getY()+tempImage.getFitHeight())){
                    ball_x_direction*=-1;//sideways hit
                }
                else{ ball_y_direction *= -1;}
                currBrick.decreaseHits();
                if(currBrick.getHitsAllowed() == 0 && !currBrick.isPermanentBrick()) {
                    if(currBrick.isHydra()) addHydraBricks((int)tempImage.getX(), (int)tempImage.getY());
                    if(currBrick.isPowerUp()) activatePowerUp();
                    brickList.remove(brickList.get(i));
                    root.getChildren().remove(tempImage);
                    gameScore++;
                    scoreDisp.setText("Score = " + gameScore);
                    return;
                }
            }
        }
    }

    /***
     * Elongates the paddle
     */
    private void elongatePower(){
        double tempWidth = myPaddle.getWidth();
        myPaddle.setWidth(myPaddle.getWidth()+45);
        myPaddle.setX(myPaddle.getX()-22);
        powerDisp.setText("POWERUP: STRETCH");powerDisp.setVisible(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        powerDisp.setVisible(false);
                        myPaddle.setWidth(tempWidth);
                    }
                },10000);
    }

    /***
     * increases paddle speed
     */
    private void speedPower(){
        powerDisp.setText("POWERUP: SWIFT"); powerDisp.setVisible(true);
        mover_speed += 20;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        powerDisp.setVisible(false);
                        mover_speed = 35;
                    }
                }, 10000);
    }

    /***
     * activates the powerUp
     * by randomly choosing a powerUp
     * if Powerup brick is hit
     */
    private void activatePowerUp(){
        Random random = new Random();
        int whichPowerUp = random.nextInt(3)+1;
        if(whichPowerUp ==1){//LIVES++
            currentLives++;
            livesDisp.setText("Lives : " + currentLives);
        }
        else if(whichPowerUp==2){//ELONGATE PADDLE
            elongatePower();
        }
        else if(whichPowerUp==3) { //Speed++
            speedPower();
        }
    }

    /***
     * Sets desired game parameters
     * for the Random part of game
     */
    private void randomizeGame(){
        gameScene.setFill(altBCKGRND);
        BOUNCER_SPEED = 200;
        mover_speed = 40;
        randomGame = true;
    }

    /***
     * Key control for random part of game
     * @param code
     */
    private void randomControl(KeyCode code){
        if(randomGame && code == KeyCode.RIGHT){
            myPaddle.setX(myPaddle.getX() - mover_speed);
        }
        else if(randomGame && code == KeyCode.LEFT){
            myPaddle.setX(myPaddle.getX() + mover_speed);
        }
        if(myPaddle.getX()>=gameScene.getWidth()){ //wrapping it around
            myPaddle.setX(3);
        }
        if(myPaddle.getX()+myPaddle.getWidth()<=3){
            myPaddle.setX(gameScene.getWidth()-myPaddle.getWidth()-3);
        }
    }

    /***
     * Key controls for regular gameplay
     * @param code
     */
    private void regularControl(KeyCode code){
        if (code == KeyCode.RIGHT && !(myPaddle.getX() + myPaddle.getWidth() >= gameScene.getWidth()-12)) {
            myPaddle.setX(myPaddle.getX() + mover_speed);
        }
        else if (code == KeyCode.LEFT && !(myPaddle.getX() <= 12)) {
            myPaddle.setX(myPaddle.getX() - mover_speed);
        }
    }

    /***
     * Handling keyboard inputs
     * @param code
     */
    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if(code==KeyCode.SPACE && !gameLost){
//            ResetParams();
            promptDisp.setVisible(false);
            animation.play();
        }
        if(!randomGame) regularControl(code);
        else if (randomGame) randomControl(code);
        checkCheatCode(code);
    }

    /***
     * Elongates paddle to fit screen
     * and returns to original size if pressed again
     * @param code
     */
    private void checkElongate(KeyCode code){
        //CHEATCODE: 'I' to elongate paddle fully
        if (code == KeyCode.I && fullBottomActivated==0){
            myPaddle.setWidth(gameScene.getWidth());
            myPaddle.setX(2);
            fullBottomActivated = 1;
        }
        else if (code == KeyCode.I && fullBottomActivated==1) {
            myPaddle.setWidth(MOVER_WIDTH);
            myPaddle.setX(gameScene.getWidth() / 2 - MOVER_WIDTH / 2);
            fullBottomActivated = 0;
        }
    }

    /***
     * Resets the ball and paddle to original position
     * @param code
     */
    private void checkReset(KeyCode code){
        //CHEATCODE: 'R' to reset paddle and ball position
        if (code == KeyCode.R){
            ResetParams();
            animation.stop();
            promptDisp.setVisible(true);
            ball_y_direction = 1;
            ball_x_direction = 1;
        }
    }

    /***
     * toggle between levels 1,2 and 3
     * @param code
     */
    private void checkToggle(KeyCode code){
        //CHEATCODE: Level Toggle: 1,2,3
        if(code.isDigitKey()){
            try {
                if(code.getCode()-48>=3){
                    advanceLevel(3);
                    currentLevel =3;
                    BOUNCER_SPEED = 330;
                    myPaddle.setY(gameScene.getHeight()- MOVER_HEIGHT - 40);
                }
                else{
                    advanceLevel(code.getCode()-48);
                    currentLevel = code.getCode()-48;
                    ResetParams();
                }
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }
    }

    /***
     * add Lives to the users game
     * @param code
     */
    private void checkLivesAdd(KeyCode code){
        //CHEATCODE: Add lives
        if(code==KeyCode.L){
            currentLives++;
            livesDisp.setText("Lives : " + currentLives);
        }
    }

    /***
     * Pops random bricks from the first 3 rows
     * @param code
     */
    private void checkPopper(KeyCode code){
        //CHEATCODE: Randomly Pops some of bricks in the first three rows
        if(code==KeyCode.P){
            Random random = new Random();
            int rowToPop = random.nextInt(3) + 1;
            for(int i = 0; i<brickList.size(); i++) {
                Brick currBrick = brickList.get(i);
                ImageView tempImage = currBrick.getBrickImage();
                if (currBrick.getRow() == rowToPop) {
                    if (!currBrick.isPermanentBrick()) {
                        brickList.remove(brickList.get(i));
                        root.getChildren().remove(tempImage);
                        gameScore++;
                        scoreDisp.setText("Score = " + gameScore);
                    }
                }
            }
        }
    }

    /***
     * checks and executes the above cheatcodes
     * on the basis of key pressed
     * @param code
     */
    private void checkCheatCode(KeyCode code){
        checkElongate(code); //elongate paddle to fit screen

        checkReset(code); //reset to start position

        checkToggle(code); //toggle 1,2,3 to diff levels

        checkLivesAdd(code); //Lives++

        checkPopper(code); //Random brick popper
    }


    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}

