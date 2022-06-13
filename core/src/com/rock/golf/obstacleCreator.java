package com.rock.golf;

import java.util.Collections;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.rock.golf.Physics.Engine.rectangleObstacle;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Physics.Engine.Derivation;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;
import org.mariuszgromada.math.mxparser.Function;

public class obstacleCreator implements InputProcessor {

    private PhysicsEngine physics;
    private RockGolf golf;
    private Function golfCourse = InputModule.getProfile();
    private double position[] = new double[2];
    private float[] defaultTree;
    private float[] defaultRectangle;
    private int treeRadius = 40;
    private boolean clickTreeFlag = false;
    private boolean clickRectangleFlag = false;
    private boolean isInWater = false;
    static boolean horizontal = true;

    public obstacleCreator(RockGolf golf) {
        this.golf = golf;
        this.defaultTree = golf.treePosition;
        this.defaultRectangle = golf.rectanglePosition;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.B) {
            golf.switchToObstacle();
        }

        if (keycode == Input.Keys.R && clickRectangleFlag) {
            horizontal = !horizontal;

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

        if (clickInsideTree(screenX, (int) RockGolf.height - screenY)) {
            clickTreeFlag = true; // to add that the tree has to not be selected already
            golf.renderTextTree = false;
        } else {
            clickTreeFlag = false;
        }

        if (!clickTreeFlag && !golf.renderTextTree) {
            if (!(Derivation.compute((screenX - RockGolf.originX) / RockGolf.metertoPixelRatio,
                    ((RockGolf.height - screenY) - RockGolf.originY) / RockGolf.metertoPixelRatio,
                    golfCourse) < 0)) {
                PhysicsEngine.trees
                        .add(new Tree(
                                new double[] { (screenX - RockGolf.originX) / RockGolf.metertoPixelRatio,
                                        ((RockGolf.height - screenY) - RockGolf.originY) / RockGolf.metertoPixelRatio },
                                0.4));
                golf.renderTextTree = true;
                golf.treePosition = defaultTree;
            } else {
                golf.renderTextTree = true;
                golf.treePosition = defaultTree;
                golf.state = "game";
            }
        }
        if (clickInsideRectangle(screenX, screenY)) {
            clickRectangleFlag = true;
            golf.renderTextRect = false;
        } else {
            clickRectangleFlag = false;
        }

        if (!clickRectangleFlag && !golf.renderTextRect) {
            float[] rectanglePosition = new float[] { (screenX - RockGolf.originX) / RockGolf.metertoPixelRatio,
                    ((RockGolf.height - screenY) - RockGolf.originY) / RockGolf.metertoPixelRatio };
            if (horizontal)
                PhysicsEngine.rectangles.add(new rectangleObstacle(rectanglePosition, 130, 20));
            else
                PhysicsEngine.rectangles.add(new rectangleObstacle(rectanglePosition, 20, 130));
            golf.renderTextRect = true;
            golf.rectanglePosition = defaultRectangle;
            horizontal = true;
        }

        return false;
    }

    private boolean clickInsideTree(int screenX, int screenY) {
        return Math.sqrt(Math.pow(Math.abs(screenX - defaultTree[0]), 2)
                + Math.pow(Math.abs(screenY - defaultTree[1]), 2)) <= treeRadius;
    }

    private boolean clickInsideRectangle(int screenX, int screenY) {
        return (screenX > defaultRectangle[0] && screenX < defaultRectangle[0] + 130
                && RockGolf.height - screenY > defaultRectangle[1]
                && RockGolf.height - screenY < defaultRectangle[1] + 20);
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

        if (clickRectangleFlag) {
            if (horizontal)
                golf.rectanglePosition = new float[] { screenX - 60, Math.abs(screenY - RockGolf.height) };
            else
                golf.rectanglePosition = new float[] { screenX - 10, Math.abs(screenY - RockGolf.height) };
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
