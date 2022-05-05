package com.rock.golf;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import java.math.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.rock.golf.Bot.HillClimbingAlgorithm;
import com.rock.golf.Bot.StochasticBot;
import com.rock.golf.Input.*;
import com.rock.golf.Math.Derivation;

import org.mariuszgromada.math.mxparser.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.mariuszgromada.math.mxparser.Function;

import com.rock.golf.Input.*;
import com.rock.golf.Math.Derivation;

public class RockGolf extends ApplicationAdapter {
    private float ballRadius;
    private float targetRadius;
    private static float originX;
    private static float originY;
    private static float xPosition;
    private static float yPosition;
    private float targetxPosition;
    private float targetyPosition;
    private boolean gameIsPlaying;
    private SpriteBatch lost;
    private Random rand;
    private ShapeRenderer ball;
    private ShapeRenderer target;
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer obstacles;
    private ShapeRenderer launchVector;
    private ArrayList<float[]> map = new ArrayList<>();
    private ArrayList<float[]> color = new ArrayList<>();
    private double[] input;
    private Vector2 obs1, obs2;
    private String status = "";
    private Runnable engine;
    private static int width, height;
    private StochasticBot bot;
    private ExecutorService executor;
    private SpriteBatch position, shot;
    private BitmapFont font;
    public static int shotCounter;
    public static boolean shotActive;
    public static boolean newShotPossible;
    public InputHandling in = new InputHandling();

    @Override
    public void create() {
        originX = Gdx.graphics.getWidth() / 2;
        originY = Gdx.graphics.getHeight() / 2;
        lost = new SpriteBatch();
        ball = new ShapeRenderer();
        target = new ShapeRenderer();
        obstacles = new ShapeRenderer();
        launchVector = new ShapeRenderer();
        engine = new PhysicsEngine();
        executor = Executors.newFixedThreadPool(1);
        Gdx.input.setInputProcessor(in);
        position = new SpriteBatch();
        shot = new SpriteBatch();
        font = new BitmapFont();
        shotCounter = 0;
        shapeRenderer = new ShapeRenderer();
        prepare_new_shot();
        xPosition = metersToPixel(convert(input[5])) + originX;
        yPosition = metersToPixel(convert(input[6])) + originY;
        generateObs();
        generateField();
        shotActive = false;
        newShotPossible = true;
        gameIsPlaying = true;

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        createMap();
        target.begin(ShapeRenderer.ShapeType.Filled);
        target.setColor(Color.BLACK);
        target.circle(targetxPosition, targetyPosition, metersToPixel(targetRadius));
        target.end();

        ball.begin(ShapeRenderer.ShapeType.Filled);
        ball.circle(xPosition, yPosition, metersToPixel(ballRadius));
        ball.end();

        generateObstacles();
        obstacles.end();

        if (!gameIsPlaying) {
            lost.begin();
            font.draw(lost, "You " + status + ", press space to restart! :)", Gdx.graphics.getWidth() / 2 - 110,
                    Gdx.graphics.getHeight() - 20);
            lost.end();
            return;
        }

        position.begin();
        font.draw(position, "X: " + (xPosition - originX) + " Y: " + (yPosition - originY), 20,
                Gdx.graphics.getHeight() - 20);
        position.end();

        shot.begin();
        font.draw(shot, "Shots: " + shotCounter, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);
        shot.end();

        launchVector.begin(ShapeRenderer.ShapeType.Line);
        getIntensity(launchVector);
        launchVector.end();
    }

    /**
     * This method is used to update the display of the x- and y- position in the
     * UI.
     * It is called from the PhysicsEngine.java class.
     * 
     * @param vector The state vector containing the updated x- and y- position.
     */
    public static void update_position(StateVector vector) {
        xPosition = originX + metersToPixel(convert(vector.getXPos()));
        yPosition = originY + metersToPixel(convert(vector.getYPos()));
    }

