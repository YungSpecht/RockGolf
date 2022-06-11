package com.rock.golf;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rock.golf.Physics.Engine.Obstacle;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;

public class obstacleCreator implements InputProcessor {

    private double mouseX;
    private double mouseY;
    private PhysicsEngine physics;
    private RockGolf golf;
    private double position[] = new double[2];
    private boolean flag = false;

    public obstacleCreator(RockGolf golf, PhysicsEngine physics) {
        this.golf = golf;
        this.physics = physics;
    }

    @Override
    public boolean keyDown(int keycode) {

        golf.switchToObstacle();

        if (keycode == Input.Keys.R) {


            
            while(keycode == Input.Keys.E){

                if(keycode == Input.Keys.W){
                    double mouseX = Gdx.input.getX();
                    double mouseY = Gdx.input.getY();
                    position[0] = mouseX;
                    position[1] = mouseY;

                    if(keycode == Input.Keys.C){
                        double new_mouseX = Gdx.input.getX();
                        double new_mouseY = Gdx.input.getY();
                        position[2] = new_mouseX;
                        position[3] = new_mouseY;

                        ShapeRenderer rectangle = new ShapeRenderer();
                        rectangle.begin(ShapeRenderer.ShapeType.Filled);
                        rectangle.setColor(Color.BLACK);
                        rectangle.rect(RockGolf.convert(mouseX), RockGolf.convert(mouseY), RockGolf.convert(new_mouseX)- RockGolf.convert(mouseX), RockGolf.convert(new_mouseY)- RockGolf.convert(mouseY));
                        rectangle.end();
                        new Obstacle(position, position[2]-position[0], position[3]-position[1]);

                        
                    }

                }

            }

            // pseudo obstacle used to be able to see where you place the obstacle in the
            // end
            

            
            
        }

        else if (keycode == Input.Keys.T) {
            golf.switchToObstacle();
            while (flag == false) {
                mouseX = Gdx.input.getX();
                mouseY = Gdx.input.getY();
                if (keycode == Input.Keys.C) {
                    flag = true;
                }
            }
            position[0] = mouseX;
            position[1] = mouseY;
            Tree tree = new Tree(position, 2);
            List<Tree> Trees = RockGolf.trees;
            Trees.add(tree);
            RockGolf.trees = Trees;
            PhysicsEngine.trees = Trees;
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

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

    public double[] getPosition() {
        return position;
    }
}
