package com.rock.golf;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
<<<<<<< HEAD
import com.rock.golf.Bot.HillClimbingAlgorithm;
import com.rock.golf.Input.*;
import com.rock.golf.Math.Derivation;

import org.mariuszgromada.math.mxparser.Function;

=======
>>>>>>> 8858767d9f8057a9b0c871c19b7311e2f09e30b5
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private ShapeRenderer ball;
    private ShapeRenderer target;
    private ShapeRenderer shapeRenderer;
    private ArrayList<float[]> map = new ArrayList<>();
	private ArrayList<float[]> color = new ArrayList<>();
    private double[] input;
    private Runnable engine;
    private ExecutorService executor;
    private SpriteBatch position, shot;
    private BitmapFont font;
    public static int shotCounter;
    public static boolean shotActive;


    @Override
    public void create () {
        originX = Gdx.graphics.getWidth() / 2;
        originY = Gdx.graphics.getHeight() / 2;
        ball = new ShapeRenderer();
        target = new ShapeRenderer();
        engine = new PhysicsEngine();
        executor = Executors.newFixedThreadPool(1);
        position = new SpriteBatch();
        shot = new SpriteBatch();
        font = new BitmapFont();
        shotCounter = 0;
        shapeRenderer = new ShapeRenderer();
        prepare_new_shot();
        xPosition = convert(input[5]) * 100 + originX;
        yPosition = convert(input[6]) * 100 + originY;
        generateField();
        shotActive = false;
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        createMap();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !shotActive){
            prepare_new_shot();
            executor.execute(engine); 
        }

        target.begin(ShapeRenderer.ShapeType.Filled);
        target.setColor(Color.BLACK);
        target.circle(targetxPosition, targetyPosition, targetRadius * 100);
        target.end();

        ball.begin(ShapeRenderer.ShapeType.Filled);
        ball.circle(xPosition, yPosition, ballRadius * 100);
        ball.end();

        position.begin();
        font.draw(position, "X: " + (xPosition-originX) + " Y: " + (yPosition-originY), 20 , Gdx.graphics.getHeight() - 20);
        position.end();

        shot.begin();
        font.draw(shot, "Shots: " + shotCounter, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);
        shot.end();
    }

    /**
	 * This method is used to update the display of the x- and y- position in the UI.
     * It is called from the PhysicsEngine.java class.
     * 
     * @param vector The state vector containing the updated x- and y- position.
	 */
    public static void  update_position(StateVector vector){
        xPosition = originX + convert(vector.getXPos()) * 100;
        yPosition = originY + convert(vector.getYPos()) * 100;
    }

    /**
	 * This method converts a double value into a float value. This is needed because values
     * from the PhysicsEngine.java class arrive as double values and the UI works with float
     * values.
     * 
     * @param d A double value
     * @return The double value converted to float
	 */
    private static float convert(double d){
        Double tmp = Double.valueOf(d);
        return tmp.floatValue();
    }

    
    /**
	 * This method is used to update the UI before a new shot is started in case
     * the user decided to change the position and size of the target or the size
     * of the ball.
	 */
    private void prepare_new_shot(){
        input = ((PhysicsEngine) engine).get_input();
        targetxPosition = convert(input[2]) * 100 + originX;
        targetyPosition = convert(input[3]) * 100 + originY;
        targetRadius = convert(input[4]);
        ballRadius = convert(((PhysicsEngine) engine).ballRadius);
    }

    @Override
    public void dispose(){
        ((PhysicsEngine)engine).abort();
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
        for(float i = 0; i <= sizeX; i+= 20) {
            for(float j = 0; j <= sizeY; j+= 20) {
                float x = i / 100;
                float y = j / 100;
				//float n = (float) (0.1*x - 1);
				float n = 0;
					n = (float) Derivation.compute(x-4, y-4, profile);
				if(n < 0) {
					if (Math.abs(n) < 0.3f) {
						color.add(new float[]{0, 0, 0.3f, 1});
					} else {
						color.add(new float[]{0,0,Math.abs(n), 1});
					}
				}
				else {
					if (n < 0.3f) {
					color.add(new float[]{0, 0.3f, 0, 1});
					} else {
						color.add(new float[]{0, n, 0, 1});
					}
				}
				map.add(new float[]{ i,  j, 20,20});
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
		for(int i = 0; i < sizeX; i+=20) {
			for(int j = 0; j <= sizeY; j+=20) {
				try {
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(color.get(counter)[0], color.get(counter)[1], color.get(counter)[2], color.get(counter)[3]);
				shapeRenderer.rect(map.get(counter)[0], map.get(counter)[1], map.get(counter)[2], map.get(counter)[3]);
				counter++;
				shapeRenderer.end();
				} catch(Exception e) {return;}
			}
		}
	}

}
