<h1 align="center"> Project 1-2: Crazy Putting | Phase 21</h1>
<p align="center">Made by Group 02 - The Rockers</p><br>
<p align="center"><img src="https://i.imgur.com/sVwhinv.png"></p>

<hr>
<h2 align="center">Introduction</h2><br>
<p> This project was created with the idea of simulating Putting, a part of Golf where players aim to strike a ball across a grassy green field to have
it fall into a hole. In order to compute the physics, a physics engine was created based on some equations provided by our department, more information in the project manual and/or our report.
<br><br>
<h2 align="center"> Game Engine </h2><br>
<p>The Game Engine was created using libGDX, a game development framework based on OpenGL. Three methods are used to create the scenary, render it and destroy it once the application is closed. Here different methods to compute collisions, height maps and the general game logic are created. More will be discussed in the main Game section.</p>
<br><br></p>
<h2 align="center">Physics Engine</h2><br>

<p>Each time it is hit, the golf ball is set in motion in a given direction. The speed is determined by how hard the ball is hit, up to some given maximum speed vmax.
To simplify the physics we assume that the ball is always sliding on top of the terrain, and is always hit along the surface (not upwards).

The motion of the ball across the course is governed by three forces:
1. The force of gravity. The gravitational force is constant, and is directed downwards.
2. The normal force. As the name indicates, this force is directed normal to the
surface. Thus, its magnitude depends on the slopes of the terrain. Due to sum of
the gravitational and normal force, the ball accelerates when it moves downhill, and
decelerates if it slides uphill.
3. The force of friction. When the ball is moving, we speak of kinetic friction. The
kinetic friction is always directed opposite to the direction of motion. The magnitude
of the kinetic friction is proportional to the normal force, with a proportionality
constant μK .
When the ball comes to rest (on a slope), we speak of static friction. The static
friction force might prevent the ball from sliding down - this happens when the
friction compensates for the downhill force. The maximal static friction force is also
proportional to the normal force, the proportionality constant μS (the static friction
coefficient) is larger than the kinetic friction coefficient μK < μS.<br>
<br>
We  implemented three numerical solvers in order to solve the differential equations that solved these differential equations, namely Euler, RK2 (Ralston's Method) and RK4 (Generic).
                                                                    </p><br>

<h2 align="center">Game</h2><br>

<p>The game consists of circles of different sizes: a ball, a hole and different other part that makes the field, namely trees, water and sandpits. The goal is hitting the ball in the goal, while avoiding water and trees. To do so the player has to drag the ball on the opposite way of where he wants it to end up, like a sling. Alternatively, for lazy players, a vast gamma of bots able to play the game are implemented :)</p><br>
<h2 align="center">Bots</h2><br>
<p> At the current state of the project we have five major bots implemented:<br><br>
-Rule-based bot, the simpliest of them all. It takes a shot based on an heuristic, and cannot simulate shots.<br>
- Bruteforce, that will try every possible combination with a recursively smaller precision in order to get the perfect shot. <br>
- Hillclimb, that improved with Simulated Annealing manages to escape the local minima and find the local optimum with great consistency.<br>
- Stochastic bot, simple yet impressive. Considering this very narrow search space, the stochastic bot utilise an heuristic to classify each shot and takes the best out of n iterations.<br>
- AngleBot, an experimental AI that uses trigonometry to optimize the shot each iteration.
  
  To open the menu used to select these bots, you just have to press "M".</p>
  <br>
 <h2 align="center">Trees and Sandpit</h2><br>
 <p> Trees and Sandpit are not included by default, yet they're implemented. To add them to the game, you have to add them manually to the dedicated lists inside the PhysicsEngine class. </p>
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
