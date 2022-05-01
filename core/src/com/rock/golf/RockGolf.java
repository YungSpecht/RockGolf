package com.rock.golf;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rock.golf.Input.*;
import com.rock.golf.Math.Derivation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RockGolf extends ApplicationAdapter {
    float ballRadius;
    float targetRadius;
    final static float originX = 400;
    final static float originY = 400;
    static float xPosition;
    static float yPosition;
    float targetxPosition;
    float targetyPosition;
    ShapeRenderer shape;
    ShapeRenderer target;
    ShapeRenderer shapeRenderer;
    ArrayList<float[]> map = new ArrayList<>();
	ArrayList<float[]> color = new ArrayList<>();
    double[] input;
    Runnable engine;
    public static ExecutorService executor;
    SpriteBatch position, shot;
    BitmapFont font;
    public static int shotCounter;

    @Override
    public void create () {
        shape = new ShapeRenderer();
        target = new ShapeRenderer();
        engine = new PhysicsEngine();
        input = ((PhysicsEngine) engine).get_input();
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
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        createMap();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)  ){
            
            
            executor.execute(engine); 
            prepare_new_shot();
        }

        target.begin(ShapeRenderer.ShapeType.Filled);
        target.setColor(Color.BLACK);
        target.circle(targetxPosition, targetyPosition, targetRadius * 100);
        target.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.circle(xPosition, yPosition, ballRadius * 100);
        shape.end();

        position.begin();
        font.draw(position, "X: " + (xPosition-originX) + " Y: " + (yPosition-originY), 20 , 775);
        position.end();

        shot.begin();
        font.draw(shot, "Shots: " + shotCounter, 700, 775);
        shot.end();
    }

    public static void  update_position(StateVector vector){
        xPosition = originX + convert(vector.getXPos()) * 100;
        yPosition = originY + convert(vector.getYPos()) * 100;
    }

    private static float convert(double d){
        Double tmp = Double.valueOf(d);
        return tmp.floatValue();
    }

    private void prepare_new_shot(){
        input = ((PhysicsEngine) engine).get_input();
        targetxPosition = convert(input[2]) * 100 + originX;
        targetyPosition = convert(input[3]) * 100 + originY;
        targetRadius = convert(input[4]);
        ballRadius = convert(((PhysicsEngine) engine).ballRadius);
    }

    @Override
    public void dispose(){
        executor.shutdown();
    }

    private void generateField() { 

		int sizeX = Gdx.graphics.getWidth();
		int sizeY = Gdx.graphics.getHeight();
        for(float i = 0; i <= sizeX; i+= 20) {
            for(float j = 0; j <= sizeY; j+= 20) {
                float x = i / 100;
                float y = j / 100;
				//float n = (float) (0.1*x - 1);
				float n = 0;
					n = (float) Derivation.compute(x-4, y-4, InputModule.get_profile());
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
