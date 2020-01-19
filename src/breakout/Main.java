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
    public static final Paint BACKGROUND = Color.DARKSALMON;

    private String BOUNCER_IMAGE = "ball.gif";
    private String PADDLE_IMAGE = "paddle.gif";
    private int BOUNCER_SPEED = 300;

    public static final int MOVER_WIDTH = 80;
    public static final int MOVER_HEIGHT = 20;
    public static final int MOVER_SPEED = 35;

    private int ball_y_direction = 1;
    private int ball_x_direction = 1;
    private int fullBottomActivated = 0;
    private ArrayList<Brick> brickList;

    // some things needed to remember during game
    private Stage stage;
    Timeline animation = new Timeline();
    private Scene gameScene;
    private ImageView myBouncer;
    private Rectangle myMover;
    private Group root;
    private Label livesDisp;
    private Label scoreDisp;
    private Label promptDisp;
    private int currentLevel = 1;
    private int currentLives;
    private int gameScore = 0;
    private boolean gameLost;


    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) throws FileNotFoundException {
        // attach scene to the stage and display it
        gameScene = setupGame(SIZE, SIZE, BACKGROUND, currentLevel);
        Button cont = new Button("Start Game");
        cont.setLayoutX(170);
        cont.setLayoutY(200);
        cont.setOnAction(e -> advanceScene(gameScene));

        Label rules = new Label("RandomBreakout\n" +
                "\t 1. Use arrow keys to move paddle to guide ball to pop bricks\n" +
                "\t 2. You get three lives per level\n" +
                "\t 3. There are different types of bricks in this game\n" +
                "\t 4. In the middle of the level controls change (such as arrow keys)\n"+
                "\t\t Adapt to this change and finish the level");
        rules.setLayoutX(0);
        rules.setLayoutY(80);

        Group root = new Group();
        root.getChildren().add(cont);
        root.getChildren().add(rules);
        Scene splashScreen = new Scene(root, SIZE, SIZE, BACKGROUND);
        stage.setTitle(TITLE);
        stage.setScene(splashScreen);
        stage.show();
        this.stage = stage;
    }

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

    private void advanceLevel(int lvl) throws FileNotFoundException {
        animation.stop();
        if(lvl>3){
            System.out.println("GAME OVER - YOU WON!!");
        }
        else {
            gameScene = setupGame(SIZE, SIZE, BACKGROUND, lvl);
            advanceScene(gameScene);
            ResetParams();
        }
    }

    private void ResetParams() {
        myBouncer.setX(gameScene.getWidth() / 4 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(gameScene.getHeight() / 2 - myBouncer.getBoundsInLocal().getHeight() / 2);
        myMover.setX(gameScene.getWidth()/2 - MOVER_WIDTH/2);
        myMover.setY(gameScene.getHeight()- MOVER_HEIGHT - 10);
        ball_y_direction = 1;
        ball_x_direction = 1;
    }

    private void addBricks (int level) throws FileNotFoundException {
        File file = new File ("resources/lvl"+ level +".txt");
        Scanner reader = new Scanner(file);

        int initXValue = 5;
        int initYValue = 40;
        int[] heightArray = {30, 20, 18};

        brickList = new ArrayList<>();
        int yValue = initYValue;
        int rowNumber = 0;
        while(reader.hasNextLine()){
            String row = reader.nextLine();
            String [] brickHits = row.split(" ");

            int xValue = initXValue;
            int yIncr = 30;
            for (int i = 0; i< brickHits.length; i++){
                int code = Integer.parseInt(brickHits[i]);
                Brick temp = new Brick("", 0,0, false, false );
                temp.setHEIGHT(heightArray[level-1]);
                if(code != 0){
                        if(code == 1){temp = new Brick("brick3.gif", 1, 0, false, false);}
                        else if(code == 2){temp = new Brick("brick5.gif", 3,0,false, false);}
                        else if(code == 3){temp = new Brick("brick7.gif", 1,0,false, true);}
                        else if(code == 4){temp = new Brick("brick2.gif", 1, 0, false, false, true);}
                        else if(code == 5){temp = new Brick("brick10.gif", 1,0,true, false);}

                    temp.setBrickX(xValue);
                    temp.setBrickY(yValue + 5*rowNumber);
                    temp.setRow(rowNumber+1);
                    root.getChildren().add(temp.getBrickImage()); //add the nodes to the group
                    brickList.add(temp);
                }
                xValue += temp.getWidth();
                yIncr = temp.getHeight();
            }
            yValue += yIncr;
            rowNumber++;
        }
        return;
    }
    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int width, int height, Paint background, int lvl) throws FileNotFoundException {
        // create one top level collection to organize the things in the scene
        gameLost = false;
        root = new Group();
        // make some shapes and set their properties
        Image ball = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBouncer = new ImageView(ball);
        // x and y represent the top left corner, so center it in window
        myBouncer.setX(width / 4 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(height / 2 - myBouncer.getBoundsInLocal().getHeight() / 2);

        myMover = new Rectangle(width / 2 - MOVER_WIDTH / 2, height - MOVER_HEIGHT - 10, MOVER_WIDTH, MOVER_HEIGHT);
        Image paddleImage = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
        ImagePattern paddleImagePattern = new ImagePattern(paddleImage);
        myMover.setFill(paddleImagePattern);

        Label levelDisp = new Label("Level: " + lvl);
        levelDisp.setFont(new Font("Georgia", 12));
        levelDisp.setLayoutY(2);
        levelDisp.setLayoutX(2);

        currentLives = 3;//reset lives for level
        livesDisp = new Label("Lives = " + currentLives);
        livesDisp.setLayoutY(2);
        livesDisp.setLayoutX(180);
        livesDisp.setFont(new Font("Georgia", 12));

        scoreDisp = new Label("Score = " + gameScore);
        scoreDisp.setLayoutY(2);
        scoreDisp.setLayoutX(340);
        livesDisp.setFont(new Font("Georgia", 12));
        // order added to the group is the order in which they are drawn
        root.getChildren().addAll(myBouncer, myMover);
        addBricks(lvl);
        promptDisp = new Label("PRESS SPACEBAR TO START...");
        promptDisp.setLayoutY(160);
        promptDisp.setLayoutX(80);
        promptDisp.setFont(new Font("Lucida", 20));
        root.getChildren().addAll(levelDisp, livesDisp, scoreDisp,promptDisp);
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }
    private boolean bricksDestroyed(){
        int count = 0;
        for (Brick brick : brickList) {
            if (!brick.isPermanentBrick()) {
                count++;
            }
        }
        return (count==0);
    }

    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) throws FileNotFoundException {
        BOUNCER_SPEED = 300;
        // update "actors" attributes
        myBouncer.setX(myBouncer.getX() + ball_x_direction*BOUNCER_SPEED * elapsedTime);
        myBouncer.setY(myBouncer.getY() + ball_y_direction*BOUNCER_SPEED * elapsedTime);

        // collisions from sides
        if (myBouncer.getY() <= 0){ ball_y_direction *= -1;}
        if (myBouncer.getY()>gameScene.getHeight()){
            currentLives--;
            livesDisp.setText("Lives = " + currentLives);
            if(currentLives>0){
                animation.stop();
                promptDisp.setVisible(true);
                ResetParams();
            }
            if(currentLives==0){
                animation.stop();
                promptDisp.setText("YOU LOST :( ...");

                gameLost = true;
                promptDisp.setVisible(true);
            }
        }
        if (myBouncer.getX() + myBouncer.getBoundsInLocal().getWidth() >= gameScene.getWidth()){
            ball_x_direction *= -1;}
        if (myBouncer.getX() <= 0){ ball_x_direction *= -1;}


        //Collisions with paddle 
        if(myMover.getBoundsInParent().intersects(myBouncer.getBoundsInParent())){
            if (myBouncer.getY() + myBouncer.getBoundsInLocal().getHeight() - 4 > myMover.getY()){
                ball_x_direction *= -1;
                ball_y_direction *= -1;
            }
            ball_y_direction = -1;
        }
        checkBrickBallCollision();
        if(bricksDestroyed()){
            BOUNCER_SPEED = BOUNCER_SPEED/2;
            currentLevel +=1;
            advanceLevel(currentLevel);
        }
    }
    private void addHydraBricks(int x, int y){
        Brick child1 = new Brick("brick1.gif", 1, 0, false, false);
        Brick child2 = new Brick("brick1.gif", 1, 0, false, false);
        child1.setWIDTH(30);
        child1.setHEIGHT(18);
        child1.setRow(3);
        child2.setRow(3);
        child2.setWIDTH(30);
        child1.setHEIGHT(18);
        child1.setBrickX(x); child1.setBrickY(y);
        child2.setBrickX(x+ child1.getWidth()+5); child2.setBrickY(y);
        root.getChildren().addAll(child1.getBrickImage(), child2.getBrickImage());
        brickList.add(child1);
        brickList.add(child2);
    }
    private void activatePowerUp(){
        Random random = new Random();
        int whichPowerUp = random.nextInt(3)+1;
        if(whichPowerUp ==1){//LIVES++
            currentLives++;
            livesDisp.setText("Lives : " + currentLives);
        }
        else if(whichPowerUp==2){//ELONGATE PADDLE


        }

    }
    private void checkBrickBallCollision(){
        for(int i = 0; i<brickList.size(); i++) {
            Brick currBrick = brickList.get(i);
            ImageView tempImage = currBrick.getBrickImage();
            if (tempImage.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
                if((tempImage.getY()<=myBouncer.getY()+myBouncer.getBoundsInLocal().getHeight()/2) && (myBouncer.getY()+myBouncer.getBoundsInLocal().getHeight()/2 <=tempImage.getY()+tempImage.getFitHeight())){
//                    ball_y_direction*=-1;
                    ball_x_direction*=-1;
                }
                else{
                ball_y_direction *= -1;}
                currBrick.decreaseHits();
                if(currBrick.getHitsAllowed() == 0 && !currBrick.isPermanentBrick()) {
                    if(currBrick.isHydra()){
                        addHydraBricks((int)tempImage.getX(), (int)tempImage.getY());
                    }
                    brickList.remove(brickList.get(i));
                    root.getChildren().remove(tempImage);
                    gameScore++;
                    scoreDisp.setText("Score = " + gameScore);
                    return;
                }
            }
        }
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if(code==KeyCode.SPACE && !gameLost){
//            ResetParams();
            promptDisp.setVisible(false);
            animation.play();
        }
        if (code == KeyCode.RIGHT && !(myMover.getX() + myMover.getWidth() >= gameScene.getWidth()-12)) {
            myMover.setX(myMover.getX() + MOVER_SPEED);
        }
        else if (code == KeyCode.LEFT && !(myMover.getX() <= 12)) {
            myMover.setX(myMover.getX() - MOVER_SPEED);
        }
        //CHEATCODE: 'I' to elongate paddle fully
        if (code == KeyCode.I && fullBottomActivated==0){
            myMover.setWidth(gameScene.getWidth());
            myMover.setX(2);
            fullBottomActivated = 1;
        }
        else if (code == KeyCode.I && fullBottomActivated==1){
            myMover.setWidth(MOVER_WIDTH);
            myMover.setX(gameScene.getWidth()/2 - MOVER_WIDTH/2);
            fullBottomActivated = 0;
        }
        //CHEATCODE: 'R' to reset paddle and ball position
        if (code == KeyCode.R){
            ResetParams();
            animation.stop();
            promptDisp.setVisible(true);
            ball_y_direction = 1;
            ball_x_direction = 1;
        }

        //CHEATCODE: Level Toggle: 1,2,3
        if(code.isDigitKey()){
            try {
                if(code.getCode()-48>3){
                    advanceLevel(3);
                    currentLevel =3;
                }
                else{
                    advanceLevel(code.getCode()-48);
                    currentLevel = code.getCode()-48;
                }
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }
        //CHEATCODE: Add lives
        if(code==KeyCode.L){
            currentLives++;
            livesDisp.setText("Lives : " + currentLives);
        }

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


    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}

