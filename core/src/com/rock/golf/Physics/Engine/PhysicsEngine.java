package com.rock.golf.Physics.Engine;

import org.mariuszgromada.math.mxparser.Function;
import java.util.ArrayList;
import java.util.List;

import com.rock.golf.RockGolf;
import com.rock.golf.Input.Crafter;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Physics.Solvers.AdamsBashforth2;
import com.rock.golf.Physics.Solvers.EulerSolver;
import com.rock.golf.Physics.Solvers.RK2Solver;
import com.rock.golf.Physics.Solvers.RK4Solver;
import com.rock.golf.Physics.Solvers.Solver;

public class PhysicsEngine implements Runnable {
    // constants
    public static final double g = 9.81;
    public double h = 0.03;
    public final double ballRadius = 0.05;

    // fields
    private double uK, uS;
    private double targetX, targetY, targetRadius;
    private Function golfCourse;
    private StateVector vector;
    private double[] input;
    private boolean abort;
    private List<Sandpit> sandpits;
    public static List<RectangleObstacle> rectangles;
    public static List<Tree> trees;
    private Solver solver;
    private char rkMode = 'h';
    public double tolerance = 0.1;
    public boolean stuck;
    public static Der derivative;

    // Bot constructor
    public PhysicsEngine(double h, char rkMode) {
        derivative = Crafter.initializeClass();
        this.rkMode = rkMode;
        input = InputModule.getInput();
        setVariables();
        abort = false;
        this.h = h;
        sandpits = new ArrayList<Sandpit>();
        trees = new ArrayList<Tree>();
        // trees.add(new Tree(new double[] { 2, 2 }, 0.4));
        rectangles = new ArrayList<RectangleObstacle>();
        // rectangles.add(new rectangleObstacle(new float[]{-2, (float) 0.5}, 5, 0.2));
        // rectangles.add(new rectangleObstacle(new float[]{-3, (float) -2.5}, 0.2, 4.5));
        // rectangles.add(new rectangleObstacle(new float[]{-2, (float) -1.5}, 0.2, 2));
        // rectangles.add(new rectangleObstacle(new float[]{-3, (float) -2.5}, 6, 0.2));
        // rectangles.add(new rectangleObstacle(new float[]{(float) 2.8, (float) -2.5}, 0.2, 3));
        // sandpits.add(new Sandpit(new double[] { -4, 4 }, 1, uK, uS));
    }

    @Override
    public void run() {
        fireNewShot();
    }

    /**
     * This method starts a new golf shot based on the current parameters that are
     * set in the Input.txt file.
     */

    public void fireNewShot() {
        RockGolf.winStatus = false;
        RockGolf.losingStatus = false;
        RockGolf.shotActive = true;
        RockGolf.shotCounter++;
        setVariables();
        solver = solverFactory();
        Double step = h * 1000;
        long timestep = step.longValue();
        long checkpoint = System.currentTimeMillis();
        if (!ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, 0))
            tolerance = 0.1;
        else
            tolerance = 0;

