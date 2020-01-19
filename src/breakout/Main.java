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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
    public static final Paint BACKGROUND = Color.BISQUE;

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
    private int currentLevel =1;
    private int currentLives;


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
                "\t 1. Use arrow keys to move paddle, and guide ball to pop bricks\n" +
                "\t 2. You get three lives per level\n" +
                "\t 3. There are different types of bricks in this game\n" +
                "\t 4. In the middle of the level the control's change (such as arrow directions)\n"+
                "\t\t Adapt to this change and finish the level");
        rules.setLayoutX(10);
        rules.setLayoutY(100);

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
//        root.getChildren().removeAll();
        this.gameScene = nextScene;
        stage.setScene(gameScene);
        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            try {
                step(SECOND_DELAY);
            } catch (FileNotFoundException ex) {
                System.out.println("Line 92");
                ex.printStackTrace();
            }
        });
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
//        animation.play();
    }

    private void advanceLevel(int lvl) throws FileNotFoundException {
        animation.stop();
        gameScene = setupGame(SIZE, SIZE, BACKGROUND, lvl);
        advanceScene(gameScene);
        ResetParams();
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

        brickList = new ArrayList<Brick>();
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
                        else if(code == 4){temp = new Brick("brick3.gif", 1, 0, false, false);}
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

        Label levelDisp = new Label("Level: " + Integer.toString(lvl));
        levelDisp.setLayoutY(2);
        levelDisp.setLayoutX(2);

        livesDisp = new Label(Integer.toString(currentLives));
        livesDisp.setLayoutY(2);
        livesDisp.setLayoutX(200);
        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBouncer);
        root.getChildren().add(myMover);
        addBricks(lvl);

        root.getChildren().add(levelDisp);
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
            if(currentLives>0){
                ResetParams();
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

    private void checkBrickBallCollision(){
        for(int i = 0; i<brickList.size(); i++) {
            Brick currBrick = brickList.get(i);
            ImageView tempImage = currBrick.getBrickImage();
            if (tempImage.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
                ball_y_direction *= -1;
                currBrick.decreaseHits();
                if(currBrick.getHitsAllowed() == 0 && !currBrick.isPermanentBrick()) {
                    brickList.remove(brickList.get(i));
                    root.getChildren().remove(tempImage);
                    return;
                }
            }
        }
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if(code==KeyCode.SPACE){
            ResetParams();
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
            ball_y_direction = 1;
            ball_x_direction = 1;
        }

        //CHEATCODE: Level Toggle: 1,2,3
        if(code.isDigitKey()){
            try {
                advanceLevel(code.getCode()-48);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }
        if(code==KeyCode.L){
            currentLives++;
        }

    }


    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}

