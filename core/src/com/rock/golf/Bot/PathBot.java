package com.rock.golf.Bot;

import java.util.ArrayList;
import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Physics.Engine.PhysicsEngine;

public class PathBot extends Bot {
    private ArrayList<Node> path;
    private double[] currentShot;
    private Node furthestReach;

    public PathBot(PhysicsEngine engine, ArrayList<Node> path) {
        this.engine = engine;
        this.path = path;
        furthestReach = path.get(0);
    }

    @Override
    public double[] getMove() {
        long checkpoint = System.currentTimeMillis();
        if(wayIsFree(ballPos[0], ballPos[1], path.get(path.size()-1))){
            HillClimb helperBot = new HillClimb(engine, 1000, 200);
            return helperBot.getMove();
        }
        furthestReach = findNextTargetNode(ballPos[0], ballPos[1]);
        double nodeAngle = convert(Math.atan2(furthestReach.yCoord()-ballPos[1], furthestReach.xCoord()-ballPos[0]));
        double[][][] shots = GenerateShotRange(nodeAngle, 100, 135, 0, 5, 20);
        currentShot = processShotsNode(shots, path);
        if (path.indexOf(furthestReach) == (path.size()-1) || EuclideanDistance(engine.getSimulatedShot(currentShot[0], currentShot[1])) < targetRadius) {
            time = System.currentTimeMillis() - checkpoint;
            return currentShot;
        }
        time = System.currentTimeMillis() - checkpoint;
        return currentShot;
    }

    public Node findNextTargetNode(double ballX, double ballY){
        Node result = null;
        for(int i = 0; i < path.size(); i++){
            Node thisNode = path.get(i);
            if(wayIsFree(ballX , ballY, thisNode)){
                result = thisNode;
            }
        }
        return result;
    }

    public boolean wayIsFree(double ballX, double ballY, Node end){
        double nodeX = end.xCoord();
        double nodeY = end.yCoord();

        double distance = Math.sqrt(Math.pow(nodeX-ballX, 2) + Math.pow(nodeY-ballY, 2));
        double traceAngle = Math.atan2(nodeY-ballY, nodeX-ballX);
        double xComponent = Math.cos(traceAngle);
        double yComponent = Math.sin(traceAngle);
        double currentDistance = Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2));
        double scalar = distance / currentDistance;
        xComponent *= scalar;
        yComponent *= scalar;
        double steps = distance / 0.01;
        xComponent /= steps;
        yComponent /= steps;
        for(int i = 1; i <= steps; i++){
            if (engine.isInWater(ballX + (i * xComponent), ballY + (i * yComponent))
            || engine.collidedWithTree(ballX + (i * xComponent), ballY + (i * yComponent))
            || (engine.collidedWithObstacles(ballX + (i * xComponent), ballY + (i * yComponent))) != -1) {
                return false;
            }
        }
        return true;
    }

    public double[] processShotsNode(double[][][] shots, ArrayList<Node> path) {
        int furthestReachIndex = -1;
        double[] furthestReachCoords = new double[]{ballPos[0], ballPos[1]};
        double[] result = new double[2];
        for (int i = 0; i < shots[0].length; i++) {
            double[] shotCoords = engine.getSimulatedShot(shots[0][i][0], shots[0][i][1]);
            iterationsCounter++;
            Node reach = findNextTargetNode(shotCoords[0], shotCoords[1]);
            if(improvement(reach, furthestReachIndex, shotCoords, furthestReachCoords)){
                result = shots[0][i];
                furthestReachIndex = path.indexOf(reach);
                furthestReachCoords = shotCoords;
            }
            if (EuclideanDistance(shotCoords) < targetRadius) {
                return result;
            }
        }
        for (int i = 1; i < shots.length; i+=2) {
            for (int j = 0; j < shots[0].length; j++) {
                double[] shotCoords = engine.getSimulatedShot(shots[i][j][0], shots[i][j][1]);
                iterationsCounter++;
                Node reach = findNextTargetNode(shotCoords[0], shotCoords[1]);
                if(improvement(reach, furthestReachIndex, shotCoords, furthestReachCoords)){
                    result = shots[i][j];
                    furthestReachIndex = path.indexOf(reach);
                    furthestReachCoords = shotCoords;
                }
                if (EuclideanDistance(shotCoords) < targetRadius) {
                    return result;
                }

                shotCoords = engine.getSimulatedShot(shots[i + 1][j][0], shots[i + 1][j][1]);
                iterationsCounter++;
                reach = findNextTargetNode(shotCoords[0], shotCoords[1]);
                if(improvement(reach, furthestReachIndex, shotCoords, furthestReachCoords)){
                    result = shots[i+1][j];
                    furthestReachIndex = path.indexOf(reach);
                    furthestReachCoords = shotCoords;
                }
                if (EuclideanDistance(shotCoords) < targetRadius) {
                    return result;
                }
            }
        }
        return result;
    }
 
    private boolean improvement(Node reach, int bestReachIndex, double[] shotCoords, double[] previousShotCoords){
        if(path.indexOf(reach) == path.size()-1){
            if(EuclideanDistance(shotCoords) < EuclideanDistance(previousShotCoords)){
                return true;
            }
        } else if(path.indexOf(reach) > bestReachIndex){
            return true;
        }
        return false;
    }
}
