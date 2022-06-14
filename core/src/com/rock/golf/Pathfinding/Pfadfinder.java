package com.rock.golf.Pathfinding;

import java.util.ArrayList;

import com.rock.golf.Physics.Engine.PhysicsEngine;

public class Pfadfinder {
    private ArrayList<Node> path;
    private PhysicsEngine engine;

    public Pfadfinder(ArrayList<Node> path, PhysicsEngine engine){
        this.path = path;
        this.engine = engine;
    }

    private Node findNextTargetNode(Node currentNode){
        int nextNodeIndex = path.indexOf(currentNode)+1;
        Node nextNode = path.get(nextNodeIndex);
        while(unobstructed(currentNode, nextNode)){
            nextNodeIndex++;
            nextNode = path.get(nextNodeIndex);
        }
        currentNode = path.get(path.indexOf(nextNode)-1);
        return path.get(path.indexOf(nextNode)-1);
    }

    private boolean unobstructed(Node start, Node end){
        double startX = start.column;
        double startY = start.row;
        double endX = end.column;
        double endY = end.row;

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
