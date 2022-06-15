package com.rock.golf.Pathfinding;

import java.util.ArrayList;

import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.StateVector;

public class NodeFinder {
    private ArrayList<Node> path;
    private PhysicsEngine engine;

    public NodeFinder(ArrayList<Node> path, PhysicsEngine engine){
        this.path = path;
        this.engine = engine;
    }

    public Node findNextTargetNode(double ballX, double ballY){
        Node result = null;
        for(int i = 0; i < path.size(); i++){
            Node thisNode = path.get(i);
            if(unobstructed(ballX , ballY, thisNode)){
                result = thisNode;
            }
        }
        return result;
    }

    private boolean unobstructed(double ballX, double ballY, Node end){
        double startX = ballX;
        double startY = ballY;
        double endX = end.column*10;
        double endY = end.row*10;

        double distance = Math.sqrt(Math.pow(endX-startX, 2) + Math.pow(endY-startY, 2));
        double traceAngle = Math.atan2(endY-startY, endX-endY);
        double xComponent = Math.cos(traceAngle);
        double yComponent = Math.sin(traceAngle);
        double currentDistance = Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2));
        double scalar = distance / currentDistance;
        xComponent *= scalar;
        yComponent *= scalar;
        double steps = distance / 0.05;
        xComponent /= steps;
        yComponent /= steps;
        int counter = 0; 
        for(double i = 0; i < distance; i += 0.05){
            counter++;
            if (engine.isInWater(startX + counter * xComponent, startY + counter * yComponent)
                || engine.collidedWithTree(startX + counter * xComponent, startY + counter * yComponent)
                || (engine.collidedWithObstacles(startX + counter * xComponent, startY + counter * yComponent)) != -1) {
                    return true;
            }
        }
        return false;
    }
}
