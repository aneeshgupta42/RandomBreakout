game
====

This project implements the game of Breakout.

Name: Aneesh Gupta

### Timeline

Start Date: 01/12/2019

Finish Date: 01/19/2020

Hours Spent: 15-18 hours

### Resources Used
* JavaFX tutorial (TutorialsPoint)
* StackExchange
* Class Lab code (`lab_bounce`)


### Running the Program

#### Main class:
- `Main.java` (game_ag468/src/breakout/Main.java)
- This is the file to be run to execute the game.

#### Data files needed: 
- From the `resources` folder: `lvl1.txt`, `lvl2.txt`, `lvl3.txt`.
- Also from the `resources` dir: various `.gif` files for the ball, paddle, brick, and misc decoration.

#### Key/Mouse inputs:
- `Right`/`Left` arrow Keys to move paddle left and right
- In Random spell of level this is switched
- `Spacebar` to start a level

#### Cheat keys:
- `1, 2, 3` number keys to toggle between levels
- `R` to reset the ball and paddle position
- `L` to add lives to the user.
- `I` to elongate the paddle to fit screen
- `P` to randomly pop bricks in the 1-3rd rows of bricks (from top)

#### Known Bugs:
- Sometimes when ball hits the Permanent Bricks at a weird angle, it slides on them.
- `[Appears to be Fixed]` After winning the game, and trying to replay it, the levels appear in a minimized manner 

#### Extra credit:
- Towards the end of each level:
  - The screen goes red
  - The paddle's controls switch (Left becomes right, right becomes left)
  - The paddle also starts to wrap around the screen
  - This resets at the completion of the level 
- Sudden change in the middle of level requires user to adapt fast, and after completion of level, adapt back to the regular controls.
- Also implemented a pane that keeps track of score, level, lives, and displays powerUps when activated.
- Have really cool *'Hydra'* bricks, that upon collision break into two smaller regular bricks.


### Notes/Assumptions

1. The game window isn't maximised or minimized during play.
2. Java13 is used.

### Impressions

