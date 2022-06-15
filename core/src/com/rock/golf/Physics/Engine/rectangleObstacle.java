package com.rock.golf.Physics.Engine;

public class rectangleObstacle {
    private float[] position;
    private double width;
    private double height;

    /**
     * Creates a new obstacele
     * 
     * @param position specifies x and y coordinates of left lower corner of a
     *                 square obstacle
     * @param length   specifies length of sides
     */

    public rectangleObstacle(float[] position, double width, double height) {
        this.width = width;
        this.height = height;
        this.position = position;
    }

    /**
     * Verifies whether ball collided with the obstacle
     * 
     * @param xPos   position of the ball
     * @param yPos   position of the ball
     * @param radius radius of the ball
     * @return
     */

    public boolean obstacleCollision(double xPos, double yPos, double radius) {
        if (((xPos + radius) >= position[0] && (xPos + radius) <= position[0] + width && yPos + radius > position[1]
                && yPos - radius < (position[1] + height))
                || ((xPos - radius) <= position[0] + width && (xPos - radius) >= position[0]
                        && yPos + radius > position[1] && yPos - radius < position[1] + height)) {

            return true;
        } else if (((yPos + radius) >= position[1] && (yPos + radius) <= position[1] + height
                && xPos + radius > position[0] && xPos - radius < position[0] + width)
                || ((yPos - radius) <= position[1] + height && (yPos - radius) >= position[1]
                        && xPos + radius > position[0] && xPos - radius < position[0] + width)) {
            return true;
        } else
            return false;
    }

    public StateVector bounce(double radius, StateVector vector) {
        if (((vector.getXPos() + radius) >= position[0] && (vector.getXPos() + radius) <= position[0] + width
                && vector.getYPos() > position[1] && vector.getYPos() < position[1] + height)
                || ((vector.getXPos() - radius) <= position[0] + width && (vector.getXPos() - radius) >= position[0]
                        && (vector.getXPos() + radius) >= position[0] && vector.getYPos() > position[1]
                        && vector.getYPos() < position[1] + height)) {

            return new StateVector(vector.getXPos(), vector.getYPos(), -vector.getXSpeed(), vector.getYSpeed());
        } else if (((vector.getYPos() + radius) >= position[1] && (vector.getYPos() + radius) <= position[1] + height
                && (vector.getXPos() > position[0] && vector.getXPos() < position[0] + width))
                || ((vector.getYPos() - radius) <= position[1] + height && (vector.getYPos() - radius) >= position[1]
                        && (vector.getXPos() > position[0] && vector.getXPos() < position[0] + width))) {
            return new StateVector(vector.getXPos(), vector.getYPos(), vector.getXSpeed(), -vector.getYSpeed());
        } else
            return vector;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public float[] getPosition() {
        return position;
    }
}
