package com.rock.golf.Physics.Engine;

public class rectangleObstacle {

    private double[] position;
    private double width;
    private double height;

    /**
     * Creates a new obstacele
     * 
     * @param position specifies x and y coordinates of left lower corner of a
     *                 square obstacle
     * @param length   specifies length of sides
     */
    public rectangleObstacle(double[] position, double width, double height) {
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
        if ((xPos + radius) >= position[0] || (xPos - radius) <= position[0] + width) {
            return true;
        } else if ((yPos + radius) >= position[1] || (yPos - radius) <= position[1] + height) {
            return true;
        } else
            return false;

    }

    public double[] bounce(double radius, double[] vector) {
        if ((vector[0] + radius) >= position[0] || (vector[0] - radius) <= position[0] + width) {
            return new double[] { -vector[2], vector[3] };
        } else if ((vector[1] + radius) >= position[1] || (vector[1] - radius) <= position[1] + height) {
            return new double[] { vector[2], -vector[3] };
        } else
            return new double[] { vector[2], vector[3] };

    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double[] getPosition() {
        return position;
    }
}