    /**
     * This method converts a double value into a float value. This is needed
     * because values
     * from the PhysicsEngine.java class arrive as double values and the UI works
     * with float
     * values.
     * 
     * @param d A double value
     * @return The double value converted to float
     */
    private static float convert(double d) {
        Double tmp = Double.valueOf(d);
        return tmp.floatValue();
    }

    private static float metersToPixel(float e) {
        return e * 100;
    }

    /**
     * This method is used to update the UI before a new shot is started in case
     * the user decided to change the position and size of the target or the size
     * of the ball.
     */
    public void prepare_new_shot() {
        input = ((PhysicsEngine) engine).get_input();
        targetxPosition = metersToPixel(convert(input[2])) + originX;
        targetyPosition = metersToPixel(convert(input[3])) + originY;
        targetRadius = convert(input[4]);
        ballRadius = convert(((PhysicsEngine) engine).ballRadius);
    }

    /**
     *
     * Generate obstacles
     *
     */
    private void generateObs() {

        while (gameIsPlaying) {
            obs1 = new Vector2(rand.nextInt(Gdx.graphics.getWidth() + 100) - Gdx.graphics.getWidth() / 2 - 100,
                    rand.nextInt(Gdx.graphics.getHeight() + 100) - Gdx.graphics.getHeight() / 2 - 100);
            obs2 = new Vector2(rand.nextInt(Gdx.graphics.getWidth() + 100) - Gdx.graphics.getWidth() / 2 - 100,
                    rand.nextInt(Gdx.graphics.getHeight() + 100) - Gdx.graphics.getHeight() / 2 - 100);
        }
    }

    /**
     *
     * Sets the obstacles to a random location
     *
     */
    private void generateObstacles() {

        obstacles.begin(ShapeRenderer.ShapeType.Filled);
        obstacles.setColor(0.62f, 0.32f, 0.20f, 1);
        obstacles.circle((float) Math.random() + width / 2, (float) originY + height / 2, 33);
        obstacles.circle((float) Math.random() + width / 2, (float) originY + height / 2, 33);
    }

    /**
     *
     * Check if two circles are colliding
     *
     * @param x1     circle 1 x
     * @param x2     circle 2 x
     * @param y1     circle 1 y
     * @param y2     circle 2 y
     * @param c1     circle 1 radius
     * @param c2     circle 2 radius
     * @param offset offset
     * @return boolean
     */
    private boolean colliding(float x1, float x2, float y1, float y2, int c1, int c2, int offset) { // checks if C1 is
                                                                                                    // inside C2
        double d = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        if (c1 + offset > (d + c2)) // + 10 gives an offset so that they don't have to be exactly inside each others
            return true;
        return false;
    }

    // checking for collsions of the obstacles
    private void checkCollisions(Vector2 ballVector) {

        if (colliding((float) InputModule.get_input()[3], (float) InputModule.get_input()[6],
                (float) InputModule.get_input()[4],
                (float) InputModule.get_input()[7], 28, 15, 10)) { // if the ball collides with the hole, win
            status = "won";
            gameIsPlaying = false;
        }

        if (colliding((int) obs1.x, (float) InputModule.get_input()[6], (int) obs1.y,
                (float) InputModule.get_input()[7], 28, 15, 28)) {
            status = "lost";
            gameIsPlaying = false;
        }

        if (colliding((int) obs2.x, (float) InputModule.get_input()[6], (int) obs2.y,
                (float) InputModule.get_input()[7], 28, 15, 28)) {
            status = "lost";
            gameIsPlaying = false;
        }
    }

    @Override
    public void dispose() {
        lost.dispose();
        ((PhysicsEngine) engine).abort();
        executor.shutdown();
        executor.shutdownNow();
    }

