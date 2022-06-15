package com.rock.golf.Bot;

import java.util.ArrayList;

import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Pathfinding.NodeFinder;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.StateVector;

public class PathBot extends Bot {
    private ArrayList<Node> path;
    private NodeFinder finder;
    private double[] currentShot;
    private double[] currentShotCoords;

    public PathBot(PhysicsEngine engine, ArrayList<Node> path) {
        this.engine = engine;
        this.path = path;
        finder = new NodeFinder(path, engine);
    }

    @Override
    public double[] getMove() {
        StateVector current = engine.getVector();
        Node furthestReach = finder.findNextTargetNode(current.getXPos(), current.getYPos());
        double nodeAngle = convert(Math.atan2(furthestReach.row*10-current.getYPos(), furthestReach.column*10-current.getXPos()));
        double[][][] shots = GenerateShotRange(nodeAngle, 3, 20, 2, 5, 10);
        currentShot = processShotsNode(shots, path, finder);
        return null;
    }
}
