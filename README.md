<h1 align="center"> Project 1-2: Crazy Putting | Phase 3</h1>
<p align="center">Made by Group 02 - The Rockers</p><br>
<p align="center"><img src="https://i.imgur.com/EkfClq5.png"></p>

<hr>
<h2 align="center">Introduction</h2><br>
<p> CrazyPutting is the second project of the academic Y1 2021-2022.
This project was created with the idea of simulating Putting, a part of Golf where players aim to strike a ball across a grassy green field to have
it fall into a hole. This simple task was later expanded and bots to automatically shoot the ball were added. In the final phase, a maze inside the field is created, and different pathfinding bots are able to solve these mazes. This project involves different fields of Science, such as Calculus, Physics, Artificial Intelligence, Numerical Mathematics, Computer Science and many others.
<br><br>
<h2 align="center"> Game Engine </h2><br>
<p>The Game Engine was created using libGDX, a game development framework based on OpenGL. Three methods are used to create the scenary, render it and destroy it once the application is closed. This is used as basis to display what we're doing, but all the computations are not based on pixels, but on meters. These computations are done in the Physics Engine.</p>
<br><br></p>
<h2 align="center">Physics Engine</h2><br>

<p>The physics engine is responsable of making the ball move, through the equations given in the project manual. These equations fall under the ODE categories, and there are different solvers (such as RK4 and Euler) able to approximate these equations at a given time. Besides this, the Physics Engine is also responsable of generating water and handling obstacles.</p><br>

<h2 align="center">Game</h2><br>

<p>The game uses a key-based interaction, with the possibility of shooting the ball with the mouse. In fact, by dragging the mouse in the opposite side of where you want to shoot it, you will be able to shoot the ball with controlled velocity. To use the different bots, you can open the menu by pressing M. Here, all the keybinds to activate the other bots are shown. Note that to use the pathfinding bot, it is necessary to first press I or A to find a path. Back to the game, by pressing B a menu with an Obstacle Creator opens, and you will be able to click and place different kind of obstacles. Inside the Input.txt file you can set all sorts of starting parameters, including a flag to decide whether to generate a random maze upon program start or not.</p><br>

<h2 align="center">Bots</h2><br>
<p> At the current state of the project we have seven major bots implemented:<br><br>
-Rule-based bot, the simpliest of them all. It takes a shot based on an heuristic, and cannot simulate shots.<br>
- Bruteforce, that will try every possible combination with a recursively smaller precision in order to get the perfect shot. <br>
- Hillclimb, that improved with Simulated Annealing manages to escape the local minima and find the local optimum with great consistency.<br>
- Stochastic bot, simple yet impressive. Considering this very narrow search space, the stochastic bot utilise an heuristic to classify each shot and takes the best out of n iterations.<br>
- AngleBot, an experimental AI that uses trigonometry to optimize the shot each iteration.<br>
- Pathfinding Bot, bip bop<br>
- Implicit Pathfinding Bot, uses BFS to find a path inside a maze and shoot to the globally optimal node.<br><br>

For more information about how these work, please refer to the report.
</p>
  <br>
 <h2 align="center">Obstacles</h2><br>
 <p>Obstacles are generated by the obstacle creator, and added to the PhysicsEngine. Colliding with a tree will instantly result in a lose, while colliding with a rectangle will just make the ball bounce. This allows the user to create complex, creative fields that really challenge the player.</p>
 <br>
<h2 align="center">Additional documentation</h2><br>
<p> Additional information about the details of the methods and the algorithms can be found in the report submitted. The code is provided with JavaDoc support, so additional documentation can be found by running the following command:</p>

```bash
javadoc PACKAGE|SOURCE_FILE OPTIONS @ARGFILES

#`javadoc` is the command which will generate java source code documentation.
#`PACKAGE|SOURCE_FILE` is the package or source file name in which documentation will be generated.
#`OPTIONS` enables different behavior of javadoc
#`@ARGFILES` are used to provide arguments to the javadoc command.
```
<h2 align="center">How to run the code</h2><br>
<p> In order to run the code, your machine has to have the JDK 16 correctly installed and set in the correct enviroment. It is beneficial to have Gradle installed, but it's not necessary to run the app. <br> 
  
Start with Gradle:

```bash
$ cd RockGolf # change directory to the repo
$ gradle run # run the project via Gradle
```
  
To start without Gradle, run the main class in the DesktopLauncher situated in "RockGolf\desktop\src\com\rock\golf"
