package com.rock.golf.Bot;

import java.util.ArrayList;

import com.rock.golf.RockGolf;
import com.rock.golf.Pathfinding.Graph;
import com.rock.golf.Pathfinding.Node;
import com.rock.golf.Physics.Engine.PhysicsEngine;

public class ImplicitPathBot extends Bot{
    private ArrayList<Node> path;

    public ImplicitPathBot(PhysicsEngine engine, ArrayList<Node> path){
        this.engine = engine;
        this.path = path;
    }

    @Override
    public double[] getMove() {
        long checkpoint = System.currentTimeMillis();
        StochasticBot bot = new StochasticBot((PhysicsEngine) RockGolf.engine, 200);
        for (int i = 0; i < path.size(); i++) {
            double nodeX = (path.get(i).row * 10 - RockGolf.originX) / RockGolf.metertoPixelRatio;
            double nodeY = (path.get(i).column * 10 - RockGolf.originY) / RockGolf.metertoPixelRatio;
            double[] nodePosition = new double[] { nodeX, nodeY };
            double[] move = bot.getMoveTarget(nodePosition);
            if (move[2] < 0.05) {
                return move;
            } else {
                System.out.println("Node " + (i + 1) + " not shootable. Retrying...");
            }
        }
        time = System.currentTimeMillis() - checkpoint;
        return new double[] { 0, 0 };
    }
    
}
