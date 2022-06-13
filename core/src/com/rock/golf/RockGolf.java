package com.rock.golf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rock.golf.Bot.AStar1;
import com.rock.golf.Input.*;
import com.rock.golf.Pathfinding.BFS;
import com.rock.golf.Pathfinding.Graph;
import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Physics.Engine.Derivation;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Sandpit;
import com.rock.golf.Physics.Engine.StateVector;
import com.rock.golf.Physics.Engine.Tree;
import com.rock.golf.Physics.Engine.rectangleObstacle;

import org.mariuszgromada.math.mxparser.Function;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main GUI class
 */

public class RockGolf extends ApplicationAdapter {

    public final static float metertoPixelRatio = 100;
    String state = "game";
    public static boolean winStatus;
    public static boolean losingStatus;
    public static boolean collisionTreeStatus;
    public static float width;
    public static float height;
    private float ballRadius;
    private float targetRadius;
    static float originX;
    static float originY;
    static float xPosition;
    static float yPosition;
    private float targetxPosition;
    private float targetyPosition;
    private List<Sandpit> sandpits;
    public static List<Tree> trees;
    private ShapeRenderer ball;
    private ShapeRenderer sandpit;
    private ShapeRenderer tree;
    private ShapeRenderer target;
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer launchVector;
    private ShapeRenderer rectangle;
    private ArrayList<float[]> map = new ArrayList<>();
    private ArrayList<float[]> color = new ArrayList<>();
    private double[] input;
    double[] initialState;
    public static Runnable engine;
    ExecutorService executor;
    private SpriteBatch position, shot, endGame;
    private BitmapFont font;
    public static int shotCounter;
    public static boolean shotActive;
    public static boolean newShotPossible;
    public InputHandling in = new InputHandling();
    public boolean renderTextTree = true;
    public boolean renderTextRect = true;
    private ShapeRenderer background;
    public float[] treePosition;
    public float[] rectanglePosition;
    private SpriteBatch water;
    static Node[][] graph;
    Graph graphClass;
    public boolean showGraph = false;
    private static ShapeRenderer graphNodes;
    private obstacleCreator obstacleCreate = new obstacleCreator(this);
    private double mouseX;
    private double mouseY;
    private double mousePosition[] = new double[2];
    private List<rectangleObstacle> rectangles;

    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        originX = width / 2;
        originY = height / 2;
        treePosition = new float[] { 270, originY * 2 - 50 };
        rectanglePosition = new float[] { 360, originY * 2 - 75 };
        rectangle = new ShapeRenderer();
        endGame = new SpriteBatch();
        ball = new ShapeRenderer();
        target = new ShapeRenderer();
        sandpit = new ShapeRenderer();
        graphNodes = new ShapeRenderer();
        background = new ShapeRenderer();
        tree = new ShapeRenderer();
        launchVector = new ShapeRenderer();
        engine = new PhysicsEngine(0.01, 'h');
        executor = Executors.newFixedThreadPool(1);
        Gdx.input.setInputProcessor(in);
        position = new SpriteBatch();
        water = new SpriteBatch();
        shot = new SpriteBatch();
        font = new BitmapFont();
        shotCounter = 0;
        shapeRenderer = new ShapeRenderer();
        prepareNewShot();
        xPosition = metersToPixel(convert(input[5])) + originX;
        yPosition = metersToPixel(convert(input[6])) + originY;
        initialState = new double[] { input[5], input[6] };
        generateField();
        sandpits = ((PhysicsEngine) engine).get_sandpits();
        rectangles = ((PhysicsEngine) engine).get_rectangles();
        trees = ((PhysicsEngine) engine).get_trees();
        shotActive = false;
        newShotPossible = true;
        graphClass = new Graph();
        graph = graphClass.generateMatrix();
    }

    @Override
    public void render() {

        Gdx.gl.glEnable(GL20.GL_BLEND);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        createMap();

        if (state.equals("menu")) {
            renderMenu();
            generateObstacles();
            return;
        }

        if (state.equals("OBS menu")) {
            renderObstacleMenu(renderTextTree, renderTextRect, treePosition, rectanglePosition, null);
            generateObstacles();
            return;
        }

        generateObstacles();
        checkStuckStatus();

        target.begin(ShapeRenderer.ShapeType.Filled);
        target.setColor(Color.BLACK);
        target.circle(targetxPosition, targetyPosition, metersToPixel(targetRadius));
        target.end();

        ball.begin(ShapeRenderer.ShapeType.Filled);
        ball.circle(xPosition, yPosition, metersToPixel(ballRadius));
        ball.end();

        position.begin();
        font.draw(position, "X: " + (xPosition - originX) + " Y: " + (yPosition - originY), 20,
                Gdx.graphics.getHeight() - 20);
        position.end();

        shot.begin();
        font.draw(shot, "Shots: " + shotCounter, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);
        shot.end();

        checkLosingStatus();

        launchVector.begin(ShapeRenderer.ShapeType.Line);
        getIntensity(launchVector);
        launchVector.end();
    }

    /**
     *
     * Check if the engine is stuck
     *
     */

    private void checkStuckStatus() {

        if (!((PhysicsEngine) engine).stuck)
            return;
        water.begin();
        font.draw(water, "Oh no, you got stuck! Press esc to reset.", 300, Gdx.graphics.getHeight() - 20);
        water.end();
    }

    /**
     *
     * Generate obstacles
     *
     */

    private void generateObstacles() {

        for (int i = 0; i < sandpits.size(); i++) {
            double[] pos = sandpits.get(i).getPosition();
            sandpit.begin(ShapeRenderer.ShapeType.Filled);
            sandpit.setColor(255, 255, 0, 1);
            sandpit.circle(metersToPixel(convert(pos[0])) + originX, metersToPixel(convert(pos[1])) + originY,
                    metersToPixel(convert(sandpits.get(i).getRadius())));
            sandpit.end();
        }

        for (int i = 0; i < trees.size(); i++) {
            double[] pos = trees.get(i).getPosition();
            tree.begin(ShapeRenderer.ShapeType.Filled);
            tree.setColor(new Color(0.3f, 0, 0, 1f));
            tree.circle(metersToPixel(convert(pos[0])) + originX, metersToPixel(convert(pos[1])) + originY,
                    metersToPixel(convert(trees.get(i).getRadius())));
            tree.end();
        }

        for (int i = 0; i < rectangles.size(); i++) {
            float[] pos = rectangles.get(i).getPosition();
            double height = rectangles.get(i).getHeight();
            double width = rectangles.get(i).getWidth();
            rectangle.begin(ShapeRenderer.ShapeType.Filled);
            rectangle.setColor(new Color(0.3f, 0, 0, 1f));
            rectangle.rect(metersToPixel(pos[0]) + originX, metersToPixel(pos[1]) + originY, (float) width, (float) height);
            rectangle.end();

        }
    }

    /**
     *
     * Check losing status
     *
     */

    private void checkLosingStatus() {

        if (winStatus == true && shotCounter == 1) {
            endGame.begin();
            font.draw(endGame, "You got a hole in one!", (Gdx.graphics.getWidth() / 2) - 50,
                    Gdx.graphics.getHeight() - 20);
            endGame.end();
        } else if (winStatus == true && shotCounter > 1) {
            endGame.begin();
            font.draw(endGame, "You took " + shotCounter + " swings to get it in the hole!",
                    (Gdx.graphics.getWidth() / 2) - 80,
                    Gdx.graphics.getHeight() - 20);
            endGame.end();
        }
    }

    /**
     *
     * Render menu
     *
     */

    private void renderMenu() {

        background.begin(ShapeRenderer.ShapeType.Filled);
        background.setColor(new Color(0, 0, 0, 0.8f));
        background.rect(0, 0, width, height);
        background.end();

        shot.begin();
        font.draw(shot,
                "Select the bot:\n\n S: Stochastic\n B: Bruteforce\n H: HillClimb\n A: AngleBot\n R: Rule-based",
                originX - 50, originY + 100);
        shot.end();
    }

    /**
     *
     * Render Obstacle menu
     *
     */

    private void renderObstacleMenu(boolean renderTextTree, boolean renderTextRec, float[] positionTree, float[] positionRectangle,
            float[] positionObstacle) {

        shot.begin();
        font.draw(shot,
                "Click your obstacle:\n",
                10, originY * 2 - 20);
        shot.end();

        tree.begin(ShapeRenderer.ShapeType.Filled);
        tree.setColor(new Color(0.3f, 0, 0, 0.7f));
        tree.circle(positionTree[0], positionTree[1], 40);
        tree.end();

        rectangle.begin(ShapeRenderer.ShapeType.Filled);
        rectangle.setColor(new Color(0.3f, 0, 0, 0.5f));
        if(obstacleCreator.horizontal) rectangle.rect(positionRectangle[0], positionRectangle[1], (float) 130, (float) 20);
        else rectangle.rect(positionRectangle[0], positionRectangle[1], (float) 20, (float) 130);
        rectangle.end();

        

        shot.begin();
        if (renderTextTree) font.draw(shot, "Tree", 255, (originY * 2 - 45));
        if (renderTextRect) font.draw(shot, "Rectangle", 395, (originY * 2 - 59));
        shot.end();
    }

    /**
     * This method is used to update the display of the x- and y- position in the
     * UI.
     * It is called from the PhysicsEngine.java class.
     * 
     * @param vector The state vector containing the updated x- and y- position.
     */

    public static void updatePosition(StateVector vector) {

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

    public static float convert(double d) {

        Double tmp = Double.valueOf(d);
        return tmp.floatValue();
    }

    /**
     * This method converts meters to pixel using a global ratio
     * 
     * @param e meters
     * @return pixels
     */

    static float metersToPixel(float e) {
        return e * metertoPixelRatio;
    }

    /**
     * This method is used to update the UI before a new shot is started in case
     * the user decided to change the position and size of the target or the size
     * of the ball.
     */

    public void prepareNewShot() {

        input = ((PhysicsEngine) engine).getInputArray();
        targetxPosition = metersToPixel(convert(input[2])) + originX;
        targetyPosition = metersToPixel(convert(input[3])) + originY;
        targetRadius = convert(input[4]);
        ballRadius = convert(((PhysicsEngine) engine).ballRadius);
    }

    @Override
    public void dispose() {

        ((PhysicsEngine) engine).abort();
        executor.shutdown();
        executor.shutdownNow();
    }

    /**
     * This method calculates the coloration of the course based on the height
     * of the course in every position.
     */

    private void generateField() {

        Function profile = InputModule.getProfile();

        int sizeX = (int) width;
        int sizeY = (int) height;

        for (float i = 0; i <= sizeX; i += 10) {
            for (float j = 0; j <= sizeY; j += 10) {
                float x = (i - originX) / metertoPixelRatio;
                float y = (j - originY) / metertoPixelRatio;

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

                if (showGraph)
                    createNode(i, j);
            }
        }
    }

    /**
     *
     * Gets the intensity of the shot
     *
     * @param launchVector the dedicated shapeRendered
     */

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

    /**
     *
     * Euclidean distance between two points
     *
     * @param x2 the x2
     * @param x1 the x1
     * @param y2 the y2
     * @param y1 the y1
     * @return double
     */

    private double euclideanDistance(int x2, int x1, int y2, int y1) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     *
     * This class handles the user inputs
     *
     */

    public class InputHandling implements InputProcessor {

        private int downX;
        private int downY;
        private double distanceX;
        private double distanceY;
        private int finalVectorX;
        private int finalVectorY;

        @Override
        public boolean keyDown(int keycode) {
            BFS bfs = new BFS();
            int ballX = (int) xPosition / 10;
            int ballY = (int) yPosition / 10;
            int targetX = (int) targetxPosition / 10;
            int targetY = (int) targetyPosition / 10;

            if (keycode == Input.Keys.ENTER && !shotActive && newShotPossible) {
                String x = JOptionPane.showInputDialog("Insert x speed:");
                String y = JOptionPane.showInputDialog("Insert y speed:");
                InputModule.setNewVelocity(Double.parseDouble(x), Double.parseDouble(y));
                prepareNewShot();
                executor.execute(engine);
            } else if (keycode == Input.Keys.ESCAPE) {
                xPosition = metersToPixel(convert(initialState[0])) + originX;
                yPosition = metersToPixel(convert(initialState[1])) + originY;
                InputModule.setNewPosition(initialState[0], initialState[1]);
                if (showGraph)
                    showGraph = !showGraph;
                shotCounter = 0;
                ((PhysicsEngine) engine).stuck = false;
                ((PhysicsEngine) engine).resume();
                newShotPossible = true;
            } else if (keycode == Input.Keys.M) {
                switchState();
            } else if (keycode == Input.Keys.D) {
                System.out.println(((PhysicsEngine) engine).tolerance);
            } else if (keycode == Input.Keys.B) {
                switchToObstacle();
            } else if (keycode == Input.Keys.P) {
                //BFS.BFSSearch(graphClass,graph[1][1], graph[60][40]);
            } else if (keycode == Input.Keys.A) {
                AStar1.findPath(graph[1][1], graph[60][40],graphClass);
            } else if (keycode == Input.Keys.G) {
                showGraph = !showGraph;
                if (showGraph) {
                    graph = graphClass.generateMatrix();
                    bfs.BFSSearch(graphClass, graph[ballX][ballY], graph[targetX][targetY]);
                }
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

            this.distanceX = (downX - screenX) / 100;
            this.distanceY = (screenY - downY) / 100;
            double finalVelocity = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

            if (finalVelocity > 5) {
                double[] vel = normalizeVelocity(new double[] { distanceX, distanceY }, 5);
                distanceX = vel[0];
                distanceY = vel[1];
            }
            if (!shotActive && newShotPossible) {
                InputModule.setNewVelocity(distanceX, distanceY);
                prepareNewShot();
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

        /**
         *
         * Normalize velocity
         *
         * @param velocities the velocities
         * @param velocity   the velocity
         * @return double[]
         */

        private double[] normalizeVelocity(double[] velocities, double velocity) {

            double currentVel = Math.sqrt(Math.pow(velocities[0], 2) + Math.pow(velocities[1], 2));
            double scalar = velocity / currentVel;
            return new double[] { velocities[0] * scalar, velocities[1] * scalar };

        }
    }

    /**
     *
     * Switch state from game to obstacle creator
     *
     */

    public void switchToObstacle() {

        if (state.equals("OBS menu")) {
            state = "game";
            Gdx.input.setInputProcessor(in);
        } else {
            state = "OBS menu";
            Gdx.input.setInputProcessor(new obstacleCreator(this));
        }
    }

    /*
     *
     * Switch state from game to menu
     *
     */

    public void switchState() {

        if (state.equals("menu")) {
            state = "game";
            Gdx.input.setInputProcessor(in);
        } else {
            state = "menu";
            Gdx.input.setInputProcessor(new BotHandler(this, (PhysicsEngine) engine));
        }
    }

    public static void createNode(int i, int j) {
        try {
            if (graph[i / 10][j / 10].currentNodeValue == 0) {
                return;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }

        graphNodes.begin(ShapeRenderer.ShapeType.Filled);
        if (graph[i / 10][j / 10].isPath)
            graphNodes.setColor(Color.RED);
        graphNodes.circle(i, j, 2);
        graphNodes.setColor(Color.WHITE);
        graphNodes.end();

        graphNodes.begin(ShapeRenderer.ShapeType.Line);

        if (i / 10 + 1 < graph.length && graph[i / 10 + 1][j / 10].currentNodeValue != 0) {
            graphNodes.line(i, j, i + 10, j);
        }

        if (j / 10 + 1 < graph[0].length && graph[i / 10][j / 10 + 1].currentNodeValue != 0) {
            graphNodes.line(i, j, i, j + 10);
        }

        graphNodes.end();
    }
}
