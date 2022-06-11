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

            mouseX = Gdx.input.getX();
            mouseY = Gdx.input.getY();

            // pseudo obstacle used to be able to see where you place the obstacle in the
            // end
            ShapeRenderer Rectangle = new ShapeRenderer();
            Rectangle.begin(ShapeRenderer.ShapeType.Filled);
            Rectangle.setColor(Color.BLACK);
            Rectangle.rect((float) mouseX, (float) mouseY, 150, 50);
            Rectangle.end();

            position[0] = mouseX;
            position[1] = mouseY;

            new Obstacle(position, 150, 50);
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
