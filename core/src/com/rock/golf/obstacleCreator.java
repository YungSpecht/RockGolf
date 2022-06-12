package com.rock.golf;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;
import com.rock.golf.Physics.Engine.rectangleObstacle;

public class obstacleCreator implements InputProcessor {

    private PhysicsEngine physics;
    private RockGolf golf;
    private double position[] = new double[2];
    private float[] defaultTree;
    private float[] defaultRectangle;
    private int treeRadius = 40;
    private boolean clickTreeFlag = false;
    private boolean clickRectangleFlag = false;

    public obstacleCreator(RockGolf golf) {
        this.golf = golf;
        this.physics = physics;
        this.defaultTree = golf.treePosition;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.B) {
            golf.switchToObstacle();
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
        clickTreeFlag = false;
        clickRectangleFlag = false;

        if (clickInsideTree(screenX, (int) RockGolf.height - screenY)) {
            clickTreeFlag = true; // to add that the tree has to not be selected already
            golf.renderText = false;
        }

        if (!clickTreeFlag) {
            PhysicsEngine.trees
                    .add(new Tree(
                            new double[] { (screenX - RockGolf.originX) / RockGolf.metertoPixelRatio,
                                    ((RockGolf.height - screenY) - RockGolf.originY) / RockGolf.metertoPixelRatio },
                            0.4));
            golf.renderText = true;
            golf.treePosition = defaultTree;
        }

        if (clickInsideRectangle(screenX, screenY))

            if (!clickRectangleFlag) {
                PhysicsEngine.rectangles.add(new rectangleObstacle(
                        (new double[] { (screenX - RockGolf.originX) / RockGolf.metertoPixelRatio,
                                ((RockGolf.height - screenY) - RockGolf.originY) / RockGolf.metertoPixelRatio }),
                        150, 50));
                golf.renderText = true;
                golf.rectanglePosition = defaultRectangle;

            }

        return false;
    }

    private boolean clickInsideTree(int screenX, int screenY) {
        return Math.sqrt(Math.pow(Math.abs(screenX - defaultTree[0]), 2)
                + Math.pow(Math.abs(screenY - defaultTree[1]), 2)) <= treeRadius;
    }

    private boolean clickInsideRectangle(int screenX, int screenY) {
        return (screenX > defaultRectangle[0] && screenX < defaultRectangle[2] && screenY > defaultRectangle[1]
                && screenY < defaultRectangle[3]);
        // default rectangle[0] and [2] is x1 and x2 and [1] and [3] is y1 and y2
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
        if (clickTreeFlag) {
            golf.treePosition = new float[] { screenX, Math.abs(screenY - RockGolf.height) };
        }
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