        while ((ballIsMoving() && !ballInTarget() || hillIsSteep() && !ballInTarget())
        && !collidedWithTree(vector.getXPos(), vector.getXPos())
        && !isInWater(vector.getXPos(), vector.getYPos())
        && ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, tolerance)) {

            long currentTime = System.currentTimeMillis();
            if (currentTime - checkpoint > timestep) {
                if (!ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, 0))
                    tolerance = 0.1;
                else
                    tolerance = 0;

                Sandpit currentSandpit = currentSandpit();
                if (currentSandpit != null) {
                    solver.updateFriction(currentSandpit.getUK(), currentSandpit.getUS());
                } else {
                    solver.updateFriction(uK, uS);
                }
                int rectID = collidedWithObstacles(vector.getXPos(), vector.getYPos());
                if (rectID != -1) {
                    RectangleObstacle obstacle = rectangles.get(rectID);
                    vector = obstacle.bounce(ballRadius, vector);
                }
                vector = solver.computeStep(vector);
                RockGolf.updatePosition(vector);
                checkpoint = System.currentTimeMillis();
            }
            if (abort) {
                break;
            }
        }
        if (!ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, -tolerance))
            tolerance = 0.1;
        else
            tolerance = 0;

        if (ballInTarget()) {
            RockGolf.winStatus = true;
        } else if (collidedWithTree(vector.getXPos(), vector.getYPos())) {
            RockGolf.collisionTreeStatus = true;
        }
        stuck = isInWater(vector.getXPos(), vector.getYPos()) || collidedWithTree(vector.getXPos(), vector.getXSpeed());
        InputModule.setNewPosition(vector.getXPos(), vector.getYPos());
        RockGolf.shotActive = false;
    }

    /**
     * This method simulates shots for the Bots without updating the UI.
     * 
     * @param velX The initial x velocity of the shot.
     * @param velY The initial y velocity of the shot
     * @return Array containing the final x and y position resulting
     *         from the shot.
     */

    public double[] getSimulatedShot(double velX, double velY) {
        setVariables();
        vector.setXSpeed(velX);
        vector.setYSpeed(velY);
        solver = solverFactory();

        while ((ballIsMoving() && !ballInTarget() || hillIsSteep() && !ballInTarget())
        // && !collidedWithTree(vector.getXPos(), vector.getYPos())
        && !isInWater(vector.getXPos(), vector.getYPos())
        && ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, tolerance)) {

            if (!ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, 0))
                tolerance = 0.1;
            else
                tolerance = 0;
            if (!ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, 0))
                tolerance = 0.1;
            else
                tolerance = 0;

            Sandpit currentSandpit = currentSandpit();
            if (currentSandpit != null) {
                solver.updateFriction(currentSandpit.getUK(), currentSandpit.getUS());
            } else {
                solver.updateFriction(uK, uS);
            }
            int rectID = collidedWithObstacles(vector.getXPos(), vector.getYPos());
            if (rectID != -1) {
                RectangleObstacle obstacle = rectangles.get(rectID);
                vector = obstacle.bounce(ballRadius, vector);
            }
            vector = solver.computeStep(vector);
            if (abort) {
                break;
            }
        }

        if (!ballInScreen(new double[] { vector.getXPos(), vector.getYPos() }, -tolerance))
            tolerance = 0.1;
        else
            tolerance = 0;

        return new double[] { vector.getXPos(), vector.getYPos() };
    }

    /**
     * This method determines wether the ball is currently inside a sandpit.
     * 
     * @return The sandpit the ball is currently in, otherwise null.
     */

    private Sandpit currentSandpit() {
        for (int i = 0; i < sandpits.size(); i++) {
            if (sandpits.get(i).isInSandpit(vector.getXPos(), vector.getYPos())) {
                return sandpits.get(i);
            }
        }
        return null;
    }

    public List<Sandpit> getSandpits() {
        return sandpits;
    }

    public boolean collidedWithTree(double xPos, double yPos) {
        for (int i = 0; i < trees.size(); i++) {
            if (trees.get(i).collidedWithTree(xPos, yPos, ballRadius)) {
                return true;
            }
        }
        return false;
    }

    public int collidedWithObstacles(double xPos, double yPos) {
        for (int i = 0; i < rectangles.size(); i++) {
            if (rectangles.get(i).obstacleCollision(xPos, yPos, ballRadius)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method reads in all the parameters from the Input.txt file and updates
     * the fields of the physics engine accordingly.
     */

    private void setVariables() {
        double[] variables = InputModule.getInput();
        uK = variables[0];
        uS = variables[1];
        targetX = variables[2];
        targetY = variables[3];
        targetRadius = variables[4];
        vector = new StateVector(variables[5], variables[6], variables[7], variables[8]);
        golfCourse = InputModule.getProfile();
    }

    /**
     * This method determines wether the ball is currently moving.
     * 
     * @return Boolean value: true if ball is moving, false if not.
     */

    private boolean ballIsMoving() {
        if (Math.abs(Math.sqrt(Math.pow(vector.getXSpeed(), 2) + Math.pow(vector.getYSpeed(), 2))) < h) {
            vector.setXSpeed(0);
            vector.setYSpeed(0);
            return false;
        }
        return true;
    }

    /**
     * This method determines wether the ball is currently inside the target based
     * on the position and radius of the ball as well as the target.
     * 
     * @return Boolean value: true if ball is inside target, false if not.
     */

    private boolean ballInTarget() {
        if (Math.pow(vector.getXPos() - targetX, 2) + Math.pow(vector.getYPos() - targetY, 2) <= Math.pow(targetRadius,
                2)) {
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    /**
     * This method determines wether the ball is currently in a body of water.
     * 
     * @param ballPos Array containing the current x and y position of the ball.
     * @return Boolean value: true if ball is inside target, false if not.
     */

    public boolean isInWater(double xPos, double yPos) {
        if (derivative.compute(xPos, yPos) < 0) {
            RockGolf.newShotPossible = false;
            return true;
        }
        return false;
    }

    /**
     * This method determines wether the downhillforce acting upon the golf ball in
     * rest
     * is greater than the static friction, which would cause it to start rolling
     * again.
     * 
     * @return Boolean value: true if ball is about to start rolling again, false if
     *         not.
     */

    private boolean hillIsSteep() {
        double xSlope = derivative.derivativeX(vector.getXPos(), vector.getYPos());
        double ySlope = derivative.derivativeY(vector.getXPos(), vector.getYPos());
        return uS <= Math.sqrt(Math.pow(xSlope, 2) + Math.pow(ySlope, 2));
    }

    /**
     * This method determines wether the ball is currently located inside of the
     * bounds of the graphical application.
     * 
     * @param ballPos Array containing the current x and y position of the ball.
     * @return Boolean value: true if ball is about to start rolling again, false if
     *         not.
     */

    public boolean ballInScreen(double[] ballPos, double tolerance) {
        boolean xIn = ballPos[0] - tolerance < ((RockGolf.width / 2) / RockGolf.metertoPixelRatio)
                && ballPos[0] + tolerance > -((RockGolf.width / 2) / RockGolf.metertoPixelRatio);
        boolean yIn = ballPos[1] - tolerance < ((RockGolf.height / 2) / RockGolf.metertoPixelRatio)
                && ballPos[1] + tolerance > -((RockGolf.height / 2) / RockGolf.metertoPixelRatio);
        return xIn && yIn;
    }

    public double[] getInputArray() {
        return input;
    }

    public StateVector getVector() {
        return vector;
    }
    
    private Solver solverFactory(){
        switch(rkMode){
            case 'l' : return new RK2Solver(uK, uS, h, golfCourse);
            case 'h' : return new RK4Solver(uK, uS, h, golfCourse);
            case 'a' : return new AdamsBashforth2(uK, uS, h, golfCourse);
            default : return new EulerSolver(uK, uS, h, golfCourse);
        }
    }

    /**
     * This method aborts a golf ball shot in case of closing the program.
     */

    public void abort() {
        abort = true;
    }

    public void resume() {
        abort = false;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public List<RectangleObstacle> getRectangles() {
        return rectangles;
    }
}
