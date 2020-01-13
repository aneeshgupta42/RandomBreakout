# Game Plan
## Name: Aneesh Gupta

### Breakout Variant
* The variant I liked the best was *Circus*. The two circus performers jumping using the paddle as a seesaw was really innovative, and added an element of 'Physics' to the gameplay. The side-scrolling game screen was also enjoyable.
### General Level Descriptions
* Level 1: There are 3 rows of bricks, all *regular* bricks. The ball speed, and the distance between the paddle and the bricks are standard.
* Level 2: There are 5 rows of bricks, with *regular* bricks, *hard* bricks, *power-up* bricks, and *permanent* bricks all present. Same distance between paddle and bricks, and same ball speed.
* Level 3: There are 6 rows of bricks, of different types (*regular, hard, power-up, permanent,* and *hydra*). The ball moves at 1.2x speed. The distance between the bricks and the paddle has also been reduced by some factor.

### Bricks Ideas
* Regular bricks: Bricks that 'pop' after one hit
* PowerUp bricks: differently colored bricks that may grant specific power-ups
* Hard bricks: Bricks that 'pop' only after multiple hits (2 or 3)
* Permanent bricks: Bricks that cannot be broken, and are just present as obstacles.
* 'Hydra' bricks: These split into 2 halves, each half then being a Regular brick. (Feature implementation left to discretion during code implementation)

### Power Up Ideas
* Elongate paddle: This powerup increases the length of the paddle.
* Speed-up paddle: This powerup increases the movement speed of the paddle.
* Extra-life: This powerup grants an extra life to the player.

### Cheat Key Ideas
* 'P' key: Automatically pops away the bricks one row at a time.
* 'I' key: Elongates the paddle to fit game screen (ball cannot escape down)
* 'R' key: Resets the ball and the paddle to the starting position.
* 'L' key: Adds an extra life to the player's game.
* '1-3' keys: Level toggle; takes you to the specified level.
### Something Extra
* There is a pane to keep track of the level, lives, and 'score'.
* In the middle of the level, randomly, the arrow keys flip (pressing left makes the paddle go right, and vice versa). Possibly, the paddle can also wrap around the sides of the screen (after encountering right side wall, paddle goes in and appears again from left).