    /**
     * This method calculates the coloration of the course based on the height
     * of the course in every position. (?)
     */
    private void generateField() {
        Function profile = InputModule.get_profile();

        int sizeX = Gdx.graphics.getWidth();
        int sizeY = Gdx.graphics.getHeight();

        for (float i = 0; i <= sizeX; i += 10) {
            for (float j = 0; j <= sizeY; j += 10) {
                float x = (i - originX) / 100;
                float y = (j - originY) / 100;

                float n = (float) Derivation.compute(x, y, profile);

                if (n < 0) {
                    if (Math.abs(n) < 0.3f) {
                        color.add(new float[] { 0, 0, 0.3f, 1 });
                    } else {
                        color.add(new float[] { 0, 0, Math.abs(n), 1 });
                    }

                } else {
                    if (n < 0.3f) {
                        color.add(new float[] { 0, 0.3f, 0, 1 });
                    } else {
                        color.add(new float[] { 0, n, 0, 1 });
                    }
                }
                map.add(new float[] { i, j, 10, 10 });
            }
        }
    }

    /**
     *
     * Create the map from the generation every frame
     *
     */
    private void createMap() {
        int sizeX = Gdx.graphics.getWidth();
        int sizeY = Gdx.graphics.getHeight();
        int counter = 0;
        for (int i = 0; i < sizeX; i += 10) {
            for (int j = 0; j <= sizeY; j += 10) {
                try {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(color.get(counter)[0], color.get(counter)[1], color.get(counter)[2],
                            color.get(counter)[3]);
                    shapeRenderer.rect(map.get(counter)[0], map.get(counter)[1], map.get(counter)[2],
                            map.get(counter)[3]);
                    counter++;
                    shapeRenderer.end();
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    private void getIntensity(ShapeRenderer launchVector) { // get the intensity of the launch vector
        if (in.finalVectorX != 0) {
            launchVector.line(xPosition, yPosition, in.finalVectorX, (originY * 2) - in.finalVectorY);
        }

        if (euclideanDistance(in.finalVectorX, in.downX, in.finalVectorY, in.downY) > 300 && in.finalVectorX != 0) {
            launchVector.setColor(1, 0, 0, 1);
        } else if (euclideanDistance(in.finalVectorX, in.downX, in.finalVectorY, in.downY) > 150
                && in.finalVectorX != 0) {
            launchVector.setColor(1, 1, 0.4f, 1);
        } else {
            launchVector.setColor(1, 1, 1, 1);
        }
    }

    private double euclideanDistance(int x2, int x1, int y2, int y1) { // euclidian distance between two points
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public class InputHandling implements InputProcessor {
        private int downX;
        private int downY;
        public double distanceX;
        public double distanceY;
        public double finalVelocity;
        public int finalVectorX;
        public int finalVectorY;

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ENTER && !shotActive && newShotPossible) {
                String x = JOptionPane.showInputDialog("Insert x speed:");
                String y = JOptionPane.showInputDialog("Insert y speed:");
                InputModule.set_new_velocity(Double.parseDouble(x), Double.parseDouble(y));
                prepare_new_shot();
                executor.execute(engine);
            } else if (keycode == Input.Keys.ALT_RIGHT) {
                bot = new StochasticBot((PhysicsEngine) engine, 50);
                double[] vel = bot.getBestMove();
                InputModule.set_new_velocity(vel[0], vel[1]);
                prepare_new_shot();
                executor.execute(engine);
                System.out.println("Shot! Distance from target: " + vel[2]);

            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            this.downX = screenX;
            this.downY = screenY;
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            this.distanceX = (downX - screenX) / 40;
            this.distanceY = (screenY - downY) / 40;
            finalVelocity = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

            while (finalVelocity > 5) {
                if (distanceX > 0)
                    distanceX = distanceX - 0.1;
                else
                    distanceX = distanceX + 0.1;

                if (distanceY > 0)
                    distanceY = distanceY - 0.1;
                else
                    distanceY = distanceY + 0.1;

                finalVelocity = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
            }

            if (!shotActive && newShotPossible) {
                InputModule.set_new_velocity(distanceX, distanceY);
                prepare_new_shot();
                executor.execute(engine);
            }
            finalVectorX = 0;
            finalVectorY = 0;
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (!shotActive) {
                finalVectorX = screenX;
                finalVectorY = screenY;
            }

            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }

    }
}
