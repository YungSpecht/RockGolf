package com.rock.golf;

import com.badlogic.gdx.InputProcessor;
import com.rock.golf.Physics.Engine.PhysicsEngine;

public class obstacleCreator implements InputProcessor {

    private RockGolf golf;
    private PhysicsEngine physics;

    public obstacleCreator(RockGolf rockGolf, PhysicsEngine engine) {
        this.golf = rockGolf;
        physics = engine;

    }

    @Override
    public boolean keyDown(int keycode) {
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
