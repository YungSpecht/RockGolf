package com.rock.golf.Physics.Engine;

public class RectangleObstacle {
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

    public RectangleObstacle(float[] position, double width, double height) {
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
        double dx = Math.abs(xPos - position[0]-width/2);
        double dy = Math.abs(yPos - position[1]-height/2);

        if (dx > (width/2 + radius)) {return false;}
        if (dy > (height/2 + radius)) {return false;}

        if (dx <= (width/2)) {return true;} 
        if (dy <= (height/2)) {return true;}

        dx= dx-width/2;
        dy= dy-height/2;
        return (dx*dx+dy*dy<=(radius*radius));
    }

    public StateVector bounce(double radius, StateVector vector) {
        //left-box-side bounce
        if ((vector.getXPos() + radius) >= position[0] && (vector.getXPos() + radius) <= position[0] + width
                && vector.getYPos() > position[1] && vector.getYPos() < position[1] + height){
                    // if(this.obstacleCollision(vector.getXPos()-vector.getXSpeed(), vector.getYPos()+vector.getYSpeed(), radius)){
                    return new StateVector(position[0]-radius, vector.getYPos(), -vector.getXSpeed(), vector.getYSpeed());

        // right-box-side bounce
        }else if ((vector.getXPos() - radius) <= position[0] + width && (vector.getXPos() - radius) >= position[0]
                && (vector.getXPos() + radius) >= position[0] && vector.getYPos() > position[1]
                && vector.getYPos() < position[1] + height) {

            return new StateVector(position[0]+width+radius, vector.getYPos(), -vector.getXSpeed(), vector.getYSpeed());
        // bottom-box-side bounce    
        } else if ((vector.getYPos() + radius) >= position[1] && (vector.getYPos() + radius) <= position[1] + height
                && (vector.getXPos() > position[0] && vector.getXPos() < position[0] + width)){
                    return new StateVector(vector.getXPos(), position[1]-radius, vector.getXSpeed(), -vector.getYSpeed());
        //top-box-side bounce            
        }else if((vector.getYPos() - radius) <= position[1] + height && (vector.getYPos() - radius) >= position[1]
                        && (vector.getXPos() > position[0] && vector.getXPos() < position[0] + width)) {

            return new StateVector(vector.getXPos(), position[1]+radius+height, vector.getXSpeed(), -vector.getYSpeed());
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
