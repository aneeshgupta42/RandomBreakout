# Game - RandomBreakout
## Name: Aneesh Gupta  (ag468)

#### Names of contributors
- Aneesh Gupta

#### Each person's role
- Aneesh Gupta: responsible for project ideation, planning, implementation, debugging, etc. (All of it)


#### Design Goals
- Wanted to make it easy to have different type of bricks, so have a brick class
- The brick class has overloaded constructors, and these are used to assign parameters to the brick (corresponding to different types of bricks)

#### High-level design
- There is a Game class
  - Responsible for running game, implementing step increment, instantiating the brick, ball and paddle objects
  - Provides the collision logic between the different components
  - Brings in different level scenes onto the stage
- Brick class
  - Different parameter variables for different bricks
  - These variables hold different values for different bricks
  - Brick object is instantiated in the Game class 

#### Assumptions and Decisions
- The Game window is not maximized or minimized during the play of the game
- I decided to go into a lot of detail such as explicitly giving rules, what different types of bricks represent
- Have tried my best to make the game very user friendly

#### Adding new features
- Current game design has logic for all components I wanted 
- Current game also meets all specification requirements
- To better the state of the game, I would like to branch off into more classes, and structure the code better.
