package com.rock.golf;

import java.lang.*;  
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.MouseInfo;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.rock.golf.Physics.Engine.Obstacle;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.Tree;



public class obstacleCreator implements InputProcessor{

    private RockGolf golf;
    private PhysicsEngine physics;
    private Obstacle obstacle;
    private Float[] position = new Float[4];

    public obstacleCreator(RockGolf rockGolf, PhysicsEngine engine) {
        this.golf = rockGolf;
        physics = engine;

    }
    
    @Override
    public boolean keyDown(int keycode) {

        golf.switchToObstacle();

        if (keycode == Input.Keys.R) {

            new MouseListenermain();
            

            // pseudo obstacle used to be able to see where you place the obstacle in the
            // end
            ShapeRenderer Rectangle = new ShapeRenderer();
            Rectangle.begin(ShapeRenderer.ShapeType.Filled);
            Rectangle.setColor(Color.BLACK);
            Rectangle.rect(position [0],  position [1], position [2]-position [0], position [3]- position [1]);


            new Obstacle(position.doubleValue(), 150, 50);

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


    public class MouseListenermain implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mousePressed(MouseEvent e) {

            double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
            double mouseY = MouseInfo.getPointerInfo().getLocation().getY();

            mouseX = position[0].doubleValue();
            mouseY = position[1].doubleValue();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            double newMouseX = MouseInfo.getPointerInfo().getLocation().getX();
            double newMouseY = MouseInfo.getPointerInfo().getLocation().getY();

            newMouseX = position[2].doubleValue();
            newMouseY = position[3].doubleValue();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

    }

}

    

    