package com.rock.golf.Bot;

import java.util.ArrayList;

import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Pathfinding.NodeFinder;
import com.rock.golf.Physics.Engine.PhysicsEngine;
import com.rock.golf.Physics.Engine.StateVector;

public class PathBot extends Bot {
    private ArrayList<Node> path;
    private NodeFinder finder;

    public PathBot(PhysicsEngine engine, ArrayList<Node> path) {
        this.engine = engine;
        this.path = path;
        finder = new NodeFinder(path, engine);
    }

    @Override
    public double[] getMove() {
        StateVector current = engine.getVector();
        Node angleOfAttack = finder.findNextTargetNode(current.getXPos(), current.getYPos());
        return null;
    }
}
