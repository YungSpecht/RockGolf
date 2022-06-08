package com.rock.golf;

import java.awt.MouseInfo;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.rock.golf.Physics.Engine.Obstacle;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;

public class obstacleCreator implements InputProcessor {

    private RockGolf golf;
    private PhysicsEngine physics;
    private Obstacle obstacle;

    public obstacleCreator(RockGolf rockGolf, PhysicsEngine engine) {
        this.golf = rockGolf;
        physics = engine;

    }

    @Override
    public boolean keyDown(int keycode) {

        golf.switchToObstacle();

        if (keycode == Input.Keys.R) {

            double position[] = new double[2];

            double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
            double mouseY = MouseInfo.getPointerInfo().getLocation().getY();

            // pseudo obstacle used to be able to see where you place the obstacle in the
            // end
            ShapeRenderer Rectangle = new ShapeRenderer();
            Rectangle.begin(ShapeRenderer.ShapeType.Filled);
            Rectangle.setColor(Color.BLACK);
            Rectangle.rect((float) mouseX, (float) mouseY, 150, 50);

            position[0] = mouseX;
            position[1] = mouseY;

            new Obstacle(position, 150, 50);

        } else if (keycode == Input.Keys.T) {

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
}
