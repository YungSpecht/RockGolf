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
    private boolean checkForMenu = false;
    private RockGolf golf;
    private double position[] = new double[2];
    boolean treeflag = false;

    public obstacleCreator(RockGolf golf) {
        this.golf = golf;
    }

    @Override
    public boolean keyDown(int keycode) {

        golf.switchToObstacle();

        if (keycode == Input.Keys.R) {

            while (keycode == Input.Keys.E) {

                if (keycode == Input.Keys.W) {
                    double mouseX = Gdx.input.getX();
                    double mouseY = Gdx.input.getY();
                    position[0] = mouseX;
                    position[1] = mouseY;

                    if (keycode == Input.Keys.C) {
                        double new_mouseX = Gdx.input.getX();
                        double new_mouseY = Gdx.input.getY();
                        position[2] = new_mouseX;
                        position[3] = new_mouseY;

                        ShapeRenderer rectangle = new ShapeRenderer();
                        rectangle.begin(ShapeRenderer.ShapeType.Filled);
                        rectangle.setColor(Color.BLACK);
                        rectangle.rect(RockGolf.convert(mouseX), RockGolf.convert(mouseY),
                                RockGolf.convert(new_mouseX) - RockGolf.convert(mouseX),
                                RockGolf.convert(new_mouseY) - RockGolf.convert(mouseY));
                        rectangle.end();
                        new Obstacle(position, position[2] - position[0], position[3] - position[1]);

                    }

                }

            }

            // pseudo obstacle used to be able to see where you place the obstacle in the
            // end

            new Obstacle(position, 150, 50);
        } else if (keycode == Input.Keys.T) {
            checkForMenu = true;
            golf.state = "create";
        } else if (checkForMenu == true && keycode == Input.Keys.C) {
            System.out.println("im at here");
            treeflag = false;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.C) {
            System.out.println("im here");
            treeflag = true;
        }
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
