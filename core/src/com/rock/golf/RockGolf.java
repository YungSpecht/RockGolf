package com.rock.golf;

import java.nio.channels.WritableByteChannel;
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
import com.rock.golf.Bot.AIBot;
import com.rock.golf.Bot.AiBot2;
import com.rock.golf.Bot.Bruteforce;
import com.rock.golf.Bot.PSOBot;
import com.rock.golf.Bot.RuleBasedBot;
import com.rock.golf.Bot.SteepestDescent;
import com.rock.golf.Bot.StochasticBot;
import com.rock.golf.Input.*;
import com.rock.golf.Math.Derivation;

import org.mariuszgromada.math.mxparser.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RockGolf extends ApplicationAdapter {
    public static final float metertoPixelRatio = 100;
    public static float width;
    public static float height;
    float ballRadius;
    private float targetRadius;
    private static float originX;
    private static float originY;
    private static float xPosition;
    private static float yPosition;
    private float targetxPosition;
    private float targetyPosition;
    private List<Sandpit> sandpits;
    private ShapeRenderer ball;
    private ShapeRenderer sandpit;
    private ShapeRenderer target;
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer launchVector;
    private ArrayList<float[]> map = new ArrayList<>();
    private ArrayList<float[]> color = new ArrayList<>();
    private double[] input;
    private double[] initialState;
    private Runnable engine;
    private StochasticBot stochasticBot;
    private Bruteforce hillClimb;
    private AIBot veryDumbBot;
    private AiBot2 botWhosMid;
    private SteepestDescent steepestDescent;
    private RuleBasedBot RuleBasedBot;
    private PSOBot PSOBot;
    private ExecutorService executor;
    private SpriteBatch position, shot;
    private BitmapFont font;
    public static int shotCounter;
    public static boolean shotActive;
    public static boolean newShotPossible;
    public InputHandling in = new InputHandling();

    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        originX = width / 2;
        originY = height / 2;
        ball = new ShapeRenderer();
        target = new ShapeRenderer();
        sandpit = new ShapeRenderer();
        launchVector = new ShapeRenderer();
        engine = new PhysicsEngine(0.05, 'h');
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
        initialState = new double[] { input[5], input[6] };
        generateField();
        sandpits = ((PhysicsEngine)engine).get_sandpits();
        shotActive = false;
        newShotPossible = true;
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        createMap();
        for(int i = 0; i < sandpits.size(); i++){
            double[] pos = sandpits.get(i).get_position();
            sandpit.begin(ShapeRenderer.ShapeType.Filled);
            sandpit.setColor(255, 255, 0, 1);
            sandpit.circle(metersToPixel(convert(pos[0])) + originX, metersToPixel(convert(pos[1])) + originY, metersToPixel(convert(sandpits.get(i).get_radius())));
            sandpit.end();
        }

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
        return e * metertoPixelRatio;
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

    @Override
    public void dispose() {
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
            PhysicsEngine botEngine = new PhysicsEngine(0.1, 'h');
            if (keycode == Input.Keys.ENTER && !shotActive && newShotPossible) {
                String x = JOptionPane.showInputDialog("Insert x speed:");
                String y = JOptionPane.showInputDialog("Insert y speed:");
                InputModule.set_new_velocity(Double.parseDouble(x), Double.parseDouble(y));
                prepare_new_shot();
                executor.execute(engine);
            } else if (keycode == Input.Keys.ALT_RIGHT) {
                stochasticBot = new StochasticBot(botEngine, 1);
                double[] vel = stochasticBot.getMove();
                InputModule.set_new_velocity(vel[0], vel[1]);
                prepare_new_shot();
                executor.execute(botEngine);
                System.out.println("Shot! Distance from target: " + vel[2]);

            } else if (keycode == Input.Keys.CONTROL_RIGHT) {
                hillClimb = new Bruteforce(botEngine, 1);
                double[] vel = hillClimb.getMove();
                InputModule.set_new_velocity(vel[0], vel[1]);
                prepare_new_shot();
                executor.execute(botEngine);

            } else if(keycode == Input.Keys.EQUALS){
                steepestDescent = new SteepestDescent(botEngine);
                double[] shot = steepestDescent.get_shot();
                InputModule.set_new_velocity(shot[0], shot[1]);
                prepare_new_shot();
                executor.execute(engine);
            } else if (keycode == Input.Keys.TAB) {
                veryDumbBot = new AIBot(botEngine);
                double[] shot = veryDumbBot.get_shot();
                InputModule.set_new_velocity(shot[0], shot[1]);
                prepare_new_shot();
                executor.execute(botEngine);
        
            } else if(keycode == Input.Keys.BACKSLASH){
                botWhosMid = new AiBot2(botEngine);
                double[] shot = botWhosMid.get_shot();
                InputModule.set_new_velocity(shot[0], shot[1]);
                prepare_new_shot();
                executor.execute(botEngine);

            } else if (keycode == Input.Keys.ESCAPE) {
                xPosition = metersToPixel(convert(initialState[0])) + originX;
                yPosition = metersToPixel(convert(initialState[1])) + originY;
                InputModule.set_new_position(initialState[0], initialState[1]);
                shotCounter = 0;
                ((PhysicsEngine) engine).resume();
                newShotPossible = true;
            } else if (keycode == Input.Keys.V) {
                // PSOBot = new PSOBot(botEngine, 50);
                // double[] vel = PSOBot.run();
                // InputModule.set_new_velocity(vel[0], vel[1]);
                // prepare_new_shot();
                // executor.execute(botEngine);
                RuleBasedBot = new RuleBasedBot(botEngine);
                double[] vel = RuleBasedBot.getMove();
                InputModule.set_new_velocity(vel[0], vel[1]);
                prepare_new_shot();
                executor.execute(botEngine);
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
